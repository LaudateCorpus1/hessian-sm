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

/**
 * Deserializing a JDK 1.2 Collection.
 */
public class CollectionDeserializer extends AbstractListDeserializer {
  private Class _type;
  
  public CollectionDeserializer(Class type)
  {
    _type = type;
  }
  
  public Class getType()
  {
    return _type;
  }
  
  public Object readList(AbstractHessianInput in, int length)
    throws IOException
  {
    Collection list = createList();

    in.addRef(list);

    while (! in.isEnd())
      list.add(in.readObject());

    in.readEnd();

    return list;
  }
  
  public Object readLengthList(AbstractHessianInput in, int length)
    throws IOException
  {
    Collection list = createList();

    in.addRef(list);

    for (; length > 0; length--)
      list.add(in.readObject());

    return list;
  }

  private Collection createList()
    throws IOException
  {
    Collection list = null;
    
    if (_type == null)
      list = new ArrayList();
    else if (! _type.isInterface()) {
      try {
        list = (Collection) _type.newInstance();
      } catch (Exception e) {
      }
    }

    if (list != null) {
    }
    else if (SortedSet.class.isAssignableFrom(_type))
      list = new TreeSet();
    else if (Set.class.isAssignableFrom(_type))
      list = new HashSet();
    else if (List.class.isAssignableFrom(_type))
      list = new ArrayList();
    else if (Collection.class.isAssignableFrom(_type))
      list = new ArrayList();
    else {
      try {
        list = (Collection) _type.newInstance();
      } catch (Exception e) {
        throw new IOExceptionWrapper(e);
      }
    }

    return list;
  }
}


