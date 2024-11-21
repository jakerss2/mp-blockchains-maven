package edu.grinnell.csc207.util;

import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Your Name Here
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V>[]pairs;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({ "unchecked" })
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(),
        DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> newArr = new AssociativeArray<K, V>();

    newArr.size = this.size();

    while (newArr.size > newArr.pairs.length) {
      newArr.expand();
    } // while

    for (int i = 0; i < this.size; i++) {
      newArr.pairs[i] = this.pairs[i].clone();
    } // for

    return newArr;
  } // clone()

  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    String arrString = "{";
    for (int i = 0; i < this.size - 1; i++) {
      try {
        arrString += this.pairs[i].toString() + ", ";
      } catch (NullPointerException e) {
        arrString += this.pairs[i].key.toString() + ": Null" + ", ";
      } // try/catch
    } // for
    try {
      arrString += this.pairs[this.size - 1].toString() + "}";
    } catch (NullPointerException e) {
      arrString += this.pairs[this.size - 1].key.toString() + ": Null" + "}";
    } // try/catch
    return arrString;
  } // toString()

  /**
   * Get all of the keys in the AssociativeArray.
   *
   * @return all of the keys
   */
  public String[] getAllKeys() {
    String[] arrString = new String[this.size];
    for (int i = 0; i < this.size; i++) {
      try {
        arrString[i] = (String) this.pairs[i].key;
      } catch (Exception e) {
        System.err.print("Somehow added a null");
      } // try/catch
    } //for
    return arrString;
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to
   * get(key) will return value.
   *
   * @param key
   *   The key whose value we are seeting.
   * @param value
   *   The value of that key.
   *
   * @throws NullKeyException
   *   If the client provides a null key.
   */
  public void set(K key, V value) throws NullKeyException {
    if (key == null) {
      throw new NullKeyException();
    } // if
    try {
      int index = this.find(key);
      this.pairs[index].val = value;
    } catch (KeyNotFoundException e) {
      if (size + 1 > this.pairs.length) {
        this.expand();
      } // if
      this.pairs[size] = new KVPair<>(key, value);
      size++;
    } // try/catch
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key
   *   A key
   *
   * @throws KeyNotFoundException
   *   when the key is null or does not appear in the associative array.
   *
   * @return the value V associated with K key
   */
  public V get(K key) throws KeyNotFoundException {
    if (key == null) {
      throw new KeyNotFoundException("Key not found");
    } // if
    try {
      int index = this.find(key);
      return this.pairs[index].val;
    } catch (KeyNotFoundException e) {
      throw new KeyNotFoundException("Key not found");
    } // try/catch
  } // get(K)

  /**
   * Determine if key appears in the associative array. Should
   * return false for the null key.
   *
   * @param key
   *
   * @return true if found, false if null or not found
   */
  public boolean hasKey(K key) {
    if (key == null) {
      return false;
    } // if
    try {
      this.find(key);
      return true;
    } catch (Exception e) {
      return false;
    } // try/catch
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls
   * to get(key) will throw an exception. If the key does not appear
   * in the associative array, does nothing.
   *
   * @param key
   */
  public void remove(K key) {
    try {
      int foundIndex = this.find(key);
      this.pairs[foundIndex] = this.pairs[size - 1];
      this.pairs[size - 1] = new KVPair<>();
      size--;
    } catch (Exception e) {
      // do nothing
    } // try/catch
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   *
   * @return the size of how many pairs are in the array.
   */
  public int size() {
    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key.
   * If no such entry is found, throws an exception.
   *
   * @param key
   *   The key of the entry.
   *
   * @throws KeyNotFoundException
   *   If the key does not appear in the associative array.
   *
   * @return the index of the key in the array
   */
  int find(K key) throws KeyNotFoundException {

    try {
      for (int i = 0; i < this.size; i++) {
        if (this.pairs[i].key.equals(key)) {
          return i;
        } // if
      }   // for
    } catch (Exception e) {
      throw new KeyNotFoundException("Key not found in pairs");
    } // try/catch
    throw new KeyNotFoundException("Key not found in pairs");
  } // find(K)

} // class AssociativeArray
