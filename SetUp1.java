package com.example.newapp;
import android.app.Activity;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import okhttp3.FormBody;
import okhttp3.Response;


public class SetUp1 {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String name;
    String url;
    BigInteger Zq;
    BigInteger g;
    BigInteger y;
    String[] share_x; String source; String destination; BigInteger sessionID;

    @RequiresApi(api = Build.VERSION_CODES.O)

    public void Share_x (String name, String url, BigInteger q, BigInteger p, BigInteger g, BigInteger y, BigInteger h,BigInteger h_prime, String[] share_x, String source, String destination, String SetupID) throws JSONException, NoSuchAlgorithmException, KeyManagementException {







        TrustManager objClient = new TrustManager();

        OkHttpClient client = objClient.getUnsafeOkHttpClient();

       // OkHttpClient client = new OkHttpClient();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        String r = dtf.format(now);

        Random randNum = new Random();
        int len = 14;

        BigInteger SessionID1 = new BigInteger(len, randNum);


        String sessionID2 =  r + SessionID1.toString() ;

        BigInteger a = new BigInteger(share_x[1]);
        int i = Integer.parseInt(share_x[0]);


        JSONArray array = new JSONArray();
        array.put(i);
        array.put(a.toString());

        JSONObject item = new JSONObject();
        item.put("Name", name);
        item.put("src", source);
        item.put("dst", destination);
        item.put("q", q.toString());
        item.put("p", p.toString());
        item.put("g", g.toString());
        item.put("y", y.toString());
        item.put("h", h.toString());
        item.put("h_prime", h_prime.toString());
        //item.put("ShareID_x", i);
        item.put("share_x", array);

        item.put("SetupID", SetupID);

        item.put("sessionID", sessionID2);

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


