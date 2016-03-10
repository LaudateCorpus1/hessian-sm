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

package com.caucho.hessian.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * FreeList provides a simple class to manage free objects.  This is useful
 * for large data structures that otherwise would gobble up huge GC time.
 *
 * <p>The free list is bounded.  Freeing an object when the list is full will
 * do nothing.
 */
public final class HessianFreeList<T> {
  private final AtomicReferenceArray<T> _freeStack;
  private final AtomicInteger _top = new AtomicInteger();

  /**
   * Create a new free list.
   *
   * @param initialSize maximum number of free objects to store.
   */
  public HessianFreeList(int size)
  {
    _freeStack = new AtomicReferenceArray(size);
  }
  
  /**
   * Try to get an object from the free list.  Returns null if the free list
   * is empty.
   *
   * @return the new object or null.
   */
  public T allocate()
  {
    int top = _top.get();

    if (top > 0 && _top.compareAndSet(top, top - 1))
      return _freeStack.getAndSet(top - 1, null);
    else
      return null;
  }
  
  /**
   * Frees the object.  If the free list is full, the object will be garbage
   * collected.
   *
   * @param obj the object to be freed.
   */
  public boolean free(T obj)
  {
    int top = _top.get();

    if (top < _freeStack.length()) {
      boolean isFree = _freeStack.compareAndSet(top, null, obj);
      
      _top.compareAndSet(top, top + 1);

      return isFree;
    }
    else
      return false;
  }

  public boolean allowFree(T obj)
  {
    return _top.get() < _freeStack.length();
  }

  /**
   * Frees the object.  If the free list is full, the object will be garbage
   * collected.
   *
   * @param obj the object to be freed.
   */
  public void freeCareful(T obj)
  {
    if (checkDuplicate(obj))
      throw new IllegalStateException("tried to free object twice: " + obj);

    free(obj);
  }

  /**
   * Debugging to see if the object has already been freed.
   */
  public boolean checkDuplicate(T obj)
  {
    int top = _top.get();

    for (int i = top - 1; i >= 0; i--) {
      if (_freeStack.get(i) == obj)
        return true;
    }

    return false;
  }
}
