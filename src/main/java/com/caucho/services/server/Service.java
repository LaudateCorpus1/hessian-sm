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

package com.caucho.services.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * Interface for a service lifecycle.
 *
 * <p>The lifecycle for a service starts with the <code>init</code>
 * method when the service starts.
 *
 * <code><pre>
 * myService.init(config);
 * ...
 * myService.hello();
 * ...
 * myService.hello();
 * ...
 * myService.destroy();
 * </pre></code>
 */
public interface Service {
  /**
   * Initialize the service instance.
   *
   * @param config the configuration for the service.
   */
  public void init(ServletConfig config)
    throws ServletException;
  
  /**
   * Cleanup the service instance.
   */
  public void destroy();
}
