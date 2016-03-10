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

package com.caucho.services.message;

import java.io.IOException;

/**
 * Exception for faults when the fault doesn't return a java exception.
 * This exception is required for MicroHessianInput.
 */
public class MessageServiceException extends IOException {
  private Throwable _rootCause;
  
  /**
   * Zero-arg constructor.
   */
  public MessageServiceException()
  {
  }
  
  /**
   * Create the exception.
   */
  public MessageServiceException(String message)
  {
    super(message);
  }
  
  /**
   * Create the exception.
   */
  public MessageServiceException(String message, Throwable rootCause)
  {
    super(message);

    _rootCause = rootCause;
  }
  
  /**
   * Create the exception.
   */
  public MessageServiceException(Throwable rootCause)
  {
    super(String.valueOf(rootCause));

    _rootCause = rootCause;
  }

  /**
   * Returns the underlying cause.
   */
  public Throwable getRootCause()
  {
    return getCause();
  }

  /**
   * Returns the underlying cause.
   */
  public Throwable getCause()
  {
    return _rootCause;
  }
}
