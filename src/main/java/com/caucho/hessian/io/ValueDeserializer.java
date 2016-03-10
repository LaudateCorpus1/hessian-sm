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
 * Deserializing a string valued object
 */
abstract public class ValueDeserializer extends AbstractDeserializer {
  public Object readMap(AbstractHessianInput in)
    throws IOException
  {
    String initValue = null;
    
    while (! in.isEnd()) {
      String key = in.readString();

      if (key.equals("value"))
        initValue = in.readString();
      else
        in.readObject();
    }

    in.readMapEnd();

    return create(initValue);
  }
  
  public Object readObject(AbstractHessianInput in, String []fieldNames)
    throws IOException
  {
    String initValue = null;

    for (int i = 0; i < fieldNames.length; i++) {
      if ("value".equals(fieldNames[i]))
        initValue = in.readString();
      else
        in.readObject();
    }

    return create(initValue);
  }

  abstract Object create(String value)
    throws IOException;
}
