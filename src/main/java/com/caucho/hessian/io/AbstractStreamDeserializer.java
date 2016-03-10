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
 * Deserializing a byte stream
 */
abstract public class AbstractStreamDeserializer extends AbstractDeserializer {
  abstract public Class<?> getType();
  
  /**
   * Reads the Hessian 1.0 style map.
   */
  @Override
  public Object readMap(AbstractHessianInput in)
    throws IOException
  {
    Object value = null;
    
    while (! in.isEnd()) {
      String key = in.readString();

      if (key.equals("value"))
        value = readStreamValue(in);
      else
        in.readObject();
    }

    in.readMapEnd();

    return value;
  }
  
  @Override
  public Object readObject(AbstractHessianInput in, Object []fields)
    throws IOException
  {
    String []fieldNames = (String []) fields;
    
    Object value = null;

    for (int i = 0; i < fieldNames.length; i++) {
      if ("value".equals(fieldNames[i])) {
        value = readStreamValue(in);
        in.addRef(value);
      }
      else {
        in.readObject();
      }
    }

    return value;
  }

  abstract protected Object readStreamValue(AbstractHessianInput in)
    throws IOException;
}
