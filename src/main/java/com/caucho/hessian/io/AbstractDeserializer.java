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
 * Deserializing an object. 
 */
public class AbstractDeserializer implements Deserializer {
  public static final NullDeserializer NULL = new NullDeserializer();
  
  public Class<?> getType()
  {
    return Object.class;
  }

  public boolean isReadResolve()
  {
    return false;
  }

  public Object readObject(AbstractHessianInput in)
    throws IOException
  {
    Object obj = in.readObject();

    String className = getClass().getName();

    if (obj != null)
      throw error(className + ": unexpected object " + obj.getClass().getName() + " (" + obj + ")");
    else
      throw error(className + ": unexpected null value");
  }
  
  public Object readList(AbstractHessianInput in, int length)
    throws IOException
  {
    throw new UnsupportedOperationException(String.valueOf(this));
  }
  
  public Object readLengthList(AbstractHessianInput in, int length)
    throws IOException
  {
    throw new UnsupportedOperationException(String.valueOf(this));
  }
  
  public Object readMap(AbstractHessianInput in)
    throws IOException
  {
    Object obj = in.readObject();

    String className = getClass().getName();

    if (obj != null)
      throw error(className + ": unexpected object " + obj.getClass().getName() + " (" + obj + ")");
    else
      throw error(className + ": unexpected null value");
  }
  
  /**
   * Creates the field array for a class. The default
   * implementation returns a String[] array.
   *
   * @param len number of items in the array
   * @return the new empty array
   */
  public Object []createFields(int len)
  {
    return new String[len];
  }
  
  /**
   * Creates a field value class. The default
   * implementation returns the String.
   *
   * @param len number of items in the array
   * @return the new empty array
   */
  public Object createField(String name)
  {
    return name;
  }
  
  @Override
  public Object readObject(AbstractHessianInput in,
                           String []fieldNames)
    throws IOException
  {
    return readObject(in, (Object []) fieldNames);
  }
  
  /**
   * Reads an object instance from the input stream
   */
  public Object readObject(AbstractHessianInput in, 
                           Object []fields)
    throws IOException
  {
    throw new UnsupportedOperationException(toString());
  }

  protected HessianProtocolException error(String msg)
  {
    return new HessianProtocolException(msg);
  }

  protected String codeName(int ch)
  {
    if (ch < 0)
      return "end of file";
    else
      return "0x" + Integer.toHexString(ch & 0xff);
  }

  /**
   * The NullDeserializer exists as a marker for the factory classes so
   * they save a null result.
   */
  static final class NullDeserializer extends AbstractDeserializer {
  }
}
