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

package com.caucho.hessian.client;

import com.caucho.hessian.HessianException;

/**
 * Exception caused by failure of the client proxy to connect to the server.
 */
public class HessianConnectionException extends HessianException {
  /**
   * Zero-arg constructor.
   */
  public HessianConnectionException()
  {
  }

  /**
   * Create the exception.
   */
  public HessianConnectionException(String message)
  {
    super(message);
  }

  /**
   * Create the exception.
   */
  public HessianConnectionException(String message, Throwable rootCause)
  {
    super(message, rootCause);
  }

  /**
   * Create the exception.
   */
  public HessianConnectionException(Throwable rootCause)
  {
    super(rootCause);
  }
}
