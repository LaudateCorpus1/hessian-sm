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

import javax.management.MBeanConstructorInfo;
import javax.management.MBeanParameterInfo;
import java.io.IOException;

/**
 * Deserializing an MBeanConstructorInfo valued object
 */
public class MBeanConstructorInfoDeserializer extends AbstractDeserializer {
  public Class getType()
  {
    return MBeanConstructorInfo.class;
  }
  
  public Object readMap(AbstractHessianInput in)
    throws IOException
  {
    String name = null;
    String description = null;
    MBeanParameterInfo []sig = null;
    
    while (! in.isEnd()) {
      String key = in.readString();

      if ("name".equals(key))
        name = in.readString();
      else if ("description".equals(key))
        description = in.readString();
      else if ("signature".equals(key))
        sig = (MBeanParameterInfo[]) in.readObject(MBeanParameterInfo[].class);
      else {
        in.readObject();
      }
    }

    in.readMapEnd();

    try {
      MBeanConstructorInfo info;

      info = new MBeanConstructorInfo(name, description, sig);
      
      return info;
    } catch (Exception e) {
      throw new IOException(String.valueOf(e));
    }
  }
}
