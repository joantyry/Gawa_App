package com.example.newapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class share_pr {
    public BigInteger Zq;
    public BigInteger secret;
    public int t ;
    public int n;
    public BigInteger y;
    public BigInteger Pi;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String[] Gen_pr(File secret_file, File Ec_file, BigInteger p, BigInteger q, BigInteger secret, BigInteger g, BigInteger y, BigInteger Pi, int t, int n, String[] url, String[] servers, String SetupID) throws JSONException, IOException, NoSuchAlgorithmException, KeyManagementException {

        FileWriter UserFile;
        //Context context = new Context();
        // share initial (2,2)

        long share_pr = System.nanoTime();
        final SecretShare[] InitialShares = Shamir.split(secret, 2, 2, q);


        String arr_0 = String.valueOf(InitialShares[0]);

        String[] arrOfStr_0 = arr_0.split(",");

        BigInteger S1 = new BigInteger(arrOfStr_0[1]);







        //share S2 (2,3)
        String arr_1 = String.valueOf(InitialShares[1]);
        String[] arrOfStr_1 = arr_1.split(",");
        BigInteger S2 = new BigInteger(arrOfStr_1[1]);

        final SecretShare[] SharesOfPr = Shamir.split(S2, t, n, q);
        long Totalshare_pr = System.nanoTime() - share_pr;


        //write S1 to file

        JSONArray arr = new JSONArray();
        JSONObject item = new JSONObject();

        item.put("Name", "InitialSetup");
        item.put("S1", S1.toString());
        //item.put("SessionID", sessionID);

        arr.put(item);

        UserFile = new FileWriter(secret_file);
        UserFile.write(arr.toString());
        UserFile.close();



        SetUp2 obj = new SetUp2();
        PwdEncrypt obj2 = new PwdEncrypt();

        long Ec_time = System.nanoTime();
        BigInteger[] Ec = obj2.Ec( p, q, g, y, Pi, Ec_file);
        long TotalEc_time = System.nanoTime() - Ec_time;

        //Send Ec and pr_shares to the servers


        for (int i = 0; i<n ; i++){


           obj.secret("Step2-setup", url[i], Ec, String.valueOf(SharesOfPr[i]).split(","), "User", servers[i], SetupID);


        }

        return new String[]{S1.toString(), String.valueOf(Totalshare_pr), String.valueOf(TotalEc_time)};


    }
}
