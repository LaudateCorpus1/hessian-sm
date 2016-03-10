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
import com.caucho.hessian.util.HessianFreeList;

/**
 * Factory for creating HessianInput and HessianOutput streams.
 */
public class HessianFactory
{
  public static final Logger log
    = Logger.getLogger(HessianFactory.class.getName());

  private SerializerFactory _serializerFactory;
  private SerializerFactory _defaultSerializerFactory;

  private final HessianFreeList<Hessian2Output> _freeHessian2Output
    = new HessianFreeList<Hessian2Output>(32);

  private final HessianFreeList<HessianOutput> _freeHessianOutput
    = new HessianFreeList<HessianOutput>(32);

  private final HessianFreeList<Hessian2Input> _freeHessian2Input
    = new HessianFreeList<Hessian2Input>(32);

  private final HessianFreeList<HessianInput> _freeHessianInput
    = new HessianFreeList<HessianInput>(32);

  public HessianFactory()
  {
    _defaultSerializerFactory = SerializerFactory.createDefault();
    _serializerFactory = _defaultSerializerFactory;
  }

  public void setSerializerFactory(SerializerFactory factory)
  {
    _serializerFactory = factory;
  }

  public SerializerFactory getSerializerFactory()
  {
    // the default serializer factory cannot be modified by external
    // callers
    if (_serializerFactory == _defaultSerializerFactory) {
      _serializerFactory = new SerializerFactory();
    }

    return _serializerFactory;
  }

  /**
   * Creates a new Hessian 2.0 deserializer.
   */
  public Hessian2Input createHessian2Input(InputStream is)
  {
    Hessian2Input in = _freeHessian2Input.allocate();
    
    if (in == null) {
      in = new Hessian2Input(is);
      in.setSerializerFactory(getSerializerFactory());
    }
    else {
      in.init(is);
    }

    return in;
  }

  /**
   * Frees a Hessian 2.0 deserializer
   */
  public void freeHessian2Input(Hessian2Input in)
  {
    if (in == null)
      return;

    in.free();

    _freeHessian2Input.free(in);
  }

  /**
   * Creates a new Hessian 2.0 deserializer.
   */
  public Hessian2StreamingInput createHessian2StreamingInput(InputStream is)
  {
    Hessian2StreamingInput in = new Hessian2StreamingInput(is);
    in.setSerializerFactory(getSerializerFactory());

    return in;
  }

  /**
   * Frees a Hessian 2.0 deserializer
   */
  public void freeHessian2StreamingInput(Hessian2StreamingInput in)
  {
  }

  /**
   * Creates a new Hessian 1.0 deserializer.
   */
  public HessianInput createHessianInput(InputStream is)
  {
    return new HessianInput(is);
  }

  /**
   * Creates a new Hessian 2.0 serializer.
   */
  public Hessian2Output createHessian2Output(OutputStream os)
  {
    Hessian2Output out = createHessian2Output();
    
    out.init(os);
    
    return out;
  }

  /**
   * Creates a new Hessian 2.0 serializer.
   */
  public Hessian2Output createHessian2Output()
  {
    Hessian2Output out = _freeHessian2Output.allocate();

    if (out == null) {
      out = new Hessian2Output();

      out.setSerializerFactory(getSerializerFactory());
    }

    return out;
  }

  /**
   * Frees a Hessian 2.0 serializer
   */
  public void freeHessian2Output(Hessian2Output out)
  {
    if (out == null)
      return;

    out.free();

    _freeHessian2Output.free(out);
  }

  /**
   * Creates a new Hessian 2.0 serializer.
   */
  public Hessian2StreamingOutput createHessian2StreamingOutput(OutputStream os)
  {
    Hessian2Output out = createHessian2Output(os);

    return new Hessian2StreamingOutput(out);
  }

  /**
   * Frees a Hessian 2.0 serializer
   */
  public void freeHessian2StreamingOutput(Hessian2StreamingOutput out)
  {
    if (out == null)
      return;

    freeHessian2Output(out.getHessian2Output());
  }

  /**
   * Creates a new Hessian 1.0 serializer.
   */
  public HessianOutput createHessianOutput(OutputStream os)
  {
    return new HessianOutput(os);
  }

  public OutputStream createHessian2DebugOutput(OutputStream os,
                                                Logger log,
                                                Level level)
  {
    HessianDebugOutputStream out
      = new HessianDebugOutputStream(os, log, level);

    out.startTop2();

    return out;
  }
}
