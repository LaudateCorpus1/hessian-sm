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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;

import java.util.logging.*;

import com.caucho.hessian.HessianException;

/**
 * Deserializing a java annotation for known object types.
 */
public class AnnotationDeserializer extends AbstractMapDeserializer {
  private static final Logger log
    = Logger.getLogger(AnnotationDeserializer.class.getName());
  
  private Class _annType;

  public AnnotationDeserializer(Class annType)
  {
    _annType = annType;
  }

  public Class getType()
  {
    return _annType;
  }
    
  public Object readMap(AbstractHessianInput in)
    throws IOException
  {
    try {
      int ref = in.addRef(null);

      HashMap<String,Object> valueMap = new HashMap<String,Object>(8);

      while (! in.isEnd()) {
        String key = in.readString();
        Object value = in.readObject();

        valueMap.put(key, value);
      }
      
      in.readMapEnd();

      return Proxy.newProxyInstance(_annType.getClassLoader(),
                                    new Class[] { _annType },
                                    new AnnotationInvocationHandler(_annType, valueMap));
      
    } catch (IOException e) {
      throw e;
    } catch (Exception e) {
      throw new IOExceptionWrapper(e);
    }
  }
    
  public Object readObject(AbstractHessianInput in,
                           Object []fields)
    throws IOException
  {
    String []fieldNames = (String []) fields;
    
    try {
      in.addRef(null);

      HashMap<String,Object> valueMap = new HashMap<String,Object>(8);
      
      for (int i = 0; i < fieldNames.length; i++) {
        String name = fieldNames[i];

        valueMap.put(name, in.readObject());
      }

      return Proxy.newProxyInstance(_annType.getClassLoader(),
                                    new Class[] { _annType },
                                    new AnnotationInvocationHandler(_annType, valueMap));
      
    } catch (IOException e) {
      throw e;
    } catch (Exception e) {
      throw new HessianException(_annType.getName() + ":" + e, e);
    }
  }
}
