package com.example.newapp;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.codahale.shamir.Scheme;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileWriter UserFile;
        Context context = null;
        EditText password;

        //final TextView text = findViewById(R.id.text1_view_result);
       // final TextView text1 = findViewById(R.id.text2_view_result);
        //final TextView text2 = findViewById(R.id.text3_view_result);

        //How to fix 'android.os.NetworkOnMainThreadException'
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button button;
        Button button1;

        button = (Button) findViewById(R.id.share);
        button1 = (Button) findViewById(R.id.reconstruct);




        //Process received intents
       Intent intent = getIntent();
       String package_name = this.getReferrer().getHost();

      Bundle sentMessage = intent.getExtras();

                                                                                                             /**/
      String Secret = null;
       String S1 = null;

       if(sentMessage!=null) {
           String name = sentMessage.getString("Name");
           
           if (name.equals("InitSetup")) {
               Secret = sentMessage.getString("Secret");
               // Secret = BigInteger.valueOf(Long.parseLong(Secret_s));

                openPasswordActivity(Secret, package_name);
           }

                                                                   /**/


            else{

               S1 = sentMessage.getString("S1");
              openSecretReconstructionActivity(S1, package_name);                                     /**/
           }

            

       }


                                                                                                             /**/
       String finalSecret = Secret;
       String finalPackage_name = package_name;
       String finalS1 = S1;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPasswordActivity(finalSecret, finalPackage_name);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecretReconstructionActivity(finalS1, finalPackage_name);
            }
        });


    }


    public void openPasswordActivity(String finalSecret, String finalPackage_name){

        Intent intent = new Intent(this, PasswordActivity.class);
       intent.setAction(Intent.ACTION_SEND);
       intent.putExtra("Secret", finalSecret);
       intent.putExtra("Package_name", finalPackage_name);

        intent.setType("text/plain");
        startActivity(intent);
   }

    public void openSecretReconstructionActivity(String S1, String finalPackage_name){
        Intent intent = new Intent(this, SecretReconstructionActivity.class);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra("S1", S1);
        intent.putExtra("Package_name", finalPackage_name);
        intent.setType("text/plain");
        startActivity(intent);
    }

}
