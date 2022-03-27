package com.example.newapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;

public class PrivateKeyRecon {

    public BigInteger p;
    public BigInteger g ;
    public BigInteger y;
    public BigInteger h;
    public BigInteger pi;
    public BigInteger xtil;
    public BigInteger[] nonces;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public BigInteger[][] SecretRecon(BigInteger p, BigInteger q, BigInteger g, BigInteger y, BigInteger h, BigInteger pi, BigInteger xtil, BigInteger[] nonces, File Ec_File, String[] url, String[] servers, BigInteger[] OnlineServers, String phaseID) throws IOException, ParseException, JSONException, NoSuchAlgorithmException, KeyManagementException {

        //BigInteger xtil = BigInteger.valueOf(5);
        //BigInteger beta = BigInteger.valueOf(3);
       // BigInteger gamma = BigInteger.valueOf(4);

        long startTime_BVTau = System.nanoTime();

        Random randNum = new Random();
        int len = 14;

        BigInteger beta = new BigInteger(len, randNum);
        beta = beta.mod(q);

        BigInteger gamma = new BigInteger(len, randNum);
        gamma = gamma.mod(q);

       // BigInteger xtil = new BigInteger(len, randNum);
        //xtil = xtil.mod(q);


        BigInteger ytil = g.modPow(xtil, p);

        //Read Ec from file....
        JSONParser parser = new JSONParser();
        File path1 = Ec_File.getAbsoluteFile();
        JSONObject object = (JSONObject) parser.parse(new FileReader(path1));

        //JSONObject object = (JSONObject) obj2.get(0); //gets the json object from json array

        BigInteger Ec_0 = new BigInteger(String.valueOf(object.get("Ec_0")));
        BigInteger Ec_1 = new BigInteger(String.valueOf(object.get("Ec_1")));


        BigInteger g_inv = g.modInverse(p);

        BigInteger b = Ec_0.modPow(pi, p);
        BigInteger w = Ec_1.modPow(pi, p);

        BigInteger c = (b.multiply(g_inv)).mod(p);

        //BigInteger tri = (y.modPow(BigInteger.valueOf(7), Zq)).modPow(pi, Zq).multiply(g).mod(Zq);


        //BigInteger[] B =  [pow(y, beta)* w % Zq, pow(g, beta)*c %Zq];
        BigInteger[] B =  {((y.modPow(beta, p)).multiply(c)).mod(p), ((g.modPow(beta, p)).multiply(w)).mod(p)};





        BigInteger[] k = {c, w};

        //V = [pow(h, gamma, Zq)* pow(g, pi, Zq)% Zq, pow(g, gamma, Zq)]

        BigInteger[] Ec = {Ec_0, Ec_1};

        BigInteger[] V = {((h.modPow(gamma, p)).multiply(g.modPow(pi, p))).mod(p), g.modPow(gamma, p)};

        BigInteger[] tau = {ytil, nonces[0], nonces[1] };

        BigInteger[][] BVTau = {B, V, tau};


        //proof

        Proof objProof = new Proof();
        String[] sigma = objProof.sigma(p,q,y,g,h,pi,gamma,beta,Ec,tau,B,V);

        long totalTime_BVTau = System.nanoTime() - startTime_BVTau;
        //String totalTime = String.valueOf(totalTime_BVTau);
        BigInteger[] totalTime = {BigInteger.valueOf(totalTime_BVTau)};


        BigInteger[][] BVTauTime = {B, V, tau, totalTime};


        if(Arrays.equals(OnlineServers, new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(2)})) {
            url = new String[]{url[0], url[1]};
            servers = new String[]{servers[0], servers[1]};

        }

       else if(Arrays.equals(OnlineServers, new BigInteger[]{BigInteger.valueOf(1), BigInteger.valueOf(3)})) {
            url = new String[]{url[0], url[2]};
            servers = new String[]{servers[0], servers[2]};

        }

       else{
            url = new String[]{url[1], url[2]};
            servers = new String[]{servers[1], servers[2]};

        }


        //Send BVtau to the Online Servers

        Reconstruction2 ObjRecon = new Reconstruction2();

        for (int i = 0; i<2 ; i++){

            ObjRecon.Recon2("Reconstruction2", url[i], BVTau, sigma,"User", servers[i], OnlineServers, phaseID);
       }



        //return BVTau;
        return BVTauTime;
        //return trial;

    }

}
