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
import java.util.HashMap;

/**
 * Deserializing a JDK 1.2 Class.
 */
public class ClassDeserializer extends AbstractMapDeserializer {
  private static final HashMap<String,Class> _primClasses
    = new HashMap<String,Class>();

  private ClassLoader _loader;
  
  public ClassDeserializer(ClassLoader loader)
  {
    _loader = loader;
  }
  
  public Class getType()
  {
    return Class.class;
  }
  
  public Object readMap(AbstractHessianInput in)
    throws IOException
  {
    int ref = in.addRef(null);
    
    String name = null;
    
    while (! in.isEnd()) {
      String key = in.readString();

      if (key.equals("name"))
        name = in.readString();
      else
        in.readObject();
    }
      
    in.readMapEnd();

    Object value = create(name);

    in.setRef(ref, value);

    return value;
  }
  
  public Object readObject(AbstractHessianInput in, Object []fields)
    throws IOException
  {
    String []fieldNames = (String []) fields;
      
    int ref = in.addRef(null);
    
    String name = null;
    
    for (int i = 0; i < fieldNames.length; i++) {
      if ("name".equals(fieldNames[i]))
        name = in.readString();
      else
        in.readObject();
    }

    Object value = create(name);

    in.setRef(ref, value);

    return value;
  }

  Object create(String name)
    throws IOException
  {
    if (name == null)
      throw new IOException("Serialized Class expects name.");

    Class cl = _primClasses.get(name);

    if (cl != null)
      return cl;

    try {
      if (_loader != null)
        return Class.forName(name, false, _loader);
      else
        return Class.forName(name);
    } catch (Exception e) {
      throw new IOExceptionWrapper(e);
    }
  }

  static {
    _primClasses.put("void", void.class);
    _primClasses.put("boolean", boolean.class);
    _primClasses.put("java.lang.Boolean", Boolean.class);
    _primClasses.put("byte", byte.class);
    _primClasses.put("java.lang.Byte", Byte.class);
    _primClasses.put("char", char.class);
    _primClasses.put("java.lang.Character", Character.class);
    _primClasses.put("short", short.class);
    _primClasses.put("java.lang.Short", Short.class);
    _primClasses.put("int", int.class);
    _primClasses.put("java.lang.Integer", Integer.class);
    _primClasses.put("long", long.class);
    _primClasses.put("java.lang.Long", Long.class);
    _primClasses.put("float", float.class);
    _primClasses.put("java.lang.Float", Float.class);
    _primClasses.put("double", double.class);
    _primClasses.put("java.lang.Double", Double.class);
    _primClasses.put("java.lang.String", String.class);
  }
}
