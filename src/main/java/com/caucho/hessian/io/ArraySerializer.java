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

/**
 * Serializing a Java array.
 */
public class ArraySerializer extends AbstractSerializer {
  public void writeObject(Object obj, AbstractHessianOutput out)
    throws IOException
  {
    if (out.addRef(obj))
      return;

    Object []array = (Object []) obj;

    boolean hasEnd = out.writeListBegin(array.length,
                                        getArrayType(obj.getClass()));

    for (int i = 0; i < array.length; i++)
      out.writeObject(array[i]);

    if (hasEnd)
      out.writeListEnd();
  }

  /**
   * Returns the &lt;type> name for a &lt;list>.
   */
  private String getArrayType(Class cl)
  {
    if (cl.isArray())
      return '[' + getArrayType(cl.getComponentType());

    String name = cl.getName();

    if (name.equals("java.lang.String"))
      return "string";
    else if (name.equals("java.lang.Object"))
      return "object";
    else if (name.equals("java.util.Date"))
      return "date";
    else
      return name;
  }
}
