package com.example.newapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class SetUp2 {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void secret (String name, String url, BigInteger[] Ec, String[] share_pr, String source, String destination, String SetupID) throws JSONException, KeyManagementException, NoSuchAlgorithmException {

        //OkHttpClient client = new OkHttpClient();

        TrustManager objClient = new TrustManager();

        OkHttpClient client = objClient.getUnsafeOkHttpClient();


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        String r = dtf.format(now);

        Random randNum = new Random();
        int len = 14;

        BigInteger SessionID1 = new BigInteger(len, randNum);


        String sessionID2 =  r + SessionID1.toString() ;



        BigInteger b = new BigInteger(share_pr[1]);
        int j = Integer.parseInt(share_pr[0]);

        JSONArray Ec_a = new JSONArray();
        Ec_a.put(Ec[0].toString());
        Ec_a.put(Ec[1].toString());
        //Ec_a.put(Ec[2]);

        JSONArray share_pr_a = new JSONArray();
        share_pr_a.put(j);
        share_pr_a.put(b.toString());


        JSONObject item = new JSONObject();
        item.put("Name", name);
        item.put("src", source);
        item.put("dst", destination);
        item.put("Ec", Ec_a);
        item.put("share_pr", share_pr_a);

        item.put("SetupID", SetupID);
        item.put("sessionID", SessionID1.toString());


        //array.put(item);

        //RequestBody array1 = RequestBody.create(JSON, array.toString());
        RequestBody array1 = RequestBody.create(JSON, item.toString());


        Request request = new Request.Builder()
                .url(url)
                .post(array1)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();


                }
            }


        });


    }
}


