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
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

/**
 * Interface for a service, including lifecycle.
 */
public class GenericService implements Service {
  protected ServletConfig config;
  
  /**
   * Initialize the service instance.
   */
  public void init(ServletConfig config)
    throws ServletException
  {
    this.config = config;

    init();
  }
  
  /**
   * Initialize the service instance.
   */
  public void init()
    throws ServletException
  {
  }

  /**
   * Returns the named initialization parameter.
   */
  public String getInitParameter(String name)
  {
    return this.config.getInitParameter(name);
  }

  /**
   * Returns the servlet context.
   */
  public ServletConfig getServletConfig()
  {
    return this.config;
  }

  /**
   * Returns the servlet context.
   */
  public ServletContext getServletContext()
  {
    return this.config.getServletContext();
  }

  /**
   * Logs a message to the error stream.
   */
  public void log(String message)
  {
    getServletContext().log(message);
  }

  /**
   * Returns the servlet request object for the request.
   */
  public ServletRequest getRequest()
  {
    return ServiceContext.getRequest();
  }

  /**
   * Returns the service identifier for the request.
   */
  public String getServiceName()
  {
    return ServiceContext.getServiceName();
  }

  /**
   * Returns the service identifier for the request.
   *
   * @deprecated
   */
  public String getServiceId()
  {
    return getServiceName();
  }

  /**
   * Returns the object identifier for the request.
   */
  public String getObjectId()
  {
    return ServiceContext.getObjectId();
  }
  
  /**
   * Cleanup the service instance.
   */
  public void destroy()
  {
  }
}
