package edu.grinnell.csc207.blockchains;

/**
 * A Node class for a linked list to store our blockchain in.
 */
public class Node {

  //fields
  /**
   * The pointer to our next node.
   */
  Node nextNode;

  /**
   * The pointer to our previous node.
   */
  Node prevNode;

  /**
   * the data of the block that corresponds to the currentNode.
   */
  private Block blockData;

  /**
   * Creates a new node.
   * @param prev
   *  the previous node pointed to in the linked list.
   * @param next
   *  the next node pointed to in the linked list.
   * @param data
   *  the block data corresponding to our node.
   */
  public Node(Node prev, Node next, Block data) {
    this.prevNode = prev;
    this.blockData = data;
    this.nextNode = next;
  } //Node

  //methods

  /**
   * accessor method for the data stored in our block field.
   * @return
   *  the data from our block.
   */
  public Block getData() {
    return blockData;
  } //getData()
} //Node
