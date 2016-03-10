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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: mhsieh
 * Date: 5/6/11
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class RunTest {
    private static Log logger = LogFactory.getLog(RunTest.class);

    public static Object readObject(String fileName) {
        long begin = System.nanoTime();
        BufferedInputStream fis = null ;
        try {
            Object object;
            fis = new BufferedInputStream (new FileInputStream(fileName));
            Hessian2Input ois =  new Hessian2Input( fis);
            ois.setCloseStreamOnClose(true);
            object = ois.readObject();
            ois.close();
            long end = System.nanoTime();
            logger.info("Time for deserize java object file "+fileName+"  duration micro seconds="+
                    TimeUnit.MICROSECONDS.convert((end - begin), TimeUnit.NANOSECONDS ) );
            return object;
        } catch ( Exception ex) {
            if ( fis != null ) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static void writeObject(Object object, String fileName) {
        long begin = System.nanoTime();
        Hessian2Output oos =  null ;
        try {
            oos = new Hessian2Output(  new FileOutputStream( fileName) );
            oos.setCloseStreamOnClose(true);
            oos.writeObject(object);
            oos.close();
            long end = System.nanoTime();
            logger.info("Time for Serize java object file "+fileName+"  duration micro seconds="+
                    TimeUnit.MICROSECONDS.convert((end - begin), TimeUnit.NANOSECONDS ) );
        } catch ( Exception ex) {
            if ( oos != null ) {
                try {
                    oos.close();
                } catch (IOException iox) {
                    logger.error(ex.getMessage(), iox);
                }
            }

            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }

    }

    public static Object toObject(byte[] bytes, int offset, int length) {
        Object object = null;
        try {
            object = new Hessian2Input(new ByteArrayInputStream(bytes, offset, length)).readObject();
        }
        catch (java.io.IOException ioe) {
            throw new RuntimeException(ioe.getMessage(), ioe);
        }
        return object;

    }

    public static Object toObject(byte[] bytes) {
        return toObject(bytes, 0, bytes.length);
    }


    public static byte[] toBytes(Object object) {
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


    public static void testFile(String file) {
        try {
            Object obj = readObject(file);
            logger.info("Read file " + file +" "+ obj.toString());
            writeObject(obj, "tmp");
        } catch (Exception ex) {
            logger.info("file " + file +" "+ex.getMessage(), ex );
        }
    }

    public static void testAll(String path) {
        File dir = new File(path);
        if ( ! dir.isDirectory()) logger.info(path+" is not a directory");
        else {
            String[] files = dir.list();
            for ( String file : files) {
                try {
                    Object obj = readObject(path+ file);
                    logger.info("Read file " + file +" "+ obj.toString());
                } catch (Exception ex) {
                    logger.info("file " + file+" "+ex.getMessage(), ex );
                }
            }
        }

    }

    public static void testAllRW(String source, String dest) {
        File dir = new File(source);
        if ( ! dir.isDirectory()) logger.info(source+" is not a directory");
        else {
            String[] files = dir.list();
            for ( String file : files) {
                try {
                    Object obj = readObject(source+ file);
                    logger.info("Read file " + file +" "+ obj.toString());
                    writeObject(obj, dest+file);
                    logger.info("Write file " + file +" "+ obj.toString());
                } catch (Exception ex) {
                    logger.info("file " + file+" "+ex.getMessage(), ex );
                }
            }
        }

    }

    public static class RR implements External {
        public RR() {
            super();
        }
        public void writeExternal(Hessian2Output out) throws IOException {
        }
        public void readExternal(Hessian2Input in) throws IOException {
        }


    }

    public static void testMap() {
        HashMap map = new HashMap();
        for ( int i =0 ; i < 10 ; i ++)
            map.put(i, "test-"+i);
        byte[] datas = toBytes( map);
        Object obj = toObject( datas);
        logger.info("object "+ obj.toString() );
        writeObject(map, "tmp");
    }

    public static void testList() {
        List list = new ArrayList(10);
        for ( int i =0 ; i < 10 ; i ++)
            list.add(new T1(i, i, "test-"+i));
        byte[] datas = toBytes( list);
        Object obj = toObject( datas);
        logger.info("object "+ obj.toString() );
    }

    public void TestT1() {
        List list = new ArrayList(10);
        for (int i =0 ; i < 10 ; i ++) {
            list.add( new T1(i, i, "test-"+1));
        }
        byte[] datas = toBytes( list);
        Object obj = toObject( datas);
        logger.info("object "+ obj.toString() );
    }

    public static void testT2() {
        T2 t2 = new T2( new T1(4, 6, "test-"+1), 3, 10.0);
        byte[] datas = toBytes( t2);
        Object obj = toObject( datas);
        logger.info("object "+ obj.toString() );
    }

    public static void main(String[] args) {

        testMap();
        testList();
        testT2();

        RR r = new RR();
        Class[] cls = r.getClass().getInterfaces();
        for ( Class each : cls) {
            logger.info("interface "+cls.toString());
            if (each.getName().equals("com.caucho.hessian.io.External") )
                logger.info("Match "+ each.getName() );
        }
//        String path = "/Test/datas/";
//        if ( args.length > 0 ) path = args[0];
//        testFile(path+"adtypes.9.13");
//        testAll("/Test/testdir/");
//        testAllRW(path,"/Test/testdir/");

    }
}
