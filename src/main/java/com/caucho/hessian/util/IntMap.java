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

/**
 * The IntMap provides a simple hashmap from keys to integers.  The API is
 * an abbreviation of the HashMap collection API.
 *
 * <p>The convenience of IntMap is avoiding all the silly wrapping of
 * integers.
 */
public class IntMap {
  /**
   * Encoding of a null entry.  Since NULL is equal to Integer.MIN_VALUE,
   * it's impossible to distinguish between the two.
   */
  public final static int NULL = 0xdeadbeef; // Integer.MIN_VALUE + 1;

  private Object []_keys;
  private int []_values;

  private int _size;
  private int _prime;

  /**
   * Create a new IntMap.  Default size is 16.
   */
  public IntMap()
  {
    int capacity = 1024;

    _keys = new Object[capacity];
    _values = new int[capacity];

    _prime = getBiggestPrime(_keys.length);
    _size = 0;
  }

  /**
   * Clear the hashmap.
   */
  public void clear()
  {
    final Object []keys = _keys;
    final int []values = _values;

    for (int i = keys.length - 1; i >= 0; i--) {
      keys[i] = null;
      values[i] = 0;
    }

    _size = 0;
  }
  /**
   * Returns the current number of entries in the map.
   */
  public final int size()
  {
    return _size;
  }

  /**
   * Puts a new value in the property table with the appropriate flags
   */
  public final int get(Object key)
  {
    int prime = _prime;
    int hash = hashCode(key) % prime;
    // int hash = key.hashCode() & mask;

    final Object []keys = _keys;

    while (true) {
      Object mapKey = keys[hash];

      if (mapKey == null)
        return NULL;
      else if (mapKey == key)
        return _values[hash];

      hash = (hash + 1) % prime;
    }
  }

  /**
   * Puts a new value in the property table with the appropriate flags
   */
  public final int put(Object key, int value, boolean isReplace)
  {
    int prime = _prime;
    int hash = hashCode(key) % prime;
    // int hash = key.hashCode() % prime;

    Object []keys = _keys;

    while (true) {
      Object testKey = keys[hash];

      if (testKey == null) {
        keys[hash] = key;
        _values[hash] = value;

        _size++;

        if (keys.length <= 4 * _size)
          resize(4 * keys.length);

        return NULL;
      }
      else if (key != testKey) {
        hash = (hash + 1) % prime;

        continue;
      }
      else if (isReplace){
        int old = _values[hash];

        _values[hash] = value;

        return old;
      }
      else {
        return _values[hash];
      }
    }
  }

  /**
   * Expands the property table
   */
  private void resize(int newSize)
  {
    Object []keys = _keys;
    int values[] = _values;

    _keys = new Object[newSize];
    _values = new int[newSize];
    _size = 0;

    _prime = getBiggestPrime(_keys.length);

    for (int i = keys.length - 1; i >= 0; i--) {
      Object key = keys[i];

      if (key != null) {
        put(key, values[i], true);
      }
    }
  }

  protected int hashCode(Object value)
  {
    return value.hashCode();
  }

  public String toString()
  {
    StringBuffer sbuf = new StringBuffer();

    sbuf.append("IntMap[");
    boolean isFirst = true;

    for (int i = 0; i <= _keys.length; i++) {
      if (_keys[i] != null) {
        if (! isFirst)
          sbuf.append(", ");

        isFirst = false;
        sbuf.append(_keys[i]);
        sbuf.append(":");
        sbuf.append(_values[i]);
      }
    }
    sbuf.append("]");

    return sbuf.toString();
  }

  public static final int []PRIMES =
  {
   1,       /* 1<< 0 = 1 */
   2,       /* 1<< 1 = 2 */
   3,       /* 1<< 2 = 4 */
   7,       /* 1<< 3 = 8 */
   13,      /* 1<< 4 = 16 */
   31,      /* 1<< 5 = 32 */
   61,      /* 1<< 6 = 64 */
   127,     /* 1<< 7 = 128 */
   251,     /* 1<< 8 = 256 */
   509,     /* 1<< 9 = 512 */
   1021,    /* 1<<10 = 1024 */
   2039,    /* 1<<11 = 2048 */
   4093,    /* 1<<12 = 4096 */
   8191,    /* 1<<13 = 8192 */
   16381,   /* 1<<14 = 16384 */
   32749,   /* 1<<15 = 32768 */
   65521,   /* 1<<16 = 65536 */
   131071,  /* 1<<17 = 131072 */
   262139,  /* 1<<18 = 262144 */
   524287,  /* 1<<19 = 524288 */
   1048573, /* 1<<20 = 1048576 */
   2097143, /* 1<<21 = 2097152 */
   4194301, /* 1<<22 = 4194304 */
   8388593, /* 1<<23 = 8388608 */
   16777213, /* 1<<24 = 16777216 */
   33554393, /* 1<<25 = 33554432 */
   67108859, /* 1<<26 = 67108864 */
   134217689, /* 1<<27 = 134217728 */
   268435399, /* 1<<28 = 268435456 */
  };

  public static int getBiggestPrime(int value)
  {
    for (int i = PRIMES.length - 1; i >= 0; i--) {
      if (PRIMES[i] <= value)
        return PRIMES[i];
    }

    return 2;
  }
}
