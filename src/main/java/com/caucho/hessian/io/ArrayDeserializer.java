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
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Deserializing a Java array
 */
public class ArrayDeserializer extends AbstractListDeserializer {
  private Class _componentType;
  private Class _type;
  
  public ArrayDeserializer(Class componentType)
  {
    _componentType = componentType;
    
    if (_componentType != null) {
      try {
        _type = Array.newInstance(_componentType, 0).getClass();
      } catch (Exception e) {
      }
    }

    if (_type == null)
      _type = Object[].class;
  }

  public Class getType()
  {
    return _type;
  }

  /**
   * Reads the array.
   */
  public Object readList(AbstractHessianInput in, int length)
    throws IOException
  {
    if (length >= 0) {
      Object []data = createArray(length);

      in.addRef(data);
      
      if (_componentType != null) {
        for (int i = 0; i < data.length; i++)
          data[i] = in.readObject(_componentType);
      }
      else {
        for (int i = 0; i < data.length; i++)
          data[i] = in.readObject();
      }

      in.readListEnd();

      return data;
    }
    else {
      ArrayList list = new ArrayList();

      in.addRef(list);

      if (_componentType != null) {
        while (! in.isEnd())
          list.add(in.readObject(_componentType));
      }
      else {
        while (! in.isEnd())
          list.add(in.readObject());
      }

      in.readListEnd();

      Object []data = createArray(list.size());
      for (int i = 0; i < data.length; i++)
        data[i] = list.get(i);

      return data;
    }
  }

  /**
   * Reads the array.
   */
  public Object readLengthList(AbstractHessianInput in, int length)
    throws IOException
  {
    Object []data = createArray(length);

    in.addRef(data);
      
    if (_componentType != null) {
      for (int i = 0; i < data.length; i++)
        data[i] = in.readObject(_componentType);
    }
    else {
      for (int i = 0; i < data.length; i++)
        data[i] = in.readObject();
    }

    return data;
  }

  protected Object []createArray(int length)
  {
    if (_componentType != null)
      return (Object []) Array.newInstance(_componentType, length);
    else
      return new Object[length];
  }

  public String toString()
  {
    return "ArrayDeserializer[" + _componentType + "]";
  }
}
