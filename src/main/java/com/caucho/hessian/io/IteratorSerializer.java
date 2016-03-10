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
import java.util.Iterator;

/**
 * Serializing a JDK 1.2 Iterator.
 */
public class IteratorSerializer extends AbstractSerializer {
  private static IteratorSerializer _serializer;

  public static IteratorSerializer create()
  {
    if (_serializer == null)
      _serializer = new IteratorSerializer();

    return _serializer;
  }
  
  public void writeObject(Object obj, AbstractHessianOutput out)
    throws IOException
  {
    Iterator iter = (Iterator) obj;

    boolean hasEnd = out.writeListBegin(-1, null);

    while (iter.hasNext()) {
      Object value = iter.next();

      out.writeObject(value);
    }

    if (hasEnd)
      out.writeListEnd();
  }
}
