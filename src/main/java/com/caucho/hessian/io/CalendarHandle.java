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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Handle for a calendar object.
 */
public class CalendarHandle implements java.io.Serializable, HessianHandle {
  private Class type;
  private Date date;

  public CalendarHandle()
  {
  }
  
  public CalendarHandle(Class type, long time)
  {
    if (! GregorianCalendar.class.equals(type))
      this.type = type;
    
    this.date = new Date(time);
  }

  private Object readResolve()
  {
    try {
      Calendar cal;
      
      if (this.type != null)
        cal = (Calendar) this.type.newInstance();
      else
        cal = new GregorianCalendar();
      
      cal.setTimeInMillis(this.date.getTime());

      return cal;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
