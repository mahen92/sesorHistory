package com.bonbloc.sample.sensorhistory;

import sawtooth.sdk.processor.TransactionProcessor;

/**
 * Example program to illustrate wrting of Transaction Processor.
 *
 * <p>Content Protection is simple application that stores the information/content over Blockchain.
 * Admin can grant access to a user based on his role - none, read, write. Any of the operation
 * performed - read, write will be logged on Blockchain again. Admin can view log of transactions.
 */
public class App {
  public static void main(String[] args) {
    TransactionProcessor transactionProcessor = new TransactionProcessor("tcp://localhost:8008");
    transactionProcessor.addHandler(new sensorHandler());
    Thread thread = new Thread(transactionProcessor);
    thread.start();
  }
}
