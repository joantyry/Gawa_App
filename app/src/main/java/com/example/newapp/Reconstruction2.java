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

public class Reconstruction2 {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String name;
    String url;
    BigInteger[][] BVTau; String source; String destination; BigInteger sessionID;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Recon2(String name, String url, BigInteger[][] BVTau, String[] sigma, String source, String destination, BigInteger[] OnlineServers, String phaseID) throws JSONException, KeyManagementException, NoSuchAlgorithmException {

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


        JSONArray OnlineServer = new JSONArray();
        OnlineServer.put(OnlineServers[0]);
        OnlineServer.put(OnlineServers[1]);


        JSONArray B = new JSONArray();
        B.put(BVTau[0][0].toString());
        B.put(BVTau[0][1].toString());

        JSONArray V = new JSONArray();
        V.put(BVTau[1][0].toString());
        V.put(BVTau[1][1].toString());

        JSONArray Tau = new JSONArray();
        Tau.put(BVTau[2][0].toString());
        Tau.put(BVTau[2][1].toString());
        Tau.put(BVTau[2][2].toString());

        JSONArray Sigma = new JSONArray();
        Sigma.put(sigma[0]);
        Sigma.put(sigma[1]);
        Sigma.put(sigma[2]);
        Sigma.put(sigma[3]);
        Sigma.put(sigma[4]);
        Sigma.put(sigma[5]);
        Sigma.put(sigma[6]);




        JSONObject item = new JSONObject();
        item.put("Name", name);
        item.put("src", source);
        item.put("dst", destination);
        item.put("B", B);
        item.put("V", V);
        item.put("Tau", Tau);
        item.put("Sigma", Sigma);
        item.put("OnlineServers", OnlineServer);
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
}
