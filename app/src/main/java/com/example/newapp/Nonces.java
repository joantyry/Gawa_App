package com.example.newapp;

import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ConnectException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class Nonces {

    String url;
    TextView mTextViewResult;
    public static FileWriter file;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public BigInteger[][] Nonce(String[] url, String[] url2, String[] servers, String phaseID) throws IOException, ConnectException, ParseException, JSONException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {

        Online objOnline = new Online();


        JSONObject Nonce1 = new JSONObject();
        JSONArray Nonce = new JSONArray();

        int status = 0;

        //check online servers
        /*for (int i = 0; i < 3; i++) {

            int s = objOnline.getStatus(url[i]);
            //Nonce.put(s);

            //String sb = Integer.toString(s);

            if (s == 200) {
                status = status + 1;


            }

        }

      if(status <= 1){


      }*/




        for (int i = 0; i < 3; i++) {

            int s = objOnline.getStatus(url[i]);
            //Nonce.put(s);

            //String sb = Integer.toString(s);

            if (s == 200) {

                //status = status +1;

                objOnline.online(url[i], servers[i], phaseID);

                Thread.sleep(10);

                Nonce1 = getV(url2[i]);

                //String Message_Name = (String) Nonce1.get("Name");
                int whi = 1;

                while (Nonce1.isEmpty() || !String.valueOf(Nonce1.get("Name")).contentEquals("Reconstruction1")){

                    Thread.sleep(100 * whi);
                    Nonce1 = getV(url2[i]);
                    whi = whi + 1;

                    if (whi == 6){
                        break;
                    }
                }

                if (String.valueOf(Nonce1.get("Name")).contentEquals("Reconstruction1") ){

                    Nonce.put(Nonce1);
                }


                else{
                    Nonce = Nonce;
                }



            }


        }

        BigInteger[][] Online = new BigInteger[0][];
        JSONArray Nonce_Online = new JSONArray();

        int j = Nonce.length();







            if (j == 2) {

                Nonce_Online = Nonce;


            } else if (j == 3) {

                //Nonce_Online = new JSONArray();
                JSONObject object = (JSONObject) Nonce.get(0); //changed from 0 to 2 so as not to use home server.
                JSONObject object2 = (JSONObject) Nonce.get(1);
                Nonce_Online.put(object);
                Nonce_Online.put(object2);

            } else {
                Nonce_Online = Nonce_Online;
            }



            if (Nonce_Online.toString().equals("{}") ){
                Online = Online;

            }
            else{

                //Get the json objects
                JSONObject OnliSer1 = (JSONObject) Nonce_Online.get(0);
                JSONObject OnliSer2 = (JSONObject) Nonce_Online.get(1);

                org.json.simple.JSONArray server1 = (org.json.simple.JSONArray) OnliSer1.get("Nonce");
                org.json.simple.JSONArray server2 = (org.json.simple.JSONArray) OnliSer2.get("Nonce");

                //BigInteger[] server1 = (BigInteger[]) OnliSer1.get("Nonce");
                //BigInteger[] server2 = (BigInteger[]) OnliSer2.get("Nonce");

                BigInteger[] Nonce_value = {new BigInteger(String.valueOf(server1.get(1))), new BigInteger(String.valueOf(server2.get(1)))};
                BigInteger[] OnlineServers = {new BigInteger(String.valueOf(server1.get(0))), new BigInteger(String.valueOf(server2.get(0)))};

                Online = new BigInteger[][]{Nonce_value, OnlineServers};

                // BigInteger[][] Online = {server1, server2};
            }



        return Online;
    }

    public JSONObject getV(String url) throws IOException, ParseException, NoSuchAlgorithmException, KeyManagementException {

        GetOneServerValue objNonce = new GetOneServerValue();

        JSONObject Nonce1 = new JSONObject();
        Nonce1 = objNonce.getValue(url);


        return Nonce1;

    }



}

