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
 * Exception for faults when the fault doesn't return a java exception.
 * This exception is required for MicroHessianInput.
 */
public class HessianServiceException extends Exception {
  private String code;
  private Object detail;

  /**
   * Zero-arg constructor.
   */
  public HessianServiceException()
  {
  }

  /**
   * Create the exception.
   */
  public HessianServiceException(String message, String code, Object detail)
  {
    super(message);
    this.code = code;
    this.detail = detail;
  }

  /**
   * Returns the code.
   */
  public String getCode()
  {
    return code;
  }

  /**
   * Returns the detail.
   */
  public Object getDetail()
  {
    return detail;
  }
}
