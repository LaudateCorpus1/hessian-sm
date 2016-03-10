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
 * Serializing an object for known object types.
 */
public class EnumSerializer extends AbstractSerializer {
  private Method _name;
  
  public EnumSerializer(Class cl)
  {
    // hessian/32b[12], hessian/3ab[23]
    if (! cl.isEnum() && cl.getSuperclass().isEnum())
      cl = cl.getSuperclass();

    try {
      _name = cl.getMethod("name", new Class[0]);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public void writeObject(Object obj, AbstractHessianOutput out)
    throws IOException
  {
    if (out.addRef(obj))
      return;

    Class<?> cl = obj.getClass();

    if (! cl.isEnum() && cl.getSuperclass().isEnum())
      cl = cl.getSuperclass();

    String name = null;
    try {
      name = (String) _name.invoke(obj, (Object[]) null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    int ref = out.writeObjectBegin(cl.getName());

    if (ref < -1) {
      out.writeString("name");
      out.writeString(name);
      out.writeMapEnd();
    }
    else {
      if (ref == -1) {
        out.writeClassFieldLength(1);
        out.writeString("name");
        out.writeObjectBegin(cl.getName());
      }

      out.writeString(name);
    }
  }
}
