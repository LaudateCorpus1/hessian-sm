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
import java.util.*;
import java.lang.reflect.*;

/**
 * Deserializing a JDK 1.2 Map.
 */
public class MapDeserializer extends AbstractMapDeserializer {
  private Class<?> _type;
  private Constructor<?> _ctor;
  
  public MapDeserializer(Class<?> type)
  {
    if (type == null)
      type = HashMap.class;
    
    _type = type;

    Constructor<?> []ctors = type.getConstructors();
    for (int i = 0; i < ctors.length; i++) {
      if (ctors[i].getParameterTypes().length == 0)
        _ctor = ctors[i];
    }

    if (_ctor == null) {
      try {
        _ctor = HashMap.class.getConstructor(new Class[0]);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }
  }
  
  public Class<?> getType()
  {
    if (_type != null)
      return _type;
    else
      return HashMap.class;
  }

  public Object readMap(AbstractHessianInput in)
    throws IOException
  {
    Map map;
    
    if (_type == null)
      map = new HashMap();
    else if (_type.equals(Map.class))
      map = new HashMap();
    else if (_type.equals(SortedMap.class))
      map = new TreeMap();
    else {
      try {
        map = (Map) _ctor.newInstance();
      } catch (Exception e) {
        throw new IOExceptionWrapper(e);
      }
    }

    in.addRef(map);

    while (! in.isEnd()) {
      map.put(in.readObject(), in.readObject());
    }

    in.readEnd();

    return map;
  }

  @Override
  public Object readObject(AbstractHessianInput in,
                           Object []fields)
    throws IOException
  {
    String []fieldNames = (String []) fields;
    Map<Object,Object> map = createMap();
      
    int ref = in.addRef(map);

    for (int i = 0; i < fieldNames.length; i++) {
      String name = fieldNames[i];

      map.put(name, in.readObject());
    }

    return map;
  }

  private Map createMap()
    throws IOException
  {
    
    if (_type == null)
      return new HashMap();
    else if (_type.equals(Map.class))
      return new HashMap();
    else if (_type.equals(SortedMap.class))
      return new TreeMap();
    else {
      try {
        return (Map) _ctor.newInstance();
      } catch (Exception e) {
        throw new IOExceptionWrapper(e);
      }
    }
  }
}
