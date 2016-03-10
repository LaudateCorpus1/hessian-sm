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

package com.caucho.hessian.io;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.*;

import com.caucho.hessian.HessianException;

/**
 * Proxy for a java annotation for known object types.
 */
public class AnnotationInvocationHandler implements InvocationHandler {
  private Class _annType;
  private HashMap<String,Object> _valueMap;

  public AnnotationInvocationHandler(Class annType,
                                     HashMap<String,Object> valueMap)
  {
    _annType = annType;
    _valueMap = valueMap;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object []args)
    throws Throwable
  {
    String name = method.getName();

    boolean zeroArgs = args == null || args.length == 0;
    
    if (name.equals("annotationType") && zeroArgs)
      return _annType;
    else if (name.equals("toString") && zeroArgs)
      return toString();
    else if (name.equals("hashCode") && zeroArgs)
      return doHashCode();
    else if (name.equals("equals") && ! zeroArgs && args.length == 1)
      return doEquals(args[0]);
    else if (! zeroArgs)
      return null;


    return _valueMap.get(method.getName());
  }
  
  public int doHashCode()
  {
    return 13;
  }
  
  public boolean doEquals(Object value)
  {
    if (! (value instanceof Annotation))
      return false;
    
    Annotation ann = (Annotation) value;
    
    if (! _annType.equals(ann.annotationType()))
      return false;
    
    return true;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    
    sb.append("@");
    sb.append(_annType.getName());
    sb.append("[");

    boolean isFirst = true;
    for (Map.Entry entry : _valueMap.entrySet()) {
      if (! isFirst)
        sb.append(", ");
      isFirst = false;

      sb.append(entry.getKey());
      sb.append("=");

      if (entry.getValue() instanceof String)
        sb.append('"').append(entry.getValue()).append('"');
      else
        sb.append(entry.getValue());
    }
    sb.append("]");

    return sb.toString();
  }
}
