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

/**
 * Encapsulates a remote address when no stub is available, e.g. for
 * Java MicroEdition.
 */
public class HessianRemote implements java.io.Serializable {
  private String type;
  private String url;

  /**
   * Creates a new Hessian remote object.
   *
   * @param type the remote stub interface
   * @param url the remote url
   */
  public HessianRemote(String type, String url)
  {
    this.type = type;
    this.url = url;
  }

  /**
   * Creates an uninitialized Hessian remote.
   */
  public HessianRemote()
  {
  }

  /**
   * Returns the remote api class name.
   */
  public String getType()
  {
    return type;
  }

  /**
   * Returns the remote URL.
   */
  public String getURL()
  {
    return url;
  }

  /**
   * Sets the remote URL.
   */
  public void setURL(String url)
  {
    this.url = url;
  }

  /**
   * Defines the hashcode.
   */
  public int hashCode()
  {
    return url.hashCode();
  }

  /**
   * Defines equality
   */
  public boolean equals(Object obj)
  {
    if (! (obj instanceof HessianRemote))
      return false;

    HessianRemote remote = (HessianRemote) obj;

    return url.equals(remote.url);
  }

  /**
   * Readable version of the remote.
   */
  public String toString()
  {
    return "HessianRemote[" + url + "]";
  }
}
