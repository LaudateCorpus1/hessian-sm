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
import java.net.URLConnection;
import java.net.HttpURLConnection;

import java.io.IOException;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Internal factory for creating connections to the server.  The default
 * factory is java.net
 */
public class HessianURLConnectionFactory implements HessianConnectionFactory {
  private static final Logger log
    = Logger.getLogger(HessianURLConnectionFactory.class.getName());
  
  private HessianProxyFactory _proxyFactory;

  public void setHessianProxyFactory(HessianProxyFactory factory)
  {
    _proxyFactory = factory;
  }
  
  /**
   * Opens a new or recycled connection to the HTTP server.
   */
  public HessianConnection open(URL url)
    throws IOException
  {
    if (log.isLoggable(Level.FINER))
      log.finer(this + " open(" + url + ")");

    URLConnection conn = url.openConnection();

    // HttpURLConnection httpConn = (HttpURLConnection) conn;
    // httpConn.setRequestMethod("POST");
    // conn.setDoInput(true);

    long connectTimeout = _proxyFactory.getConnectTimeout();

    if (connectTimeout >= 0)
      conn.setConnectTimeout((int) connectTimeout);

    conn.setDoOutput(true);

    long readTimeout = _proxyFactory.getReadTimeout();

    if (readTimeout > 0) {
      try {
        conn.setReadTimeout((int) readTimeout);
      } catch (Throwable e) {
      }
    }

    /*
    // Used chunked mode when available, i.e. JDK 1.5.
    if (_proxyFactory.isChunkedPost() && conn instanceof HttpURLConnection) {
      try {
        HttpURLConnection httpConn = (HttpURLConnection) conn;

        httpConn.setChunkedStreamingMode(8 * 1024);
      } catch (Throwable e) {
      }
    }
    */
    
    return new HessianURLConnection(url, conn);
  }
}
