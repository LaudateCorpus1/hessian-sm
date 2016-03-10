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

import com.caucho.hessian.HessianException;

/**
 * Exception for faults when the fault doesn't return a java exception.
 * This exception is required for MicroHessianInput.
 */
public class HessianMethodSerializationException extends HessianException {
  /**
   * Zero-arg constructor.
   */
  public HessianMethodSerializationException()
  {
  }
  
  /**
   * Create the exception.
   */
  public HessianMethodSerializationException(String message)
  {
    super(message);
  }
  
  /**
   * Create the exception.
   */
  public HessianMethodSerializationException(String message, Throwable cause)
  {
    super(message, cause);
  }
  
  /**
   * Create the exception.
   */
  public HessianMethodSerializationException(Throwable cause)
  {
    super(cause);
  }
}
