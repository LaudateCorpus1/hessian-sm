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
import java.util.logging.*;

import com.caucho.hessian.HessianException;

/**
 * Serializing an object. 
 */
abstract public class AbstractSerializer implements Serializer {
  public static final NullSerializer NULL = new NullSerializer();
  
  protected static final Logger log
    = Logger.getLogger(AbstractSerializer.class.getName());
  
  @Override
  public void writeObject(Object obj, AbstractHessianOutput out)
    throws IOException
  {
    if (out.addRef(obj)) {
      return;
    }
    
    try {
      Object replace = writeReplace(obj);
      
      if (replace != null) {
        // out.removeRef(obj);

        out.writeObject(replace);

        out.replaceRef(replace, obj);

        return;
      }
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      // log.log(Level.FINE, e.toString(), e);
      throw new HessianException(e);
    }

    Class<?> cl = getClass(obj);

    int ref = out.writeObjectBegin(cl.getName());

    if (ref < -1) {
      writeObject10(obj, out);
    }
    else {
      if (ref == -1) {
        writeDefinition20(cl, out);

        out.writeObjectBegin(cl.getName());
      }

      writeInstance(obj, out);
    }
  }

  protected Object writeReplace(Object obj)
  {
    return null;
  }

  protected Class<?> getClass(Object obj)
  {
    return obj.getClass();
  }

  protected void writeObject10(Object obj,
                            AbstractHessianOutput out)
    throws IOException
  {
    throw new UnsupportedOperationException(getClass().getName());
  }

  protected void writeDefinition20(Class<?> cl,
                                   AbstractHessianOutput out)
    throws IOException
  {
    throw new UnsupportedOperationException(getClass().getName());
  }

  protected void writeInstance(Object obj,
                            AbstractHessianOutput out)
    throws IOException
  {
    throw new UnsupportedOperationException(getClass().getName());
  }

  /**
   * The NullSerializer exists as a marker for the factory classes so
   * they save a null result.
   */
  static final class NullSerializer extends AbstractSerializer {
    public void writeObject(Object obj, AbstractHessianOutput out)
      throws IOException
    {
      throw new IllegalStateException(getClass().getName());
    }
  }
}
