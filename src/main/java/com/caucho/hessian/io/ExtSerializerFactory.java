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

import java.util.HashMap;

/**
 * Factory for returning serialization methods.
 */
public class ExtSerializerFactory extends AbstractSerializerFactory {
  private HashMap _serializerMap = new HashMap();
  private HashMap _deserializerMap = new HashMap();

  /**
   * Adds a serializer.
   *
   * @param cl the class of the serializer
   * @param serializer the serializer
   */
  public void addSerializer(Class cl, Serializer serializer)
  {
    _serializerMap.put(cl, serializer);
  }

  /**
   * Adds a deserializer.
   *
   * @param cl the class of the deserializer
   * @param deserializer the deserializer
   */
  public void addDeserializer(Class cl, Deserializer deserializer)
  {
    _deserializerMap.put(cl, deserializer);
  }
  
  /**
   * Returns the serializer for a class.
   *
   * @param cl the class of the object that needs to be serialized.
   *
   * @return a serializer object for the serialization.
   */
  public Serializer getSerializer(Class cl)
    throws HessianProtocolException
  {
    return (Serializer) _serializerMap.get(cl);
  }
  
  /**
   * Returns the deserializer for a class.
   *
   * @param cl the class of the object that needs to be deserialized.
   *
   * @return a deserializer object for the serialization.
   */
  public Deserializer getDeserializer(Class cl)
    throws HessianProtocolException
  {
    return (Deserializer) _deserializerMap.get(cl);
  }
}
