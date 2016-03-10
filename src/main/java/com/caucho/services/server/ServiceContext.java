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

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.util.HashMap;

/**
 * Context for a service, to handle request-specific information.
 */
public class ServiceContext {
  private static final ThreadLocal<ServiceContext> _localContext
    = new ThreadLocal<ServiceContext>();

  private ServletRequest _request;
  private ServletResponse _response;
  private String _serviceName;
  private String _objectId;
  private int _count;
  private HashMap _headers = new HashMap();

  private ServiceContext()
  {
  }
  
  /**
   * Sets the request object prior to calling the service's method.
   *
   * @param request the calling servlet request
   * @param serviceId the service identifier
   * @param objectId the object identifier
   */
  public static void begin(ServletRequest request,
                           ServletResponse response,
                           String serviceName,
                           String objectId)
    throws ServletException
  {
    ServiceContext context = (ServiceContext) _localContext.get();

    if (context == null) {
      context = new ServiceContext();
      _localContext.set(context);
    }

    context._request = request;
    context._response = response;
    context._serviceName = serviceName;
    context._objectId = objectId;
    context._count++;
  }

  /**
   * Returns the service request.
   */
  public static ServiceContext getContext()
  {
    return (ServiceContext) _localContext.get();
  }

  /**
   * Adds a header.
   */
  public void addHeader(String header, Object value)
  {
    _headers.put(header, value);
  }

  /**
   * Gets a header.
   */
  public Object getHeader(String header)
  {
    return _headers.get(header);
  }

  /**
   * Gets a header from the context.
   */
  public static Object getContextHeader(String header)
  {
    ServiceContext context = (ServiceContext) _localContext.get();

    if (context != null)
      return context.getHeader(header);
    else
      return null;
  }

  /**
   * Returns the service request.
   */
  public static ServletRequest getContextRequest()
  {
    ServiceContext context = (ServiceContext) _localContext.get();

    if (context != null)
      return context._request;
    else
      return null;
  }

  /**
   * Returns the service request.
   */
  public static ServletResponse getContextResponse()
  {
    ServiceContext context = (ServiceContext) _localContext.get();

    if (context != null)
      return context._response;
    else
      return null;
  }

  /**
   * Returns the service id, corresponding to the pathInfo of the URL.
   */
  public static String getContextServiceName()
  {
    ServiceContext context = (ServiceContext) _localContext.get();

    if (context != null)
      return context._serviceName;
    else
      return null;
  }

  /**
   * Returns the object id, corresponding to the ?id= of the URL.
   */
  public static String getContextObjectId()
  {
    ServiceContext context = (ServiceContext) _localContext.get();

    if (context != null)
      return context._objectId;
    else
      return null;
  }

  /**
   * Cleanup at the end of a request.
   */
  public static void end()
  {
    ServiceContext context = (ServiceContext) _localContext.get();

    if (context != null && --context._count == 0) {
      context._request = null;
      context._response = null;

      context._headers.clear();
      
      _localContext.set(null);
    }
  }

  /**
   * Returns the service request.
   *
   * @deprecated
   */
  public static ServletRequest getRequest()
  {
    ServiceContext context = (ServiceContext) _localContext.get();

    if (context != null)
      return context._request;
    else
      return null;
  }

  /**
   * Returns the service id, corresponding to the pathInfo of the URL.
   *
   * @deprecated
   */
  public static String getServiceName()
  {
    ServiceContext context = (ServiceContext) _localContext.get();

    if (context != null)
      return context._serviceName;
    else
      return null;
  }

  /**
   * Returns the object id, corresponding to the ?id= of the URL.
   *
   * @deprecated
   */
  public static String getObjectId()
  {
    ServiceContext context = (ServiceContext) _localContext.get();

    if (context != null)
      return context._objectId;
    else
      return null;
  }
}
