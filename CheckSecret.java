package com.example.newapp;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckSecret {


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String name;

    //@RequiresApi(api = Build.VERSION_CODES.O)

    public void check(BigInteger Secret) throws JSONException, NoSuchAlgorithmException, KeyManagementException {




        TrustManager objClient = new TrustManager();

        OkHttpClient client = objClient.getUnsafeOkHttpClient();

        // OkHttpClient client = new OkHttpClient();

        String url = "https://70.75.153.207/LocalServer/PostProcess/PostProcess2.php";

        JSONObject item = new JSONObject();
        item.put("Secret", Secret);

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


