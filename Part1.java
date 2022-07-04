package com.example.newapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
//import java.text.ParseException;
import org.json.simple.parser.ParseException;

import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Part1 {
    BigInteger Pi; BigInteger secret;
    FileWriter file;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String[] Start(BigInteger secret, BigInteger p, BigInteger q, BigInteger g, String pwd, File userValues_file, File secret_file, File Ec_file, File servers_file, String[] finalUrl, String[] finalUrl1, String[] servers, String SetupID) throws JSONException, IOException, ParseException, KeyManagementException, NoSuchAlgorithmException, InterruptedException, NoSuchAlgorithmException {





        //Generating p,q,y,h, xtil,sessionID
        Random randNum = new Random();
        int len = 14;




        BigInteger x = new BigInteger(len, randNum);
        x = x.mod(q);

        BigInteger y = g.modPow(x, p);




        BigInteger H = new BigInteger(len, randNum);
        H = H.mod(q);

        BigInteger h  = g.modPow(H, p);

        BigInteger H_0 = new BigInteger(len, randNum);
        H_0 = H_0.mod(q);

        BigInteger h_prime  = g.modPow(H_0, p);




        //https://cstheory.stackexchange.com/questions/5109/how-to-compute-integer-hash-of-a-string

        MessageDigest passwd = MessageDigest.getInstance("SHA-256");

        passwd.update(pwd.getBytes());
        byte byteData[] = passwd.digest();
        BigInteger Pi_min = new BigInteger(1,byteData);
        //BigInteger p_1 = p.subtract(BigInteger.valueOf(1));
        BigInteger Pi_mod = Pi_min.mod(q);


        //check if Pi = 0. If Pi =0, it will not have a multiplicative inverse.

        BigInteger Pi = null;

        if(Pi_mod == BigInteger.valueOf(0) ){

             Pi = Pi_mod.add(BigInteger.valueOf(1));

        }



        else{
             Pi = Pi_mod;
        }


        //Write h, h_prime, y to file


        org.json.JSONArray user_values = new org.json.JSONArray();
        JSONObject item = new JSONObject();

        item.put("Name", "userValues");
        item.put("h", h.toString());
        item.put("h_prime", h_prime.toString());
        item.put("y", y.toString());



        user_values.put(item);


        //write to userValues_file
        file = new FileWriter(userValues_file);
        file.write(item.toString());
        file.close();




        //share x (2,3)
        share_x obj_x = new share_x();
        String sharex_time = obj_x.Gen_x(q, p, x, g, y, h, h_prime, 2, 3, finalUrl, servers, SetupID);


        //share_pr
       share_pr obj_pr = new share_pr();
        // BigInteger S2 = null;


         String[] arrValues = obj_pr.Gen_pr(secret_file, Ec_file, p, q, secret, g, y, Pi, 2, 3, finalUrl, servers, SetupID);

        String S1 = arrValues[0];
        String SharePrTime = arrValues[1];
        String Ec_Time = arrValues[2];

         Thread.sleep(100);


        //Get ServerValues
        GetServerValues objGSV = new GetServerValues();

       //String ver = new String();

      JSONArray ver = new JSONArray();


         ver = objGSV.WriteServerFile(servers_file, finalUrl1);

        return new String[]{S1, sharex_time, SharePrTime, Ec_Time};
        //return ver.toJSONString();


    }


}
