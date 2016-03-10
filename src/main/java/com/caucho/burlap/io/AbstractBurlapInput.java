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

import com.caucho.hessian.io.AbstractHessianInput;

/**
 * Abstract base class for Burlap requests.  Burlap users should only
 * need to use the methods in this class.
 *
 * <p>Note, this class is just an extension of AbstractHessianInput.
 *
 * <pre>
 * AbstractBurlapInput in = ...; // get input
 * String value;
 *
 * in.startReply();         // read reply header
 * value = in.readString(); // read string value
 * in.completeReply();      // read reply footer
 * </pre>
 */
abstract public class AbstractBurlapInput extends AbstractHessianInput {
}
