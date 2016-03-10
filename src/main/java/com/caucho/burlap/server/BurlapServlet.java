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

package com.caucho.burlap.server;

import com.caucho.burlap.io.BurlapInput;
import com.caucho.burlap.io.BurlapOutput;
import com.caucho.services.server.Service;
import com.caucho.services.server.ServiceContext;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Servlet for serving Burlap services.
 */
public class BurlapServlet extends GenericServlet {
  private Class<?> _apiClass;
  private Object _service;
  
  private BurlapSkeleton _skeleton;

  public String getServletInfo()
  {
    return "Burlap Servlet";
  }

  /**
   * Sets the service class.
   */
  public void setService(Object service)
  {
    _service = service;
  }

  /**
   * Sets the api-class.
   */
  public void setAPIClass(Class<?> apiClass)
  {
    _apiClass = apiClass;
  }

  /**
   * Initialize the service, including the service object.
   */
  public void init(ServletConfig config)
    throws ServletException
  {
    super.init(config);
    
    try {
      if (_service == null) {
        String className = getInitParameter("service-class");
        Class<?> serviceClass = null;

        if (className != null) {
          ClassLoader loader = Thread.currentThread().getContextClassLoader();

          if (loader != null)
            serviceClass = Class.forName(className, false, loader);
          else
            serviceClass = Class.forName(className);
        }
        else {
          if (getClass().equals(BurlapServlet.class))
            throw new ServletException("server must extend BurlapServlet");

          serviceClass = getClass();
        }

        _service = serviceClass.newInstance();

        if (_service instanceof BurlapServlet)
          ((BurlapServlet) _service).setService(this);
        if (_service instanceof Service)
          ((Service) _service).init(getServletConfig());
        else if (_service instanceof Servlet)
          ((Servlet) _service).init(getServletConfig());
      }
      
      if (_apiClass == null) {
        String className = getInitParameter("api-class");

        if (className != null) {
          ClassLoader loader = Thread.currentThread().getContextClassLoader();

          if (loader != null)
            _apiClass = Class.forName(className, false, loader);
          else
            _apiClass = Class.forName(className);
        }
        else
          _apiClass = _service.getClass();
      }

      _skeleton = new BurlapSkeleton(_service, _apiClass);
    } catch (ServletException e) {
      throw e;
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
  
  /**
   * Execute a request.  The path-info of the request selects the bean.
   * Once the bean's selected, it will be applied.
   */
  public void service(ServletRequest request, ServletResponse response)
    throws IOException, ServletException
  {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    if (! req.getMethod().equals("POST")) {
      res.setStatus(500, "Burlap Requires POST");
      PrintWriter out = res.getWriter();

      res.setContentType("text/html");
      out.println("<h1>Burlap Requires POST</h1>");
      
      return;
    }

    String serviceId = req.getPathInfo();
    String objectId = req.getParameter("id");
    if (objectId == null)
      objectId = req.getParameter("ejbid");

    ServiceContext.begin(req, res, serviceId, objectId);

    try {
      InputStream is = request.getInputStream();
      OutputStream os = response.getOutputStream();

      BurlapInput in = new BurlapInput(is);
      BurlapOutput out = new BurlapOutput(os);

      _skeleton.invoke(in, out);
    } catch (RuntimeException e) {
      throw e;
    } catch (ServletException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServletException(e);
    } finally {
      ServiceContext.end();
    }
  }
}
