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

package com.caucho.burlap.io;

import com.caucho.hessian.io.HessianProtocolException;

/**
 * Exception for faults when the fault doesn't return a java exception.
 * This exception is required for MicroBurlapInput.
 */
public class BurlapProtocolException extends HessianProtocolException {
  /**
   * Zero-arg constructor.
   */
  public BurlapProtocolException()
  {
  }
  
  /**
   * Create the exception.
   */
  public BurlapProtocolException(String message)
  {
    super(message);
  }
  
  /**
   * Create the exception.
   */
  public BurlapProtocolException(String message, Throwable rootCause)
  {
    super(message, rootCause);
  }
  
  /**
   * Create the exception.
   */
  public BurlapProtocolException(Throwable rootCause)
  {
    super(rootCause);
  }
}
