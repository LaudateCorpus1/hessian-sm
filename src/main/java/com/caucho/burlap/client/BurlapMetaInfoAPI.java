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

package com.caucho.burlap.client;

/**
 * API retrieving burlap meta information.
 */
public interface BurlapMetaInfoAPI {
  /**
   * Returns a service attribute.
   *
   * <ul>
   * <li>java.api.class - the Java interface for the object interface.
   * <li>java.ejb.home.class - the EJB home interface
   * <li>java.ejb.remote.class - the EJB remote interface
   * <li>java.primary.key.class - the EJB primary key class
   * </ul>
   */
  public Object _burlap_getAttribute(String name);
}
