package edu.grinnell.csc207.blockchains;

import java.security.*;
import java.util.Random;
import java.nio.ByteBuffer;

/**
 * Blocks to be stored in blockchains.
 *
 * @author Benjamin Sheeley
 * @author Jake Bell
 * @author Samuel A. Rebelsky
 */
public class Block {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  private Hash prevHash;

  private Hash currentHash;

  private int index;

  private Transaction blockData;

  private long nonce;

  private HashValidator checker;
  
  private MessageDigest md;


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
    Random rand = new Random();
    try {
      this.md = MessageDigest.getInstance("sha-256");
      this.index = num;
      this.blockData = transaction;
      this.prevHash = prevHash;
      this.checker = check;
      computeHash();
      while (!this.checker.isValid(currentHash)) {
        this.nonce = rand.nextLong();
        md.update(ByteBuffer.allocate(Long.BYTES).putLong(this.nonce).array());
        this.currentHash = new Hash(md.digest());
      } //while
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
      this.md = MessageDigest.getInstance("sha-256");
      this.index = num;
      this.blockData = transaction;
      this.prevHash = prevHash;
      this.nonce = nonce;
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
    this.md.update(ByteBuffer.allocate(Integer.BYTES).putInt(this.index).array());
    this.md.update(this.blockData.getSource().getBytes());
    this.md.update(this.blockData.getTarget().getBytes());
    this.md.update(ByteBuffer.allocate(Integer.BYTES).putInt(this.blockData.getAmount()).array());
    this.md.update(this.prevHash.getBytes());
    this.md.update(ByteBuffer.allocate(Long.BYTES).putLong(this.nonce).array());
    this.currentHash = new Hash(md.digest());
  } // computeHash()

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Get the number of the block.
   *
   * @return the number of the block.
   */
  public int getNum() {
    return this.index;
  } // getNum()

  /**
   * Get the transaction stored in this block.
   *
   * @return the transaction.
   */
  public Transaction getTransaction() {
    return this.blockData;
  } // getTransaction()

  /**
   * Get the nonce of this block.
   *
   * @return the nonce.
   */
  public long getNonce() {
    return this.nonce;
  } // getNonce()

  /**
   * Get the hash of the previous block.
   *
   * @return the hash of the previous block.
   */
  public Hash getPrevHash() {
    return this.prevHash;
  } // getPrevHash

  /**
   * Get the hash of the current block.
   *
   * @return the hash of the current block.
   */
  public Hash getHash() {
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
