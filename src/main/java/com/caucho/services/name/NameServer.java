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

package com.caucho.services.name;

/**
 * A read-only name service.  The name service provides hierarchical
 * object lookup.  The path names are separated by '/'.
 *
 * <p>Because the name service is hierarchical, a lookup of an intermediate
 * node will return a NameServer instance.
 *
 * <p>The following example is a simple use of the NameServer:
 * <pre>
 * /dir-1/1 - where foo contains the string "foo-1"
 * /dir-1/2 - where foo contains the string "foo-2"
 * /dir-2/1 - where foo contains the string "foo-1"
 * /dir-2/2 - where foo contains the string "foo-2"
 * </pre>
 *
 * <p/>The root server might have a URL like:
 * <pre>
 * http://www.caucho.com/hessian/hessian/name?ejbid=/
 * </pre>
 *
 * <p/>So <code>root.lookup("/dir-1/1")</code> will return the string
 * "foo-1", and <code>root.lookup("/dir-1")</code> will return the
 * NameServer with the URL:
 * <pre>
 * http://www.caucho.com/hessian/hessian/name?ejbid=/dir-1
 * </pre>
 */
public interface NameServer {
  /**
   * Lookup an object from the name server.
   *
   * @param name the relative path name
   *
   * @return the matching object or null if no object maches
   *
   * @exception NameServiceException if there's an error
   */
  public Object lookup(String name)
    throws NameServiceException;

  /**
   * Lists all the object name components directly below the current context.
   * The names are the relative compent name.
   *
   * <p>For example, if the name server context is "/dir-1", the returned
   * values will be ["1", "2"].
   */
  public String []list()
    throws NameServiceException;
}
