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
 * Deserializing an object. Custom deserializers should extend
 * from AbstractDeserializer to avoid issues with signature
 * changes.
 */
public interface Deserializer {
  public Class<?> getType();

  public boolean isReadResolve();

  public Object readObject(AbstractHessianInput in)
    throws IOException;
  
  public Object readList(AbstractHessianInput in, int length)
    throws IOException;
  
  public Object readLengthList(AbstractHessianInput in, int length)
    throws IOException;
  
  public Object readMap(AbstractHessianInput in)
    throws IOException;
  
  /**
   * Creates an empty array for the deserializers field
   * entries.
   * 
   * @param len number of fields to be read
   * @return empty array of the proper field type.
   */
  public Object []createFields(int len);
  
  /**
   * Returns the deserializer's field reader for the given name.
   * 
   * @param name the field name
   * @return the deserializer's internal field reader
   */
  public Object createField(String name);
  
  /**
   * Reads the object from the input stream, given the field
   * definition.
   * 
   * @param in the input stream
   * @param fields the deserializer's own field marshal
   * @return the new object
   * @throws IOException
   */
  public Object readObject(AbstractHessianInput in,
                           Object []fields)
    throws IOException;
  
  public Object readObject(AbstractHessianInput in,
                           String []fieldNames)
    throws IOException;
}
