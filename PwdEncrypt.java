package com.example.newapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class PwdEncrypt {

    BigInteger Zq;
    BigInteger g;
    BigInteger y;
    BigInteger Pi;
    FileWriter file;

    public BigInteger[] Ec(BigInteger p, BigInteger q, BigInteger g, BigInteger y, BigInteger Pi, File Ec_file) throws IOException, JSONException {


        //BigInteger y = g.modPow(x, Zq);
       // BigInteger p_1 = p.subtract(BigInteger.valueOf(1));


        //compute Pi_inv such that Pi_inv * Pi = 1 mod Zq-1

       BigInteger Pi_inv = Pi.modInverse(q);


        //BigInteger Pi_inv = BigInteger.valueOf(1);

        //Compute Ec

        //BigInteger alpha = new BigInteger(String.valueOf(randomNum));
        Random randNum = new Random();
        int len = 14;

        BigInteger alpha = new BigInteger(len, randNum);
        alpha = alpha.mod(q);

        //BigInteger alpha = BigInteger.valueOf(13);

        BigInteger a = y.modPow(alpha, p);
        BigInteger b = g.modPow(Pi_inv, p);

        BigInteger c = (a.multiply(b)).mod(p);
        BigInteger d = g.modPow(alpha, p);

        BigInteger[] Ec = {c, d};
       // BigInteger[] Mc = {c, d};


        //Add Ec to Json Array
        JSONArray Ec_values = new JSONArray();
        JSONObject item = new JSONObject();

        item.put("Name", "Ec_values");
        item.put("Ec_0", Ec[0].toString());
        item.put("Ec_1", Ec[1].toString());


        Ec_values.put(item);


        //write to Ec_file
        file = new FileWriter(Ec_file);
        file.write(item.toString());
        file.close();



        return Ec;

    }
}


