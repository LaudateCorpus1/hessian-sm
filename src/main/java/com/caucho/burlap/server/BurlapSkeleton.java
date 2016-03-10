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
import com.caucho.services.server.AbstractSkeleton;
import com.caucho.services.server.ServiceContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.*;

/**
 * Proxy class for Burlap services.
 */
public class BurlapSkeleton extends AbstractSkeleton {
  private static final Logger log
    = Logger.getLogger(BurlapSkeleton.class.getName());
  
  private Object _service;
  
  /**
   * Create a new burlap skeleton.
   *
   * @param service the underlying service object.
   * @param apiClass the API interface
   */
  public BurlapSkeleton(Object service, Class apiClass)
  {
    super(apiClass);

    _service = service;
  }
  
  /**
   * Create a new burlap skeleton.
   *
   * @param service the underlying service object.
   * @param apiClass the API interface
   */
  public BurlapSkeleton(Class apiClass)
  {
    super(apiClass);
  }

  /**
   * Invoke the object with the request from the input stream.
   *
   * @param in the Burlap input stream
   * @param out the Burlap output stream
   */
  public void invoke(BurlapInput in, BurlapOutput out)
    throws Exception
  {
    invoke(_service, in, out);
  }

  /**
   * Invoke the object with the request from the input stream.
   *
   * @param in the Burlap input stream
   * @param out the Burlap output stream
   */
  public void invoke(Object service, BurlapInput in, BurlapOutput out)
    throws Exception
  {
    in.readCall();

    ServiceContext context = ServiceContext.getContext();
    
    String header;
    while ((header = in.readHeader()) != null) {
      Object value = in.readObject();

      context.addHeader(header, value);
    }

    String methodName = in.readMethod();
    Method method = getMethod(methodName);

    if (log.isLoggable(Level.FINE))
      log.fine(this + " invoking " + methodName + " (" + method + ")");

    if (method != null) {
    }
    else if ("_burlap_getAttribute".equals(in.getMethod())) {
      String attrName = in.readString();
      in.completeCall();

      String value = null;

      if ("java.api.class".equals(attrName))
        value = getAPIClassName();
      else if ("java.home.class".equals(attrName))
        value = getHomeClassName();
      else if ("java.object.class".equals(attrName))
        value = getObjectClassName();

      out.startReply();

      out.writeObject(value);

      out.completeReply();
      return;
    }
    else if (method == null) {
      out.startReply();
      out.writeFault("NoSuchMethodException",
                     "The service has no method named: " + in.getMethod(),
                     null);
      out.completeReply();
      return;
    }

    Class []args = method.getParameterTypes();
    Object []values = new Object[args.length];

    for (int i = 0; i < args.length; i++)
      values[i] = in.readObject(args[i]);

    in.completeCall();

    Object result = null;
    
    try {
      result = method.invoke(service, values);
    } catch (Throwable e) {
      log.log(Level.FINE,
              service + "." + method.getName() + "() failed with exception:\n"
              + e.toString(),
              e);
      
      if (e instanceof InvocationTargetException
          && e.getCause() instanceof Exception)
        e = ((InvocationTargetException) e).getTargetException();
      out.startReply();
      out.writeFault("ServiceException", e.getMessage(), e);
      out.completeReply();
      return;
    }

    out.startReply();

    out.writeObject(result);
    
    out.completeReply();
  }
}
