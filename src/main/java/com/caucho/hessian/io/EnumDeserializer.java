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
import java.lang.reflect.Method;

/**
 * Deserializing an enum valued object
 */
public class EnumDeserializer extends AbstractDeserializer {
  private Class _enumType;
  private Method _valueOf;
  
  public EnumDeserializer(Class cl)
  {
    // hessian/33b[34], hessian/3bb[78]
    if (cl.isEnum())
      _enumType = cl;
    else if (cl.getSuperclass().isEnum())
      _enumType = cl.getSuperclass();
    else
      throw new RuntimeException("Class " + cl.getName() + " is not an enum");

    try {
      _valueOf = _enumType.getMethod("valueOf",
                             new Class[] { Class.class, String.class });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public Class getType()
  {
    return _enumType;
  }
  
  public Object readMap(AbstractHessianInput in)
    throws IOException
  {
    String name = null;
    
    while (! in.isEnd()) {
      String key = in.readString();

      if (key.equals("name"))
        name = in.readString();
      else
        in.readObject();
    }

    in.readMapEnd();

    Object obj = create(name);
    
    in.addRef(obj);

    return obj;
  }
  
  @Override
  public Object readObject(AbstractHessianInput in, Object []fields)
    throws IOException
  {
    String []fieldNames = (String []) fields;
    String name = null;

    for (int i = 0; i < fieldNames.length; i++) {
      if ("name".equals(fieldNames[i]))
        name = in.readString();
      else
        in.readObject();
    }

    Object obj = create(name);

    in.addRef(obj);

    return obj;
  }

  private Object create(String name)
    throws IOException
  {
    if (name == null)
      throw new IOException(_enumType.getName() + " expects name.");

    try {
      return _valueOf.invoke(null, _enumType, name);
    } catch (Exception e) {
      throw new IOExceptionWrapper(e);
    }
  }
}
