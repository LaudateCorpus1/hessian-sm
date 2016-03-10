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

/**
 * Wrapper for protocol exceptions thrown in the proxy.
 */
public class HessianRuntimeException extends RuntimeException {
  private Throwable rootCause;

  /**
   * Zero-arg constructor.
   */
  public HessianRuntimeException()
  {
  }

  /**
   * Create the exception.
   */
  public HessianRuntimeException(String message)
  {
    super(message);
  }

  /**
   * Create the exception.
   */
  public HessianRuntimeException(String message, Throwable rootCause)
  {
    super(message);

    this.rootCause = rootCause;
  }

  /**
   * Create the exception.
   */
  public HessianRuntimeException(Throwable rootCause)
  {
    super(String.valueOf(rootCause));

    this.rootCause = rootCause;
  }

  /**
   * Returns the underlying cause.
   */
  public Throwable getRootCause()
  {
    return this.rootCause;
  }

  /**
   * Returns the underlying cause.
   */
  public Throwable getCause()
  {
    return getRootCause();
  }
}
