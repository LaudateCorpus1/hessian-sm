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
import java.io.IOException;

/**
 * Internal factory for creating connections to the server.  The default
 * factory is java.net
 */
abstract public class AbstractHessianConnectionFactory
  implements HessianConnectionFactory
{
  private HessianProxyFactory _factory;
  
  /**
   * The HessianProxyFactory contains some common network
   * configuration like timeouts.
   */
  public void setHessianProxyFactory(HessianProxyFactory factory)
  {
    _factory = factory;
  }
  
  /**
   * The HessianProxyFactory contains some common network
   * configuration like timeouts.
   */
  public HessianProxyFactory getHessianProxyFactory()
  {
    return _factory;
  }
  
  /**
   * Opens a new or recycled connection to the HTTP server.
   */
  abstract public HessianConnection open(URL url)
    throws IOException;
}
