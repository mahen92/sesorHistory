package com.bonbloc.sample.sensorhistory;


import sawtooth.sdk.processor.State;
import sawtooth.sdk.processor.TransactionHandler;
import sawtooth.sdk.processor.exceptions.InternalError;
import sawtooth.sdk.processor.exceptions.InvalidTransactionException;
import sawtooth.sdk.protobuf.TpProcessRequest;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.protobuf.ByteString;


public class sensorHandler implements TransactionHandler {

  private static final String transactionFamilyName = "sensorhist";
  private static final String transactionFamilyVersion = "1.0";
  private  String transactionFamilyNameSpaceAddress="";
  String TRACKING =  NameSpaceUtils.calculateNameSpace("tracking", 6);
  String TRACKING_TABLE = transactionFamilyNameSpaceAddress + TRACKING;

  // Objects that will help process the transactions
  private final sensorUnpacker
      sensorUnpacker;
  

  sensorHandler() {
	  sensorUnpacker = new sensorUnpacker();
    /*
     * Find the hash of transactionFamilyName, it is used in this application to find namespace
     * Consider first 3 bytes / 6 hexadecimal characters
     */
    transactionFamilyNameSpaceAddress =
        NameSpaceUtils.calculateNameSpace(
            transactionFamilyName(), 6);
    
  }

  /**
   * Any transaction in Sawtooth is sent to apply method. Based on passed parameters the handling
   * can be done
   */
  @Override
  public void apply(TpProcessRequest transactionRequest, State state)
      {
    // Note: Request verification too is done here
	 String data= sensorUnpacker.unpackTransactionRequest(transactionRequest);
    processTransaction(data, state);
  }
  
  public String getSensorAddress(String sensorID) {
	    return TRACKING_TABLE+NameSpaceUtils.calculateNameSpace("tracking", 58);
  }
  
  public void processTransaction(
	      String data, State state)
	       {

	    String address=getSensorAddress(data);
	    String stateToStore = data;
	    ByteString byteStringStateToBeStored = ByteString.copyFromUtf8(stateToStore);
	    Map.Entry<String, ByteString> entry =
	        new AbstractMap.SimpleEntry<>(address, byteStringStateToBeStored);
	    Collection<Map.Entry<String, ByteString>> addressValues = Collections.singletonList(entry);
	    try {
			Collection<String> addresses = state.setState(addressValues);
		} catch (InternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	  }
	

  /**
   * Sawtooth expects namespaces be given as input to Peer. This is used to structure the Merkel
   * Tree and effectively index the storage.
   */
  @Override
  public Collection<String> getNameSpaces() {
    List<String> nameSpaces = new ArrayList<>();
    nameSpaces.add(getTransactionFamilyNameSpaceAddress());
    return nameSpaces;
  }

  /**
   * Version field acts as important parameter for Sawtooth to identify if a new smart contract
   * needs to be installed on the network. Sawtooth taks care of pending or ongoing requests when a
   * newer version of smart contract is asked to be registered in network.
   */
  @Override
  public String getVersion() {
    return transactionFamilyVersion;
  }

  /**
   * Sawtooth expects each TransactionFamily have its name, this can be also used to generate the
   * unique hash which could act as namespace when storing in Merkel tree.
   */
  @Override
  public String transactionFamilyName() {
    return transactionFamilyName;
  }



  /** @return the transactionFamilyNameSpace */
  private String getTransactionFamilyNameSpaceAddress() {
    return transactionFamilyNameSpaceAddress;
  }

  
}
