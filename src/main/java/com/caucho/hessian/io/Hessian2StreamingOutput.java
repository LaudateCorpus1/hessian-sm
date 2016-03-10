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

/**
 * Output stream for Hessian 2 streaming requests.
 */
public class Hessian2StreamingOutput
{
  private Hessian2Output _out;
  
  /**
   * Creates a new Hessian output stream, initialized with an
   * underlying output stream.
   *
   * @param os the underlying output stream.
   */
  public Hessian2StreamingOutput(OutputStream os)
  {
    _out = new Hessian2Output(os);
  }
  
  public Hessian2StreamingOutput(Hessian2Output out)
  {
    _out = out;
  }

  public Hessian2Output getHessian2Output()
  {
    return _out;
  }
  
  public void setCloseStreamOnClose(boolean isClose)
  {
    _out.setCloseStreamOnClose(isClose);
  }
  
  public boolean isCloseStreamOnClose()
  {
    return _out.isCloseStreamOnClose();
  }

  /**
   * Writes any object to the output stream.
   */
  public void writeObject(Object object)
    throws IOException
  {
    _out.writeStreamingObject(object);
  }

  /**
   * Flushes the output.
   */
  public void flush()
    throws IOException
  {
    _out.flush();
  }

  /**
   * Close the output.
   */
  public void close()
    throws IOException
  {
    _out.close();
  }
}
