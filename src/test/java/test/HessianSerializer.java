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

package test;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: mhsieh
 * Date: 6/7/12
 * Time: 9:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class HessianSerializer<T>  {

    public byte[] toBytes(Object object) {
  	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			Hessian2Output oos = new Hessian2Output(baos);
			oos.writeObject(object);
			oos.flushBuffer();
		}
		catch (java.io.IOException ioe) {
			throw new RuntimeException(ioe.getMessage(), ioe);
		}
		return baos.toByteArray();
    }

    public T toObject(byte[] bytes) {
        return toObject(bytes, 0, bytes.length);
    }

    public T toObject(byte[] bytes, int offset, int length) {
        T object = null;
        try {
            object = (T) new Hessian2Input(new ByteArrayInputStream(bytes, offset, length)).readObject();
            return object;
        }
        catch (java.io.IOException ioe) {
            throw new RuntimeException(ioe.getMessage(), ioe);
        }
    }
}

