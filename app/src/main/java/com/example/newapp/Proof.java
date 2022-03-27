package com.example.newapp;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class Proof {


    public String[] sigma(BigInteger p, BigInteger q, BigInteger y, BigInteger g, BigInteger h, BigInteger pi, BigInteger gamma, BigInteger beta, BigInteger[] Ec, BigInteger[] tau, BigInteger[] B, BigInteger[] V){

        //Proof

        Random randNum = new Random();
        int len = 14;

        BigInteger mu1 = new BigInteger(len, randNum);
        mu1 = mu1.mod(q);

        BigInteger mu2 = new BigInteger(len, randNum);
        mu2 = mu2.mod(q);

        BigInteger v = new BigInteger(len, randNum);
        v = v.mod(q);

        BigInteger[] B_prime = {((y.modPow(mu1, p)).multiply(Ec[0].modPow(mu2,p))).mod(p), ((g.modPow(mu1, p)).multiply(Ec[1].modPow(mu2,p))).mod(p) };

        BigInteger[] V_prime = {((h.modPow(v, p)).multiply(g.modPow(mu2,p))).mod(p), g.modPow(v, p)};

        BigInteger[] e_values = {tau[0],tau[1],tau[2],Ec[0],Ec[1],B[0],B[1],V[0],V[1],B_prime[0],B_prime[1],V_prime[0], V_prime[1]};


        BigInteger[] trial = {mu1,mu2,v,h,beta,gamma,pi};

        MessageDigest ehash = null;
        try {
            ehash = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //ehash.update(e_values.toString().getBytes(StandardCharsets.UTF_8));
        ehash.update(Arrays.toString(e_values).getBytes(StandardCharsets.UTF_8));

        byte[] byteData = ehash.digest();


        BigInteger e= new BigInteger(1,byteData);
         e = e.mod(q);

        StringBuffer e_hexString = new StringBuffer();

        for (int i = 0;i< byteData.length;i++) {


            e_hexString.append(String.format("%02x", 0xff & byteData[i]));

        }

       // BigInteger e = null;




        BigInteger z1 = (e.multiply(beta)).add(mu1).mod(q);
        BigInteger z2 = (e.multiply(pi)).add(mu2).mod(q);
        BigInteger z3 = (e.multiply(gamma)).add(v).mod(q);

        String[] sigma = { String.valueOf(e_hexString), String.valueOf(e), String.valueOf(z1), String.valueOf(z2), String.valueOf(z3), Arrays.toString(e_values), Arrays.toString(trial)};
        return  sigma;




    }
}
