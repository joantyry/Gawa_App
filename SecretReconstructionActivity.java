package com.example.newapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class SecretReconstructionActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_reconstruction);

        //final TextView text = findViewById(R.id.trial);

        //Get S1
        Intent intent = getIntent();
        String S1_s = intent.getStringExtra("S1");
        String package_name = intent.getStringExtra("Package_name");

        BigInteger S1 = new BigInteger(S1_s);



        EditText recon_password = (EditText) findViewById(R.id.reconstruct_password);
        CircularProgressButton submit = (CircularProgressButton) findViewById(R.id.submit);



        //Read values from config file(config.properties)
    /*    String[] confNames = {"HomeServer_url", "CloudServer1_url", "CloudServer2_url"};

        String[] confNames2 = {"HomeServer_userData", "CloudServer1_userData", "CloudServer2_userData"};

        List<String> url = new ArrayList<String>();

        for (int i = 0; i < confNames.length; i++) {

            url.add(Config.getConfigValue(this, confNames[i]));


        }

        List<String> url2 = new ArrayList<String>();

        for (int i = 0; i < confNames2.length; i++) {

            url2.add(Config.getConfigValue(this, confNames2[i]));


        }

        String Serv = Config.getConfigValue(this, "serverList");
        String[] servers = Serv.split(",");


        String[] finalUrl = url.toArray(new String[0]);
        String[] finalUrl1 = url2.toArray(new String[0]);

*/
        //Get p,q,g and config values
        Resources res = getResources();

        BigInteger p = new BigInteger(res.getString(R.string.p));
        BigInteger q = new BigInteger(res.getString(R.string.q));
        BigInteger g = new BigInteger(res.getString(R.string.g));



        String HomeServer_url = res.getString(R.string.HomeServer_url);
        String CloudServer1_url = res.getString(R.string.CloudServer1_url);
        String CloudServer2_url = res.getString(R.string.CloudServer2_url);


        String HomeServer_userData = res.getString(R.string.HomeServer_userData);
        String CloudServer1_userData = res.getString(R.string.CloudServer1_userData);
        String CloudServer2_userData = res.getString(R.string.CloudServer2_userData);

        String Server_List = res.getString(R.string.serverList);


        String[] finalUrl = {HomeServer_url,CloudServer1_url, CloudServer2_url};
        String[] finalUrl1 = {HomeServer_userData,CloudServer1_userData, CloudServer2_userData};

        String[]  servers = Server_List.split(",");




        //Generating phaseID
        Random randNum = new Random();
        int len = 14;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        String r = dtf.format(now);

        BigInteger phaseID1 = new BigInteger(len, randNum);


        String phaseID = r + phaseID1.toString();




        File secret_file = new File(getFilesDir(), "Secret.json");

        File servers_file = new File(getFilesDir(), "Servers.json");

        File Ec_file = new File(getFilesDir(), "Ec_Value.json");

        File userValues_file = new File(getFilesDir(), "userValues_file.json");

/*
        String[] Secret = new String[]{};

        Part2 objRecon = new Part2();
        String pwd = "pass";

        try {
            Secret = objRecon.Recon_Secret(S1,p,q,g, pwd, Ec_file, servers_file, secret_file, userValues_file, finalUrl, finalUrl1, servers, phaseID);
        } catch (IOException | ParseException | InterruptedException | NoSuchAlgorithmException | JSONException | KeyManagementException e) { e.printStackTrace();
        }*/




       submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long startTime = System.nanoTime();

                recon_password.setTextColor(Color.TRANSPARENT);
                submit.startAnimation();

                //button.setBackgroundColor(Color.GRAY);


                String pwd = recon_password.getText().toString();

                Part2 objRecon = new Part2();
                // BigInteger Secret= null;




              AsyncTask<String, String, String[]> recon =  new  AsyncTask<String, String, String[]>(){

                    @Override
                    protected String[] doInBackground(String... strings) {
                        //String StartTime_Recon = String.valueOf(startTime);
                        String[] Secret = new String[]{};

                        try {
                            Secret = objRecon.Recon_Secret(S1,p,q,g, pwd, Ec_file, servers_file, secret_file, userValues_file, finalUrl, finalUrl1, servers, phaseID);
                        } catch (IOException | ParseException | InterruptedException | NoSuchAlgorithmException | JSONException | KeyManagementException e) { e.printStackTrace();
                        }



                        return Secret;

                        }


                    @Override
                    protected void onPostExecute(String[] s) {
                        String StartTime_Recon = String.valueOf(startTime);

                        if(s.length==0){

                            submit.stopAnimation();
                           // submit.setClickable(true);
                            //submit.set;
                            submit.setText("An error occurred, please try again later!");



                        }
                        else{
                            submit.stopAnimation();
                            //submit.doneLoadingAnimation(Color.parseColor("#333639"), BitmapFactory.de);
                            Intent intent = getPackageManager().getLaunchIntentForPackage(package_name);
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra("Name", "SecretRecon");
                            intent.putExtra("SecretRecon", s[2]);
                            intent.putExtra("StartTime", StartTime_Recon);
                            intent.putExtra("BVTauTime", s[1]);
                            intent.putExtra("SecretTime", s[0]);
                            intent.setType("text/plain");

                            startActivity(intent);


                        }
                    }
                };


                recon.execute();

                //submit.stopAnimation();

            }

        });




            //submit.setText(Arrays.toString(Secret));



    }
}