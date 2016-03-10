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

package com.caucho.hessian.jmx;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;

import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.io.IOException;

/**
 * Deserializing an ObjectInstance valued object
 */
public class ObjectInstanceDeserializer extends AbstractDeserializer {
  public Class getType()
  {
    return ObjectInstance.class;
  }
  
  public Object readMap(AbstractHessianInput in)
    throws IOException
  {
    String className = null;
    ObjectName objectName = null;
    
    String initValue = null;
    
    while (! in.isEnd()) {
      String key = in.readString();

      if ("className".equals(key))
        className = in.readString();
      else if ("name".equals(key))
        objectName = (ObjectName) in.readObject(ObjectName.class);
      else
        in.readObject();
    }

    in.readMapEnd();

    try {
      return new ObjectInstance(objectName, className);
    } catch (Exception e) {
      throw new IOException(String.valueOf(e));
    }
  }
}
