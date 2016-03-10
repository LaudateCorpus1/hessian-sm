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

import com.caucho.hessian.io.HessianRemoteResolver;

import java.io.IOException;

/**
 * Looks up remote objects in the proxy.
 */
public class HessianProxyResolver implements HessianRemoteResolver {
  private HessianProxyFactory _factory;
  
  /**
   * Creates an uninitialized Hessian remote resolver.
   */
  public HessianProxyResolver(HessianProxyFactory factory)
  {
    _factory = factory;
  }

  /**
   * Looks up a proxy object.
   */
  public Object lookup(String type, String url)
    throws IOException
  {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();

    try {
      Class api = Class.forName(type, false, loader);

      return _factory.create(api, url);
    } catch (Exception e) {
      throw new IOException(String.valueOf(e));
    }
  }
}
