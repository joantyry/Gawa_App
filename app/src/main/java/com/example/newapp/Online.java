package com.example.newapp;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ConnectException;
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

public class Online {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void online( String url,  String destination, String phaseID) throws JSONException, KeyManagementException, NoSuchAlgorithmException {

        //OkHttpClient client = new OkHttpClient();
        TrustManager objClient = new TrustManager();

        OkHttpClient client = objClient.getUnsafeOkHttpClient();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        String r = dtf.format(now);

        Random randNum = new Random();
        int len = 14;

        BigInteger SessionID1 = new BigInteger(len, randNum);


        String sessionID =  r + SessionID1.toString() ;

        //JSONArray array = new JSONArray();
        JSONObject item = new JSONObject();
        item.put("Name", "Reconstruction1");
        item.put("src", "User");
        item.put("dst", destination);
        item.put("Message", "Nonces!");
        item.put("phaseID", phaseID);
        item.put("sessionID", sessionID);

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

    public int getStatus(String url) throws KeyManagementException, NoSuchAlgorithmException, IOException, ConnectException {


        int responseCode = 0;
        try {
            TrustManager objClient = new TrustManager();

            OkHttpClient client = objClient.getUnsafeOkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            responseCode = response.code();
        } catch (ConnectException e) {
            e.printStackTrace();
        }


        return responseCode;

    }
}
