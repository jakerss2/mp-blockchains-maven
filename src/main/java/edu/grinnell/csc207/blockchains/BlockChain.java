package edu.grinnell.csc207.blockchains;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.text.html.HTMLDocument;

import edu.grinnell.csc207.util.AssociativeArray;

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

  /** The element at the front of the BlockChain */
  public Node head;

  /** The element at the back of the BlockChain */
  private Node tail;

  /** The validator for the BlockChain */
  private HashValidator validator;

  /** Keep track of the length of the BlockChain */
  private int size;

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
    this.validator = check;
    Transaction initTrans = new Transaction("", "", 0);
    byte[] initByte = new byte[0];
    Hash initHash = new Hash(initByte);
    Block initBlock = new Block(0, initTrans, initHash, this.validator);
    Node initNode = new Node (null, null, initBlock);
    this.head = initNode;
    this.tail = initNode;
    this.size = 1;
  } // BlockChain(HashValidator)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

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
    return new Block(this.size, t, this.tail.getData().getHash(), this.validator);
  } // mine(Transaction)

  /**
   * Get the number of blocks curently in the chain.
   *
   * @return the number of blocks in the chain, including the initial block.
   */
  public int getSize() {
    return this.size;
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
    Block blockCopy = new Block(blk.getNum(), blk.getTransaction(), blk.getPrevHash(), blk.getNonce());

    if (!this.validator.isValid(blk.getHash())) {
      throw new IllegalArgumentException("Invalid hash in appended block: " + blk.getHash());
    } else if (!blk.getHash().equals(blockCopy.getHash())) {
      throw new IllegalArgumentException("Invalid hash in appended block: " + blk.getHash());
    } else if (!blk.getPrevHash().equals(this.tail.getData().getHash())) {
      throw new IllegalArgumentException("Does not match the previous hash: " + this.tail.getData().getHash() + " was " + blk.getPrevHash());
    } else {
      Node newNode = new Node(this.tail, null, blk);
      this.tail.nextNode = newNode;
      this.tail = newNode;
      this.size++;
    }
  } // append()

  /**
   * Attempt to remove the last block from the chain.
   *
   * @return false if the chain has only one block (in which case it's
   *   not removed) or true otherwise (in which case the last block
   *   is removed).
   */
  public boolean removeLast() {
    if (this.size == 1) {
      return false;
    } // if
    this.tail = this.tail.prevNode;
    this.tail.nextNode = null;
    this.size--;
    return true;
  } // removeLast()

  /**
   * Get the hash of the last block in the chain.
   *
   * @return the hash of the last sblock in the chain.
   */
  public Hash getHash() {
    return this.tail.getData().getHash();
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
    try {
      this.check();
    } catch (Exception e) {
      return false;
    } // try/catch
    return true;
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
    AssociativeArray arr = new AssociativeArray<String, Integer>();
    Block prevBlock = null;
    while(blockIter.hasNext()) {
      Block curBlock = blockIter.next();
      Transaction curTrans = curBlock.getTransaction();
      if (curTrans.getAmount() < 0) {
        throw new Exception("Invalid transaction amount " + curTrans.getAmount()
        + " in block " + curBlock.getNum());
      } // if
      if (!(curTrans.getSource().equals(""))) {
        try {
          arr.set(curTrans.getSource(), (int)arr.get(curTrans.getSource()) - curTrans.getAmount());
          if ((int) arr.get(curTrans.getSource()) < 0) {
            throw new Exception("Insufficient funds for " + curTrans.getSource() + " in "
                + curBlock.getNum() + ": Has " + (int)arr.get(curTrans.getSource()) + " needs "
                + curTrans.getAmount());
          } // if
        } catch (Exception e) {
          throw new Exception("Unknown Source in block " + curBlock.getNum() + ": " + curTrans.getSource());
        } // try/catch
      } // if
      try {
        arr.set(curTrans.getTarget(), (int)arr.get(curTrans.getTarget()) + curTrans.getAmount());
      } catch (Exception e) {
        arr.set(curTrans.getTarget(), curTrans.getAmount());
      } // try/catch
      Block dupBlock = new Block(curBlock.getNum(), curBlock.getTransaction(), curBlock.getPrevHash(), curBlock.getNonce());
      if (!curBlock.getHash().equals(dupBlock.getHash())){
        throw new Exception("Hash is not correct in block " + curBlock.getNum());
      } // if

      if (!this.validator.isValid(curBlock.getHash())) {
        throw new Exception("Hash is not correct in block " + curBlock.getNum());
      } // if
      if (prevBlock != null) {
        if (!prevBlock.getHash().equals(curBlock.getPrevHash())) {
          throw new Exception("The previous hash stored in " + curBlock.getNum() + " is not correct");
        } // if
      } // if
      prevBlock = curBlock;
    } // while
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

      /** An associative array of all users */
      String[] userArr;

      /** Index of block */
      int index = 0;

      {
        AssociativeArray<String, Boolean> tmpArr = new AssociativeArray<>();
        try {
          while (current != null) {
            String sourceName = current.getData().getTransaction().getSource();
            String targetName = current.getData().getTransaction().getTarget();
            if(!(sourceName.equals(""))) {
              tmpArr.set(sourceName, true);
            } // if
            if(!(targetName.equals(""))) {
              tmpArr.set(targetName, true);
            } // if
            current = current.nextNode;
          } // while
          userArr = tmpArr.getAllKeys();
        } catch (Exception e) {
        } // try/catch
      } // init userArr

      public boolean hasNext() {
        return (index < userArr.length);
      } // hasNext()

      public String next() {
        return userArr[index++];
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
      String sourceName = obj.getSource();
      String targetName = obj.getTarget();
      if (sourceName.equals(user)) {
        userBalance -= obj.getAmount();
      } else if (targetName.equals(user)) {
        userBalance += obj.getAmount();
      } // if
    } // for
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
        return current != null;
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
        return (current != null);
      } // hasNext()

      public Transaction next() {
        if (!hasNext()) {
        throw new NoSuchElementException();
        }
        Transaction data = current.getData().getTransaction();
        current = current.nextNode;
        return data;
      } // next()
    };
  } // iterator()
} // class BlockChain
