package com.example.newapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;

import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class share_x {
    public BigInteger Zq;
    public BigInteger x;
    public int t ;
    public int n;
    public String[] url;
    public String[] servers;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String Gen_x(BigInteger q, BigInteger p, BigInteger x, BigInteger g, BigInteger y, BigInteger h, BigInteger h_prime, int t, int n, String[] url, String[] servers, String SetupID){

        //Random random_x = new Random();

        //BigInteger maxLimit = new BigInteger(String.valueOf(Zq));
        //BigInteger x = new BigInteger(String.valueOf(15));
        //x = x.mod(maxLimit);

        long share_x = System.nanoTime();
        final SecretShare[] SharesOfX = Shamir.split(x, t, n, q);
        long Totalshare_x = System.nanoTime() - share_x;




        SetUp1 obj = new SetUp1();



        for (int i = 0; i<n ; i++){


            //String.valueOf(shares_x[i]).split(",");
            try {
                obj.Share_x("Step1-setup", url[i], q, p ,g,y,h,h_prime, String.valueOf(SharesOfX[i]).split(","), "User", servers[i], SetupID);
            } catch (JSONException | NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }

        }


    return String.valueOf(Totalshare_x);

}

}
