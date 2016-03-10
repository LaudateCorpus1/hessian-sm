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

import com.caucho.hessian.io.External;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: mhsieh
 * Date: 5/6/11
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class T1  implements Serializable, External {
    int i1;
    double d1;
    String s1;

    public T1() {
        super();
    }
    public T1(int i1, double d1, String s1) {
        this.i1 = i1;
        this.d1 = d1;
        this.s1 = s1;
    }


    public int getI1() {
        return i1;
    }

    public void setI1(int i1) {
        this.i1 = i1;
    }

    public double getD1() {
        return d1;
    }

    public void setD1(double d1) {
        this.d1 = d1;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    @Override
    public String toString() {
        return "T1{" +
                "i1=" + i1 +
                ", d1=" + d1 +
                ", s1='" + s1 + '\'' +
                '}';
    }

    @Override
    public void writeExternal(Hessian2Output out) throws IOException {
        out.writeInt(i1);
        out.writeDouble(d1);
        out.writeString(s1);
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void readExternal(Hessian2Input in) throws IOException {
        i1 = in.readInt();
        d1 = in.readDouble();
        s1 = in.readString();
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
