package edu.grinnell.csc207.blockchains;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

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

  /** The hash of the previous given block. */
  private Hash prevHash;

  /** The hash of the current block. */
  private Hash currentHash;

  /** The index of the block in the BlockChain. */
  private int index;

  /** The transaction in this given block. */
  private Transaction blockData;

  /**
   * The number that help give it a unique hash
   * that meets our validators standards.
  */
  private long nonce;

 /** Our hash must follow a certain pattern according to the given HashValidator. */
  private HashValidator checker;

  /** What will encode our hashes. */
  private MessageDigest md;

  /** The hash without nonce. */
  private byte[] baseHash;

  /** ByteBuffer for the integers. */
  static ByteBuffer intBuffer = ByteBuffer.allocate(Integer.BYTES);

  /** ByteBuffer for the longs. */
  static ByteBuffer longBuffer = ByteBuffer.allocate(Long.BYTES);



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
   * @param prevBlockHash
   *   The hash of the previous block.
   * @param check
   *   The validator used to check the block.
   */
  Block(int num, Transaction transaction, Hash prevBlockHash, HashValidator check) {
    Random rand = new Random();
    try {
      this.md = MessageDigest.getInstance("sha-256");
      this.index = num;
      this.blockData = transaction;
      this.prevHash = prevBlockHash;
      this.checker = check;
      computeBase();
      this.currentHash = new Hash(new byte[0]);
      while (!this.checker.isValid(currentHash)) {
        this.nonce = rand.nextLong();
        computeHash();
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
   * @param prevBlockHash
   *   The hash of the previous block.
   * @param blockNonce
   *   The nonce of the block.
   */
  public Block(int num, Transaction transaction, Hash prevBlockHash, long blockNonce) {
    try {
      this.md = MessageDigest.getInstance("sha-256");
      this.index = num;
      this.blockData = transaction;
      this.prevHash = prevBlockHash;
      this.nonce = blockNonce;
      computeBase();
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
    this.md.reset();
    this.md.update(this.baseHash);
    longBuffer.clear();
    this.md.update(longBuffer.putLong(this.nonce).array());
    this.currentHash = new Hash(md.digest());
  } // computeHash()

  /**
   * Compute the base hash to refrain from repeatedly
   * remaking it in computerHash().
   */
  void computeBase() {
    intBuffer.clear();
    this.md.update(intBuffer.putInt(this.index).array());
    this.md.update(this.blockData.getSource().getBytes());
    this.md.update(this.blockData.getTarget().getBytes());
    intBuffer.clear();
    this.md.update(intBuffer.putInt(this.blockData.getAmount()).array());
    this.md.update(this.prevHash.getBytes());
    this.baseHash = this.md.digest();
  } // computeBase()

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
    return "Block " + this.index
      + " (Transaction: [Source " + this.blockData.getSource()
      + ", Target " + this.blockData.getTarget()
      + ", Amount " + this.blockData.getAmount()
      + "], Nonce: " + this.nonce
      + ", prevHash: " + this.prevHash.toString()
      + ", hash: " + this.currentHash.toString() + ")";
  } // toString()
} // class Block
