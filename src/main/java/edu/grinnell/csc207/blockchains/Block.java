package edu.grinnell.csc207.blockchains;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Blocks to be stored in blockchains.
 *
 * @author Benjamin Sheeley
 * @author Jake Bell
 * @author Samuel A. Rebelsky
 */
class Block {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  Hash prevHash;

  Hash currentHash;

  int index;

  Transaction blockData;

  long nonce;

  HashValidator checker;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new block from the specified block number, transaction, and
   * previous hash, mining to choose a nonce that meets the requirements
   * of the validator.
   *
   * @param num
   *   The number of the block.
   * @param transaction
   *   The transaction for the block.
   * @param prevHash
   *   The hash of the previous block.
   * @param check
   *   The validator used to check the block.
   */
  Block(int num, Transaction transaction, Hash prevHash, HashValidator check) {
    try {
      this.index = num;
      this.blockData = transaction;
      this.prevHash = prevHash;
      this.checker = check;
      computeHash();
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Algorithm not found (should never happen)");
    } //try/catch
  } // Block(int, Transaction, Hash, HashValidator)

  /**
   * Create a new block, computing the hash for the block.
   *
   * @param num
   *   The number of the block.
   * @param transaction
   *   The transaction for the block.
   * @param prevHash
   *   The hash of the previous block.
   * @param nonce
   *   The nonce of the block.
   */
  Block(int num, Transaction transaction, Hash prevHash, long nonce) {
    try {
      this.index = num;
      this.blockData = transaction;
      this.prevHash = prevHash;
      this.nonce = nonce;
      this.checker = null;
      computeHash();
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Algorithm not found (should never happen)");
    } //try/catch
  } // Block(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Compute the hash of the block given all the other info already
   * stored in the block.
   */
  void computeHash() throws NoSuchAlgorithmException {
    Random rand = new Random();
    MessageDigest md = MessageDigest.getInstance("sha-256");
    md.update(ByteBuffer.allocate(Integer.BYTES).putInt(this.index).array());
    md.update(this.blockData.getSource().getBytes());
    md.update(this.blockData.getTarget().getBytes());
    md.update(ByteBuffer.allocate(Integer.BYTES).putInt(this.blockData.getAmount()).array());
    md.update(this.prevHash.getBytes());
    md.update(ByteBuffer.allocate(Long.BYTES).putLong(this.nonce).array());
    currentHash = new Hash(md.digest());
    while (currentHash.toString().startsWith("000")) {
      this.nonce = rand.nextLong();
      md.update(ByteBuffer.allocate(Long.BYTES).putLong(this.nonce).array());
      currentHash = new Hash(md.digest());
    }
  } // computeHash()

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Get the number of the block.
   *
   * @return the number of the block.
   */
  int getNum() {
    return this.index;
  } // getNum()

  /**
   * Get the transaction stored in this block.
   *
   * @return the transaction.
   */
  Transaction getTransaction() {
    return this.blockData;
  } // getTransaction()

  /**
   * Get the nonce of this block.
   *
   * @return the nonce.
   */
  long getNonce() {
    return this.nonce;
  } // getNonce()

  /**
   * Get the hash of the previous block.
   *
   * @return the hash of the previous block.
   */
  Hash getPrevHash() {
    return this.prevHash;
  } // getPrevHash

  /**
   * Get the hash of the current block.
   *
   * @return the hash of the current block.
   */
  Hash getHash() {
    return this.currentHash;
  } // getHash

  /**
   * Get a string representation of the block.
   *
   * @return a string representation of the block.
   */
  public String toString() {
    return "Block " + this.index + 
    " (Transaction: [Source " + this.blockData.getSource() +
    ", Target " + this.blockData.getTarget() +
    ", Amount " + this.blockData.getAmount() +
    "], Nonce: " + this.nonce +
    ", prevHash: " + this.prevHash.toString() +
    ", hash: " + this.currentHash.toString() + ")";
  } // toString()
} // class Block
