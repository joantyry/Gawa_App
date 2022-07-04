package com.example.newapp;

import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetReconValues {

    String url;
    TextView mTextViewResult;
    public static FileWriter file;

    public JSONArray getRecon(String[] url) throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException {
        //String[] url = {"http://174.0.246.67:9000/LocalServer/Database/userMessages.json", "http://40.117.176.226/CloudServer1/Database/userMessages.json", "http://40.76.203.48/CloudServer2/Database/userMessages.json"};
        String[] url1 = url;

        //OkHttpClient client = new OkHttpClient();
        TrustManager objClient = new TrustManager();

        OkHttpClient client = objClient.getUnsafeOkHttpClient();

        JSONArray ser = new JSONArray();

        JSONObject obj = new JSONObject();

        for (int i = 0; i < 2; i++) {

            Request request = new Request.Builder()
                    .url(url1[i])
                    .build();

            Response responses = client.newCall(request).execute();
            String jsonData = responses.body().string();


            JSONParser parser = new JSONParser();

            obj = (JSONObject) parser.parse(jsonData);

            ser.add(obj);
        }



        return ser;




    }

}



