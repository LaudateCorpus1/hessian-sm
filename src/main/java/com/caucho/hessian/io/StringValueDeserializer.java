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
import java.lang.reflect.Constructor;

import com.caucho.hessian.HessianException;

/**
 * Deserializing a string valued object
 */
public class StringValueDeserializer extends AbstractStringValueDeserializer {
  private Class _cl;
  private Constructor _constructor;
  
  public StringValueDeserializer(Class cl)
  {
    try {
      _cl = cl;
      _constructor = cl.getConstructor(new Class[] { String.class });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public Class getType()
  {
    return _cl;
  }

  @Override
  protected Object create(String value)
    throws IOException
  {
    if (value == null)
      throw new IOException(_cl.getName() + " expects name.");

    try {
      return _constructor.newInstance(new Object[] { value });
    } catch (Exception e) {
      throw new HessianException(_cl.getName() + ": value=" + value + "\n" + e,
                                 e);
    }
  }
}
