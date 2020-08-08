package com.bonbloc.sample.sensorhistory;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.DatatypeConverter;



import sawtooth.sdk.protobuf.TpProcessRequest;

public class sensorUnpacker {
	
	public String unpackTransactionRequest(
		      TpProcessRequest transactionRequest) {
		     List<String> receivedRequestList =
		            Arrays.asList(
		                    new String(
		                            DatatypeConverter.parseBase64Binary(
		                                transactionRequest.getPayload().toStringUtf8()))
		                        .split(","));
		     return receivedRequestList.get(0);
		  }

}
