package com.algorand.javatest;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.crypto.Digest;
import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.algod.client.AlgodClient;
import com.algorand.algosdk.algod.client.ApiException;
import com.algorand.algosdk.algod.client.api.AlgodApi;
import com.algorand.algosdk.algod.client.auth.ApiKeyAuth;

import com.algorand.algosdk.algod.client.model.TransactionID;
import com.algorand.algosdk.algod.client.model.TransactionParams;
import com.algorand.algosdk.util.Encoder;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import java.math.BigInteger;


/**
 * SaveTxForOffline
 *
 */
public class SaveTxForOffline {
    public static void main(String args[]) throws Exception {
        final String DEST_ADDR = "KV2XGKMXGYJ6PWYQA5374BYIQBL3ONRMSIARPCFCJEAMAHQEVYPB7PL3KU";
        final String SRC_ADDR = "EZG6H7XKLBMWRJ2OX47J5LZA672BTCUESIQVCZZ7BEWLQO2GPW5XKJKBLE";

        final String ALGOD_API_ADDR = "http://localhost:8080";
        final String ALGOD_API_TOKEN = "f1dee49e36a82face92fdb21cd3d340a1b369925cd12f3ee7371378f1665b9b1";

        AlgodClient client = (AlgodClient) new AlgodClient().setBasePath(ALGOD_API_ADDR);
        ApiKeyAuth api_key = (ApiKeyAuth) client.getAuthentication("api_key");
        api_key.setApiKey(ALGOD_API_TOKEN);
        AlgodApi algodApiInstance = new AlgodApi(client);       

        // get last round and suggested tx fee
        BigInteger firstRound = BigInteger.valueOf(301);
        String genId = null;
        Digest genesisHash = null;
        try {
            // Get suggested parameters from the node
            TransactionParams params = algodApiInstance.transactionParams();
            firstRound = params.getLastRound();
            genId = params.getGenesisID();
            genesisHash = new Digest(params.getGenesishashb64());

        } catch (ApiException e) {
            System.err.println("Exception when calling algod#transactionParams");
            e.printStackTrace();
        }

        //amount to send
        BigInteger amount = BigInteger.valueOf(200000);
        //number of blocks the transaction is valid for
        BigInteger lastRound = firstRound.add(BigInteger.valueOf(1000)); // 1000 is the max tx window
        Transaction tx = new Transaction(new Address(SRC_ADDR),  BigInteger.valueOf(1000), firstRound, lastRound, null, amount, new Address(DEST_ADDR), genId, genesisHash);
 
        //Save the Transaction to a file
        try { 
            //Saving of object in a file 
            FileOutputStream file = new FileOutputStream("./tx.sav"); 
            ObjectOutputStream out = new ObjectOutputStream(file); 
            out.writeObject(tx);
            out.close(); 
            file.close();
            System.out.println("Transaction written to a file");
        } catch (Exception e) { 
            System.out.println("Exception: " + e); 
        }

    }

}
