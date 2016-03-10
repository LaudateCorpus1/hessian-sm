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

import com.caucho.hessian.io.AbstractHessianOutput;
import java.io.IOException;

/**
 * Abstract output stream for Burlap requests.
 *
 * <pre>
 * OutputStream os = ...; // from http connection
 * AbstractOutput out = new BurlapSerializerOutput(os);
 * String value;
 *
 * out.startCall("hello");  // start hello call
 * out.writeString("arg1"); // write a string argument
 * out.completeCall();      // complete the call
 * </pre>
 */
abstract public class AbstractBurlapOutput extends AbstractHessianOutput {
  @Override
  public void startCall(String method, int length)
    throws IOException
  {
    startCall(method);
  }

  abstract void startCall(String method)
    throws IOException;
}
