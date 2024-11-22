package edu.grinnell.csc207.blockchains;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A full blockchain.
 *
 * @author Benjamin Sheeley
 * @author Jake Bell
 */
public class BlockChain implements Iterable<Transaction> {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+
  public Node firstNode;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new blockchain using a validator to check elements.
   *
   * @param check
   *   The validator used to check elements.
   */
  public BlockChain(HashValidator check) {
    // STUB
  } // BlockChain(HashValidator)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /** The element at the front of the BlockChain */
  private Node head;

  /** The element at the back of the BlockChain */
  private Node tail;

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Mine for a new valid block for the end of the chain, returning that
   * block.
   *
   * @param t
   *   The transaction that goes in the block.
   *
   * @return a new block with correct number, hashes, and such.
   */
  public Block mine(Transaction t) {
    return new Block(10, t, new Hash(new byte[] {7}), 11);       // STUB
  } // mine(Transaction)

  /**
   * Get the number of blocks curently in the chain.
   *
   * @return the number of blocks in the chain, including the initial block.
   */
  public int getSize() {
    return 2;   // STUB
  } // getSize()

  /**
   * Add a block to the end of the chain.
   *
   * @param blk
   *   The block to add to the end of the chain.
   *
   * @throws IllegalArgumentException if (a) the hash is not valid, (b)
   *   the hash is not appropriate for the contents, or (c) the previous
   *   hash is incorrect.
   */
  public void append(Block blk) {
    // STUB
  } // append()

  /**
   * Attempt to remove the last block from the chain.
   *
   * @return false if the chain has only one block (in which case it's
   *   not removed) or true otherwise (in which case the last block
   *   is removed).
   */
  public boolean removeLast() {
    return true;        // STUB
  } // removeLast()

  /**
   * Get the hash of the last block in the chain.
   *
   * @return the hash of the last sblock in the chain.
   */
  public Hash getHash() {
    return new Hash(new byte[] {2, 0, 7});   // STUB
  } // getHash()

  /**
   * Determine if the blockchain is correct in that (a) the balances are
   * legal/correct at every step, (b) that every block has a correct
   * previous hash field, (c) that every block has a hash that is correct
   * for its contents, and (d) that every block has a valid hash.
   *
   * @return true if the blockchain is correct and false otherwise.
   */
  public boolean isCorrect() {
    return true;        // STUB
  } // isCorrect()

  /**
   * Determine if the blockchain is correct in that (a) the balances are
   * legal/correct at every step, (b) that every block has a correct
   * previous hash field, (c) that every block has a hash that is correct
   * for its contents, and (d) that every block has a valid hash.
   *
   * @throws Exception
   *   If things are wrong at any block.
   */
  public void check() throws Exception {
    Iterator<Block> blockIter = blocks();
    while (blockIter.hasNext()) {
      
    } //while
  } // check()

  /**
   * Return an iterator of all the people who participated in the
   * system.
   *
   * @return an iterator of all the people in the system.
   */
  public Iterator<String> users() {
    return new Iterator<String>() {
      /** Keep track of the current node of the iterator */
      private Node current = head;

      /** Keep track if we are at the source or target */
      private boolean shownSource = false;

      public boolean hasNext() {
        return (current.nextNode != null);
      } // hasNext()

      public String next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        } //if
        if (shownSource) {
          String name = current.getData().getTransaction().getSource();
          current = current.nextNode;
          shownSource = true;
          return name;
        } //if
        String name = current.getData().getTransaction().getTarget();
        current = current.nextNode;
        shownSource = false;
        return name;
      } // next()
    };
  } // users()

  /**
   * Find one user's balance.
   *
   * @param user
   *   The user whose balance we want to find.
   *
   * @return that user's balance (or 0, if the user is not in the system).
   */
  public int balance(String user) {
    int userBalance = 0;
    for (Transaction obj : this) {
      if (obj.getSource().equals(user)) {
        userBalance += obj.getAmount();
      } else if (obj.getSource().equals(user)) {
        userBalance -= obj.getAmount();
      } //if
    } //for
    return userBalance;
  } // balance()

  /**
   * Get an interator for all the blocks in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Block> blocks() {
    return new Iterator<Block>() {
      /** Keep track of the current node for iterator */
      private Node current = head;

      public boolean hasNext() {
        return (current.nextNode != null);
      } // hasNext()

      public Block next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        } // if
        Block data = current.getData();
        current = current.nextNode;
        return data;
      } // next()
    };
  } // blocks()

  /**
   * Get an interator for all the transactions in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Transaction> iterator() {
    return new Iterator<Transaction>() {
      /** Keep track of the current node for iterator */
      private Node current = head;

      public boolean hasNext() {
        return (current.nextNode != null);
      } // hasNext()

      public Transaction next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        } //if
        Transaction data = current.getData().getTransaction();
        current = current.nextNode;
        return data;
      } // next()
    };
  } // iterator()
} // class BlockChain
