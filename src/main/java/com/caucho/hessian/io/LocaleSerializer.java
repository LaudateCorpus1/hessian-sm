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
import java.util.Locale;

/**
 * Serializing a locale.
 */
public class LocaleSerializer extends AbstractSerializer {
  private static LocaleSerializer SERIALIZER = new LocaleSerializer();

  public static LocaleSerializer create()
  {
    return SERIALIZER;
  }
  
  public void writeObject(Object obj, AbstractHessianOutput out)
    throws IOException
  {
    if (obj == null)
      out.writeNull();
    else {
      Locale locale = (Locale) obj;

      out.writeObject(new LocaleHandle(locale.toString()));
    }
  }
}
