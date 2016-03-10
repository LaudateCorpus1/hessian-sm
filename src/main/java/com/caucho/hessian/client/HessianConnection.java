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
public interface HessianConnection {
  /**
   * Adds HTTP headers.
   */
  public void addHeader(String key, String value);
  
  /**
   * Returns the output stream for the request.
   */
  public OutputStream getOutputStream()
    throws IOException;

  /**
   * Sends the query
   */
  public void sendRequest()
    throws IOException;

  /**
   * Returns the status code.
   */
  public int getStatusCode();

  /**
   * Returns the status string.
   */
  public String getStatusMessage();
  
  /**
   * Returns the content encoding
   */
  public String getContentEncoding();
  

  /**
   * Returns the InputStream to the result
   */
  public InputStream getInputStream()
    throws IOException;

  /**
   * Close/free the connection. If keepalive is allowed, it may be used.
   */
  public void close()
    throws IOException;

  /**
   * Shut the connection down.
   */
  public void destroy()
    throws IOException;
}

