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

import java.util.Locale;

/**
 * Handle for a locale object.
 */
public class LocaleHandle implements java.io.Serializable, HessianHandle {
  private String value;

  public LocaleHandle(String locale)
  {
    this.value = locale;
  }

  private Object readResolve()
  {
    String s = this.value;
    
    if (s == null)
      return null;
    
    int len = s.length();
    char ch = ' ';

    int i = 0;
    for (;
         i < len && ('a' <= (ch = s.charAt(i)) && ch <= 'z'
                     || 'A' <= ch && ch <= 'Z'
                     || '0' <= ch && ch <= '9');
         i++) {
    }

    String language = s.substring(0, i);
    String country = null;
    String var = null;

    if (ch == '-' || ch == '_') {
      int head = ++i;
      
      for (;
           i < len && ('a' <= (ch = s.charAt(i)) && ch <= 'z'
                       || 'A' <= ch && ch <= 'Z'
                       || '0' <= ch && ch <= '9');
           i++) {
      }
      
      country = s.substring(head, i);
    }

    if (ch == '-' || ch == '_') {
      int head = ++i;
      
      for (;
           i < len && ('a' <= (ch = s.charAt(i)) && ch <= 'z'
                       || 'A' <= ch && ch <= 'Z'
                       || '0' <= ch && ch <= '9');
           i++) {
      }
      
      var = s.substring(head, i);
    }

    if (var != null)
      return new Locale(language, country, var);
    else if (country != null)
      return new Locale(language, country);
    else
      return new Locale(language);
  }
}
