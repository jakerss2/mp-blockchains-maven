package edu.grinnell.csc207.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;

import edu.grinnell.csc207.blockchains.Block;
import edu.grinnell.csc207.blockchains.BlockChain;
import edu.grinnell.csc207.blockchains.HashValidator;
import edu.grinnell.csc207.blockchains.Transaction;
import edu.grinnell.csc207.util.IOUtils;

/**
 * A simple UI for our BlockChain class.
 *
 * @author Your Name Here
 * @author Samuel A. Rebelsky
 */
public class BlockChainUI {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The number of bytes we validate. Should be set to 3 before submitting.
   */
  static final int VALIDATOR_BYTES = 3;

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Print out the instructions.
   *
   * @param pen
   *   The pen used for printing instructions.
   */
  public static void instructions(PrintWriter pen) {
    pen.println("""
      Valid commands:
        mine: discovers the nonce for a given transaction
        append: appends a new block onto the end of the chain
        remove: removes the last block from the end of the chain
        check: checks that the block chain is valid
        users: prints a list of users
        balance: finds a user's balance
        transactions: prints out the chain of transactions
        blocks: prints out the chain of blocks (for debugging only)
        help: prints this list of commands
        quit: quits the program""");
  } // instructions(PrintWriter)

  // +------+--------------------------------------------------------
  // | Main |
  // +------+

  /**
   * Run the UI.
   *
   * @param args
   *   Command-line arguments (currently ignored).
   */
  public static void main(String[] args) throws Exception {
    PrintWriter pen = new PrintWriter(System.out, true);
    BufferedReader eyes = new BufferedReader(new InputStreamReader(System.in));

    // Set up our blockchain.
    HashValidator validator =
        (h) -> {
          if (h.length() < VALIDATOR_BYTES) {
            return false;
          } // if
          for (int v = 0; v < VALIDATOR_BYTES; v++) {
            if (h.get(v) != 0) {
              return false;
            } // if
          } // for
          return true;
        };
    BlockChain chain = new BlockChain(validator);

    instructions(pen);

    boolean done = false;

    String source;
    String target;
    String user;
    int amount;
    long nonce;

    while (!done) {
      pen.print("\nCommand: ");
      pen.flush();
      String command = eyes.readLine();
      if (command == null) {
        command = "quit";
      } // if

      switch (command.toLowerCase()) {
        case "append":
          source = IOUtils.readLine(pen, eyes, "Source (return for deposit): ");
          target = IOUtils.readLine(pen, eyes, "Target: ");
          amount = IOUtils.readInt(pen, eyes, "Amount: ");
          nonce = IOUtils.readLong(pen, eyes, "Nonce: ");
          Block newBlock = new Block(chain.getSize(),
              (new Transaction(source, target, amount)), chain.getHash(), nonce);
          try {
            chain.append(newBlock);
          } catch (Exception e) {
            pen.println("Could not append: " + e.getMessage());
          } // try/catch
          pen.println("Appended: " + newBlock.toString());
          break;

        case "balance":
          user = IOUtils.readLine(pen, eyes, "User: ");
          int balance = chain.balance(user);
          pen.println(user + "'s balance is " + balance);
          break;

        case "blocks":
          Iterator<Block> blocks = chain.blocks();
          while (blocks.hasNext()) {
            pen.println(blocks.next().toString());
          } //while
          break;

        case "check":
          try {
            chain.check();
            pen.println("The blockchain checks out.");
          } catch (Exception e) {
            pen.println(e.getMessage());
          } // try/catch
          break;

        case "help":
          instructions(pen);
          break;

        case "mine":
          source = IOUtils.readLine(pen, eyes, "Source (return for deposit): ");
          target = IOUtils.readLine(pen, eyes, "Target: ");
          amount = IOUtils.readInt(pen, eyes, "Amount: ");
          Block b = chain.mine(new Transaction(source, target, amount));
          pen.println("Use nonce: " + b.getNonce());
          break;

        case "quit":
          done = true;
          break;

        case "remove":
          chain.removeLast();
          pen.println("Last value in blockchain removed");
          break;

        case "transactions":
          for (Transaction obj : chain) {
            pen.println(obj.toString());
          } // for
          break;

        case "users":
          Iterator<String> users = chain.users();
          while (users.hasNext()) {
            pen.println(users.next());
          } //while
          break;

        default:
          pen.println("invalid command entered, please try again");
          break;
      } // switch
    } // while

    pen.printf("\nGoodbye\n");
    eyes.close();
    pen.close();
  } // main(String[])
} // class BlockChainUI
