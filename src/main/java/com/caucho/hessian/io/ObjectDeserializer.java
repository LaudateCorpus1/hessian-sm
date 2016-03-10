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
 * Serializing an object for known object types.
 */
public class ObjectDeserializer extends AbstractDeserializer {
  private Class<?> _cl;

  public ObjectDeserializer(Class<?> cl)
  {
    _cl = cl;
  }

  public Class<?> getType()
  {
    return _cl;
  }
  
  @Override  
  public Object readObject(AbstractHessianInput in)
    throws IOException
  {
    return in.readObject();
  }

  @Override
  public Object readObject(AbstractHessianInput in, Object []fields)
    throws IOException
  {
    throw new UnsupportedOperationException(String.valueOf(this));
  }
  
  @Override  
  public Object readList(AbstractHessianInput in, int length)
    throws IOException
  {
    throw new UnsupportedOperationException(String.valueOf(this));
  }
  
  @Override  
  public Object readLengthList(AbstractHessianInput in, int length)
    throws IOException
  {
    throw new UnsupportedOperationException(String.valueOf(this));
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + _cl + "]";
  }
}
