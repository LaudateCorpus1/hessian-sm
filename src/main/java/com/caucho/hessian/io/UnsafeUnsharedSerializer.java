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
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Serializing an object for known object types.
 */
public class UnsafeUnsharedSerializer extends UnsafeSerializer
{
  private static final Logger log
    = Logger.getLogger(UnsafeUnsharedSerializer.class.getName());
  
  public UnsafeUnsharedSerializer(Class<?> cl)
  {
    super(cl);
  }
  
  @Override
  public void writeObject(Object obj, AbstractHessianOutput out)
    throws IOException
  {
    boolean oldUnshared = out.setUnshared(true);
    
    try {
      super.writeObject(obj, out);
    } finally {
      out.setUnshared(oldUnshared);
    }
  }
}
