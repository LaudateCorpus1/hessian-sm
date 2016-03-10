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

package com.caucho.hessian.client;

import java.net.URL;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Internal connection to a server.  The default connection is based on
 * java.net
 */
abstract public class AbstractHessianConnection implements HessianConnection {
  /**
   * Adds HTTP headers.
   */
  public void addHeader(String key, String value)
  {
  }
  
  /**
   * Returns the output stream for the request.
   */
  abstract public OutputStream getOutputStream()
    throws IOException;

  /**
   * Sends the query
   */
  abstract public void sendRequest()
    throws IOException;

  /**
   * Returns the status code.
   */
  abstract public int getStatusCode();

  /**
   * Returns the status string.
   */
  abstract public String getStatusMessage();

  /**
   * Returns the InputStream to the result
   */
  abstract public InputStream getInputStream()
    throws IOException;

  @Override
  public String getContentEncoding()
  {
    return null;
  }
  
  /**
   * Close/free the connection, using keepalive if appropriate.
   */
  public void close()
    throws IOException
  {
    destroy();
  }

  /**
   * Destroy/disconnect the connection
   */
  abstract public void destroy()
    throws IOException;
}

