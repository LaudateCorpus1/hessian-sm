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
 * Serializing a remote object.
 */
public class ClassSerializer extends AbstractSerializer {
  public void writeObject(Object obj, AbstractHessianOutput out)
    throws IOException
  {
    Class cl = (Class) obj;

    if (cl == null) {
      out.writeNull();
    }
    else if (out.addRef(obj)) {
      return;
    }
    else {
      int ref = out.writeObjectBegin("java.lang.Class");

      if (ref < -1) {
        out.writeString("name");
        out.writeString(cl.getName());
        out.writeMapEnd();
      }
      else {
        if (ref == -1) {
          out.writeInt(1);
          out.writeString("name");
          out.writeObjectBegin("java.lang.Class");
        }

        out.writeString(cl.getName());
      }
    }
  }
}
