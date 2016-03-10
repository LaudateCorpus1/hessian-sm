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

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import java.io.IOException;

/**
 * Deserializing an MBeanInfo valued object
 */
public class MBeanInfoDeserializer extends AbstractDeserializer {
  public Class getType()
  {
    return MBeanInfo.class;
  }
  
  public Object readMap(AbstractHessianInput in)
    throws IOException
  {
    String className = null;
    String description = null;
    MBeanAttributeInfo []attributes = null;
    MBeanConstructorInfo []constructors = null;
    MBeanOperationInfo []operations = null;
    MBeanNotificationInfo []notifications = null;
    
    while (! in.isEnd()) {
      String key = in.readString();

      if ("className".equals(key))
        className = in.readString();
      else if ("description".equals(key))
        description = in.readString();
      else if ("attributes".equals(key)) {
        attributes = (MBeanAttributeInfo []) in.readObject(MBeanAttributeInfo[].class);
      }
      /*
      else if ("isWrite".equals(key))
        isWrite = in.readBoolean();
      else if ("isIs".equals(key))
        isIs = in.readBoolean();
      */
      else
        in.readObject();
    }

    in.readMapEnd();

    try {
      MBeanInfo info;
      
      info = new MBeanInfo(className, description, attributes,
                           constructors, operations, notifications);

      return info;
    } catch (Exception e) {
      throw new IOException(String.valueOf(e));
    }
  }
}
