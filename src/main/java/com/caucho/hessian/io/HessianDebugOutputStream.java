/*
 *  Copyright 2012-2015 Viant.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

package com.caucho.hessian.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.io.PrintWriter;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Debugging output stream for Hessian requests.
 */
public class HessianDebugOutputStream extends OutputStream
{
  private static final Logger log
    = Logger.getLogger(HessianDebugOutputStream.class.getName());
  
  private OutputStream _os;
  
  private HessianDebugState _state;
  
  /**
   * Creates an uninitialized Hessian input stream.
   */
  public HessianDebugOutputStream(OutputStream os, PrintWriter dbg)
  {
    _os = os;

    _state = new HessianDebugState(dbg);
  }
  
  /**
   * Creates an uninitialized Hessian input stream.
   */
  public HessianDebugOutputStream(OutputStream os, Logger log, Level level)
  {
    this(os, new PrintWriter(new LogWriter(log, level)));
  }
  
  /**
   * Creates an uninitialized Hessian input stream.
   */
  public HessianDebugOutputStream(Logger log, Level level)
  {
    this(null, new PrintWriter(new LogWriter(log, level)));
  }
  
  public void initPacket(OutputStream os)
  {
    _os = os;
  }

  public void startTop2()
  {
    _state.startTop2();
  }

  public void startStreaming()
  {
    _state.startStreaming();
  }

  /**
   * Writes a character.
   */
  @Override
  public void write(int ch)
    throws IOException
  {
    ch = ch & 0xff;
    
    _os.write(ch);

    try {
      _state.next(ch);
    } catch (Exception e) {
      log.log(Level.WARNING, e.toString(), e);
    }
  }

  @Override
  public void flush()
    throws IOException
  {
    _os.flush();
  }

  /**
   * closes the stream.
   */
  @Override
  public void close()
    throws IOException
  {
    OutputStream os = _os;
    _os = null;

    if (os != null) {
      _state.next(-1);
      os.close();
    }

    _state.println();
  }

  static class LogWriter extends Writer {
    private Logger _log;
    private Level _level;
    private StringBuilder _sb = new StringBuilder();

    LogWriter(Logger log, Level level)
    {
      _log = log;
      _level = level;
    }

    public void write(char ch)
    {
      if (ch == '\n' && _sb.length() > 0) {
        _log.log(_level, _sb.toString());
        _sb.setLength(0);
      }
      else
        _sb.append((char) ch);
    }

    public void write(char []buffer, int offset, int length)
    {
      for (int i = 0; i < length; i++) {
        char ch = buffer[offset + i];

        if (ch == '\n' && _sb.length() > 0) {
          _log.log(_level, _sb.toString());
          _sb.setLength(0);
        }
        else
          _sb.append((char) ch);
      }
    }

    public void flush()
    {
    }

    public void close()
    {
    }
  }
}
