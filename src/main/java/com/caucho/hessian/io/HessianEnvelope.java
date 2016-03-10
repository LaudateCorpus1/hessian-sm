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

import java.io.*;

/**
 * Factory class for wrapping and unwrapping hessian streams.
 */
abstract public class HessianEnvelope {
  /**
   * Wrap the Hessian output stream in an envelope.
   */
  abstract public Hessian2Output wrap(Hessian2Output out)
    throws IOException;

  /**
   * Unwrap the Hessian input stream with this envelope.  It is an
   * error if the actual envelope does not match the expected envelope
   * class.
   */
  abstract public Hessian2Input unwrap(Hessian2Input in)
    throws IOException;

  /**
   * Unwrap the envelope after having read the envelope code ('E') and
   * the envelope method.  Called by the EnvelopeFactory for dynamic
   * reading of the envelopes.
   */
  abstract public Hessian2Input unwrapHeaders(Hessian2Input in)
    throws IOException;
}
