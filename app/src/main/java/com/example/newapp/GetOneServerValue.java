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

public class GetOneServerValue {
    String url;
    TextView mTextViewResult;
    public static FileWriter file;

    public JSONObject getValue(String url) throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException {


        //OkHttpClient client = new OkHttpClient();

        TrustManager objClient = new TrustManager();

        OkHttpClient client = objClient.getUnsafeOkHttpClient();

        JSONArray ser = new JSONArray();



        Request request = new Request.Builder()
                .url(url)
                .build();

        Response responses = client.newCall(request).execute();


        String jsonData = responses.body().string();
        JSONParser parser = new JSONParser();


        if(jsonData.isEmpty()){

            jsonData = "{}";

        }

       else {
           jsonData = jsonData;
        }



        JSONObject obj = (JSONObject) parser.parse(jsonData);





        return obj;




    }
}
