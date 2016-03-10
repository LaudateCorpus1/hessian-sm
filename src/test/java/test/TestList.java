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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: mhsieh
 * Date: 6/7/12
 * Time: 9:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestList {
    private static Log logger = LogFactory.getLog(TestList.class);

    HessianSerializer hs;

    @BeforeTest
    public void init() {
        hs = new HessianSerializer();
    }

   public static void write2File(byte[] datas, String filename) {
        FileOutputStream fp = null;
        try {
            fp = new FileOutputStream( new File(filename));
                fp.write( datas);
       } catch (IOException e) {
            throw new RuntimeException( e.getMessage(), e);
        } finally {
            if ( fp != null ) {
                try {
                    fp.close();
                } catch (IOException e) {
                    //swallow exception
                     logger.error(e.getMessage());
                }
            }
        }

    }

    @Test(groups = {"test"})
    public void testList1() {
        int j = 2;
        List<T1> list =new ArrayList<T1>(j);
        for ( int i =0 ; i < j ; i++) {
            list.add( new T1( i, i, "test-"+i));
        }
        byte[] bs = hs.toBytes(list);
        write2File( bs, "./test1.log");
        List<T1> list1 = (List<T1>)hs.toObject(bs);
        assertEquals(list.size(), list1.size());
    }

    @Test(groups = {"test"})
    public void testList2() {
        int j = 2;
        List<T4> list =new ArrayList<T4>(j);
        for ( int i =0 ; i < j ; i++) {
            list.add( new T4( "pet-"+i, "test-"+i));
        }
        byte[] bs = hs.toBytes(list);
        write2File( bs, "./test2.log");
        List<T4> list1 = (List<T4>) hs.toObject(bs);
        assertEquals(list.size(), list1.size());
    }

}
