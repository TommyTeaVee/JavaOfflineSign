package com.algorand.javatest;

import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.account.Account;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Submit from a signed tx in a file
 *
 */
public class SignFromFile 
{
    public static void main(String args[]) throws Exception {

        SignedTransaction signedTx = null;
        Transaction tx = null;
        try {
            //Reading  from a file 
            FileInputStream file = new FileInputStream("./tx.sav"); 
            ObjectInputStream in = new ObjectInputStream(file); 
            tx = (Transaction)in.readObject();
            in.close(); 
            file.close(); 


            //Using a backup mnemonic to recover the pk to sign tx
            //This is for demo purposes only
            //Never store mneomonic in code. This should be a call out to get the users backupphrase
            String SRC_ACCOUNT = "only atom opera jealous obscure fade drama bicycle near cable company other hazard math argue anxiety corn approve crumble trust hunt cattle parent ability raw";
            Account src = new Account(SRC_ACCOUNT);
            signedTx = src.signTransaction(tx);

             
            FileOutputStream ofile = new FileOutputStream("./stx.sav"); 
            ObjectOutputStream out = new ObjectOutputStream(ofile); 
            out.writeObject(signedTx);
            out.close(); 
            file.close();
            System.out.println("Transaction Signed and written to a file");
        } catch (Exception e) {
            System.out.println("Exception: " + e); 
            return;
        }

    }

}

