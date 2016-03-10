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

import com.caucho.hessian.io.*;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: mhsieh
 * Date: 6/7/12
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class T4 implements Serializable {
    String s1;
    String t1;

    public T4(String s1, String t1) {
        this.s1 = s1;
        this.t1 = t1;
    }

//    @Override
//    public void writeExternal(Hessian2Output out) throws IOException {
//        out.writeString(s1);
//        out.writeString(t1);
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void readExternal(Hessian2Input in) throws IOException {
//        s1 = in.readString();
//        t1 = in.readString();
//        //To change body of implemented methods use File | Settings | File Templates.
//    }


}
