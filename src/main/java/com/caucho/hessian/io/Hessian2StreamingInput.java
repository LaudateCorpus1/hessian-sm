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
import java.io.InputStream;

import java.util.logging.*;

/**
 * Input stream for Hessian 2 streaming requests using WebSocket.
 * 
 * For best performance, use HessianFactory:
 * 
 * <code><pre>
 * HessianFactory factory = new HessianFactory();
 * Hessian2StreamingInput hIn = factory.createHessian2StreamingInput(is);
 * </pre></code>
 */
public class Hessian2StreamingInput
{
  private static final Logger log
    = Logger.getLogger(Hessian2StreamingInput.class.getName());
  
  private StreamingInputStream _is;
  private Hessian2Input _in;
  
  /**
   * Creates a new Hessian input stream, initialized with an
   * underlying input stream.
   *
   * @param is the underlying output stream.
   */
  public Hessian2StreamingInput(InputStream is)
  {
    _is = new StreamingInputStream(is);
    _in = new Hessian2Input(_is);
  }

  public void setSerializerFactory(SerializerFactory factory)
  {
    _in.setSerializerFactory(factory);
  }

  public boolean isDataAvailable()
  {
    StreamingInputStream is = _is;
    
    return is != null && is.isDataAvailable();
  }

  public Hessian2Input startPacket()
    throws IOException
  {
    if (_is.startPacket()) {
      _in.resetReferences();
      _in.resetBuffer(); // XXX:
      return _in;
    }
    else
      return null;
  }

  public void endPacket()
    throws IOException
  {
    _is.endPacket();
    _in.resetBuffer(); // XXX:
  }

  public Hessian2Input getHessianInput()
  {
    return _in;
  }

  /**
   * Read the next object
   */
  public Object readObject()
    throws IOException
  {
    _is.startPacket();
    
    Object obj = _in.readStreamingObject();

    _is.endPacket();

    return obj;
  }

  /**
   * Close the output.
   */
  public void close()
    throws IOException
  {
    _in.close();
  }

  static class StreamingInputStream extends InputStream {
    private InputStream _is;
    
    private int _length;
    private boolean _isPacketEnd;

    StreamingInputStream(InputStream is)
    {
      _is = is;
    }

    public boolean isDataAvailable()
    {
      try {
        return _is != null && _is.available() > 0;
      } catch (IOException e) {
        log.log(Level.FINER, e.toString(), e);

        return true;
      }
    }

    public boolean startPacket()
      throws IOException
    {
      // skip zero-length packets
      do {
        _isPacketEnd = false;
      } while ((_length = readChunkLength(_is)) == 0);

      return _length > 0;
    }

    public void endPacket()
      throws IOException
    {
      while (! _isPacketEnd) {
        if (_length <= 0)
          _length = readChunkLength(_is);

        if (_length > 0) {
          _is.skip(_length);
          _length = 0;
        }
      }
      
      if (_length > 0) {
        _is.skip(_length);
        _length = 0;
      }
    }

    public int read()
      throws IOException
    {
      InputStream is = _is;
      
      if (_length == 0) {
        if (_isPacketEnd)
          return -1;
        
        _length = readChunkLength(is);

        if (_length <= 0)
          return -1;
      }

      _length--;
      
      return is.read();
    }

    @Override
    public int read(byte []buffer, int offset, int length)
      throws IOException
    {
      InputStream is = _is;
      
      if (_length <= 0) {
        if (_isPacketEnd)
          return -1;
        
        _length = readChunkLength(is);

        if (_length <= 0)
          return -1;
      }

      int sublen = _length;
      if (length < sublen)
        sublen = length;
      
      sublen = is.read(buffer, offset, sublen);

      if (sublen < 0)
        return -1;

      _length -= sublen;

      return sublen;
    }

    private int readChunkLength(InputStream is)
      throws IOException
    {
      if (_isPacketEnd)
        return -1;
      
      int length = 0;

      int code = is.read();

      if (code < 0) {
        _isPacketEnd = true;
        return -1;
      }
      
      _isPacketEnd = (code & 0x80) == 0;
      
      int len = is.read() & 0x7f;
      
      if (len < 0x7e) {
        length = len;
      }
      else if (len == 0x7e) {
        length = (((is.read() & 0xff) << 8)
                  + (is.read() & 0xff));
      }
      else {
        length = (((is.read() & 0xff) << 56)
                  + ((is.read() & 0xff) << 48)
                  + ((is.read() & 0xff) << 40)
                  + ((is.read() & 0xff) << 32)
                  + ((is.read() & 0xff) << 24)
                  + ((is.read() & 0xff) << 16)
                  + ((is.read() & 0xff) << 8)
                  + ((is.read() & 0xff)));
      }

      return length;
    }
  }
}
