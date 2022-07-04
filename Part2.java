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
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class Part2 {
    BigInteger g;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String[] Recon_Secret(BigInteger S1, BigInteger p, BigInteger q, BigInteger g, String pwd, File Ec_file, File servers_file, File secret_file, File userValues_file, String[] finalUrl, String[] finalUrl1, String[] servers, String phaseID) throws IOException, ParseException, InterruptedException, NoSuchAlgorithmException, JSONException, KeyManagementException, NoSuchAlgorithmException {


        //read p,q,g,y,h,xtil

        //Read Ec from file....
        JSONParser parser = new JSONParser();
        //File path1 = Ec_File.getAbsoluteFile();
        JSONObject object = (JSONObject) parser.parse(new FileReader(userValues_file.getAbsoluteFile()));

        //JSONObject object = (JSONObject) obj2.get(0); //gets the json object from json array

        //BigInteger p = BigInteger.valueOf((Long) object.get("p"));
        //BigInteger q = BigInteger.valueOf((Long) object.get("q"));
        //BigInteger g = BigInteger.valueOf((Long) object.get("g"));
        String hh = (String) object.get("h");
        String yy = (String) object.get("y");

        BigInteger h = new BigInteger(hh);
        BigInteger y = new BigInteger(yy);


        //BigInteger xtil = BigInteger.valueOf((Long) object.get("xtil"));


        //Hash the string pwd to a bigint
        MessageDigest passwd = MessageDigest.getInstance("SHA-256");


        passwd.update(pwd.getBytes());
        byte byteData[] = passwd.digest();
        BigInteger Pi_min = new BigInteger(1, byteData);
        BigInteger Pi_mod = Pi_min.mod(q);
        //BigInteger p_1 = p.subtract(BigInteger.valueOf(1));

        BigInteger Pi = null;

        if (Pi_mod == BigInteger.valueOf(0)) {

            Pi = Pi_mod.add(BigInteger.valueOf(1));

        } else {
            Pi = Pi_mod;
        }


        //Get Nonces and online servers
        Nonces objNonces = new Nonces();

        BigInteger[][] NonOnline = new BigInteger[0][];
        BigInteger[][] CheckNonOnline = new BigInteger[0][];

        NonOnline = objNonces.Nonce(finalUrl, finalUrl1, servers, phaseID);

        //if less than two servers online
        //Check if NonOnline is empty

        String[] values = new String[]{};

        if (Arrays.equals(NonOnline, CheckNonOnline)) {

            values = values;

        } else {


            BigInteger[] nonces = {new BigInteger(String.valueOf(NonOnline[0][0])), new BigInteger(String.valueOf(NonOnline[0][1]))};

            BigInteger[] OnlineServers = {NonOnline[1][0], NonOnline[1][1]};


            Random randNum = new Random();
            int len = 14;
            BigInteger xtil = new BigInteger(len, randNum);
            xtil = xtil.mod(q);


            PrivateKeyRecon objPKR = new PrivateKeyRecon();
            BigInteger[][] BVTau = new BigInteger[0][];
            // BigInteger[] result = new BigInteger[0];


            BigInteger[][] BVTauTime = objPKR.SecretRecon(p, q, g, y, h, Pi, xtil, nonces, Ec_file, finalUrl, servers, OnlineServers, phaseID);

            BVTau = new BigInteger[][]{BVTauTime[0],BVTauTime[1], BVTauTime[2]};

            String totalTime_BVTau = String.valueOf(BVTauTime[3][0]);


            Thread.sleep(100);

            //Reconstruct secret


            SessionKey objSecret = new SessionKey();
            //BigInteger Secret = BigInteger.valueOf(0);
            //BigInteger Secret1 = new BigInteger[0];
            JSONObject y_Prime = new JSONObject();

            String[] Secret = objSecret.keySecret(S1, BVTau, OnlineServers, finalUrl1, xtil, p, q, servers_file, secret_file);


            values = new String[]{String.valueOf(Secret[1]), totalTime_BVTau, Secret[0]};
        }

        //return Secret;

        return values;


    }
}



