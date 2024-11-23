package edu.grinnell.csc207.blockchains;

import java.util.Arrays;

/**
 * Encapsulated hashes.
 *
 * @author Jacob Bell
 * @author Ben Sheeley
 * @author Samuel A. Rebelsky
 */
public class Hash {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The hash stored.
   */
  byte[] hashData;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new encapsulated hash.
   *
   * @param data
   *   The data to copy into the hash.
   */
  public Hash(byte[] data) {
    byte[] dataCopy = new byte[data.length];
    System.arraycopy(data, 0, dataCopy, 0, data.length);
    this.hashData = dataCopy;
  } // Hash(byte[])

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Determine how many bytes are in the hash.
   *
   * @return the number of bytes in the hash.
   */
  public int length() {
    return this.hashData.length;
  } // length()

  /**
   * Get the ith byte.
   *
   * @param i
   *   The index of the byte to get, between 0 (inclusive) and
   *   length() (exclusive).
   *
   * @return the ith byte
   */
  public byte get(int i) {
    return this.hashData[i];
  } // get()

  /**
   * Get a copy of the bytes in the hash. We make a copy so that the client
   * cannot change them.
   *
   * @return a copy of the bytes in the hash.
   */
  public byte[] getBytes() {
    byte[] arrCpy = new byte[this.length()];
    System.arraycopy(this.hashData, 0, arrCpy, 0, this.length());
    return arrCpy;
  } // getBytes()

  /**
   * Convert to a hex string.
   *
   * @return the hash as a hex string.
   */
  public String toString() {
    String str = "";
    for (byte byt : this.hashData) {
      str += String.format("%02X", Byte.toUnsignedInt(byt));
    } //for
    return str;
  } // toString()

  /**
   * Determine if this is equal to another object.
   *
   * @param other
   *   The object to compare to.
   *
   * @return true if the two objects are conceptually equal and false
   *   otherwise.
   */
  public boolean equals(Object other) {
    if (other instanceof Hash) {
      Hash o = (Hash) other;
      return Arrays.equals(this.hashData, o.hashData);
    } //if
    return false;
  } // equals(Object)

  /**
   * Get the hash code of this object.
   *
   * @return the hash code.
   */
  public int hashCode() {
    return this.toString().hashCode();
  } // hashCode()
} // class Hash
