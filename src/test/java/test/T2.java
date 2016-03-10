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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mhsieh
 * Date: 5/9/11
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class T2 implements Serializable {
    private T1 t1;
    private Map maps;
    private List<T1> list;
    private int i1;
    private double d1;

    public T2(T1 t1, int i1, double d1) {
        this.t1 = t1;
        this.i1 = i1;
        this.d1 = d1;
        init();
    }

    private void init() {
        maps = new HashMap();
        for ( int i =0 ; i < 100 ; i ++)
            maps.put(i, "test-"+i);
        list = new ArrayList(100);
        for (int i =0 ; i < 100 ; i ++) {
            list.add( new T1(i, i, "list-"+i));
        }


    }

    @Override
    public String toString() {
        return "T2{" +
                "t1=" + t1 +
                ", maps=" + maps +
                ", list=" + list +
                ", i1=" + i1 +
                ", d1=" + d1 +
                '}';
    }
}

