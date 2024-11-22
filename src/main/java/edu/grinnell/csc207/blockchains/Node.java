package edu.grinnell.csc207.blockchains;

public class Node {
  

  //fields
  Node nextNode;

  Node prevNode;

  private Block blockData;

  public Node(Node prev, Node next, Block data) {
    this.prevNode = prev;
    this.blockData = data;
    this.nextNode = next;
  } //Node

  //methods
  public Block getData() {
    return blockData;
  }
} //Node