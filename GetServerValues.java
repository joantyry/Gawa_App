package com.example.newapp;

import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;



import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

//import org.json.JSONException;

//import org.json.JSONParser;

import okhttp3.OkHttpClient;


public class GetServerValues{
        String url;
        TextView mTextViewResult;
    public static FileWriter file;

        public JSONArray get(String[] url) throws IOException, NoSuchAlgorithmException, KeyManagementException, InterruptedException, ParseException {


            //OkHttpClient client = new OkHttpClient();

            TrustManager objClient = new TrustManager();


            OkHttpClient client = objClient.getUnsafeOkHttpClient();


            JSONArray ser = new JSONArray();

            JSONObject obj = new JSONObject();

           GetOneServerValue objValue = new GetOneServerValue();



            for (int i = 0; i < 3; i++) {

           /*     Request request = new Request.Builder()
                        .url(url[i])
                        .build();

                Response responses = client.newCall(request).execute();
                String jsonData = responses.body().string();


                JSONParser parser = new JSONParser();




                obj = (JSONObject) parser.parse(jsonData);
                 */


                obj= objValue.getValue(url[i]);
                //boolean a = obj.toString().isEmpty();


                int whi = 1;

                while (!String.valueOf(obj.get("Name")).contentEquals("Step1-setup")){

                    Thread.sleep(100 * whi);

                    obj= objValue.getValue(url[i]);
                    //a = obj.toString().isEmpty();

                    whi = whi + 1;

                    if (whi == 1000){
                        break;
                    }
                }

                if (String.valueOf(obj.get("Name")).contentEquals("Step1-setup") ){

                    ser.add(obj);
                }



                //ser.add(obj);

            }


            JSONObject a = (JSONObject) ser.get(0);
            //String b = String.valueOf(a.get("Name"));
            return ser;
            //return b;




        }

  public JSONArray WriteServerFile(File filename, String[] url) throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException, InterruptedException {

            GetServerValues objGSV = new GetServerValues();
            JSONArray ser = objGSV.get(url);



            //write to server file
            file = new FileWriter(filename);
            file.write(ser.toJSONString());
            file.close();

            //Read server file
            JSONParser parser = new JSONParser();
            File path1 = filename.getAbsoluteFile();
            JSONArray obj2 = (JSONArray) parser.parse(new FileReader(path1));

            return obj2;


        }

}







