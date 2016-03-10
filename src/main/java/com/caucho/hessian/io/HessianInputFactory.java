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

import java.util.logging.*;
import java.io.*;

public class HessianInputFactory
{
  public static final Logger log
    = Logger.getLogger(HessianInputFactory.class.getName());

  private HessianFactory _factory = new HessianFactory();

  public void setSerializerFactory(SerializerFactory factory)
  {
    _factory.setSerializerFactory(factory);
  }

  public SerializerFactory getSerializerFactory()
  {
    return _factory.getSerializerFactory();
  }

  public HeaderType readHeader(InputStream is)
    throws IOException
  {
    int code = is.read();

    int major = is.read();
    int minor = is.read();

    switch (code) {
    case -1:
      throw new IOException("Unexpected end of file for Hessian message");
      
    case 'c':
      if (major >= 2)
        return HeaderType.CALL_1_REPLY_2;
      else
        return HeaderType.CALL_1_REPLY_1;
    case 'r':
      return HeaderType.REPLY_1;
      
    case 'H':
      return HeaderType.HESSIAN_2;

    default:
      throw new IOException((char) code + " 0x" + Integer.toHexString(code) + " is an unknown Hessian message code.");
    }
  }

  public AbstractHessianInput open(InputStream is)
    throws IOException
  {
    int code = is.read();

    int major = is.read();
    int minor = is.read();

    switch (code) {
    case 'c':
    case 'C':
    case 'r':
    case 'R':
      if (major >= 2) {
        return _factory.createHessian2Input(is);
      }
      else {
        return _factory.createHessianInput(is);
      }

    default:
      throw new IOException((char) code + " is an unknown Hessian message code.");
    }
  }

  public enum HeaderType {
    CALL_1_REPLY_1,
      CALL_1_REPLY_2,
      HESSIAN_2,
      REPLY_1,
      REPLY_2;

    public boolean isCall1()
    {
      switch (this) {
      case CALL_1_REPLY_1:
      case CALL_1_REPLY_2:
        return true;
      default:
        return false;
      }
    }

    public boolean isCall2()
    {
      switch (this) {
      case HESSIAN_2:
        return true;
      default:
        return false;
      }
    }

    public boolean isReply1()
    {
      switch (this) {
      case CALL_1_REPLY_1:
        return true;
      default:
        return false;
      }
    }

    public boolean isReply2()
    {
      switch (this) {
      case CALL_1_REPLY_2:
      case HESSIAN_2:
        return true;
      default:
        return false;
      }
    }
  }
}
