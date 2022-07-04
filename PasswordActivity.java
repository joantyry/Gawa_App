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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class PasswordActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        // Button submit_button = (Button) findViewById(R.id.submit);


        //Get Secret
        Intent intent = getIntent();
        String Secret_s = intent.getStringExtra("Secret");
        String package_name = intent.getStringExtra("Package_name");
        BigInteger Secret = new BigInteger(Secret_s);

        //submit_button.setText(Secret_s);


        //create files
        File secret_file = new File(getFilesDir(), "Secret.json");

        File servers_file = new File(getFilesDir(), "Servers.json");

        File Ec_file = new File(getFilesDir(), "Ec_Value.json");

        File userValues_file = new File(getFilesDir(), "userValues_file.json");


        Random randNum = new Random();
        int len = 14;


        //Get file size. This was done after a complete setup

       /* int server_size = Integer.parseInt(String.valueOf(servers_file.length()));

        int Ec_size = Integer.parseInt(String.valueOf(Ec_file.length()));

        int [] file_size = {server_size, Ec_size};
*/



        //Read values from config file(config.properties)
 /*      String[] confNames ={"HomeServer_url","CloudServer1_url", "CloudServer2_url" };

        String[] confNames2 ={"HomeServer_userData","CloudServer1_userData", "CloudServer2_userData" };

        List<String> url= new ArrayList<String>();

        for(int i = 0; i< confNames.length; i++){

            url.add (Config.getConfigValue(this, confNames[i]));


        }

        List<String> url2= new ArrayList<String>();

        for(int i = 0; i< confNames2.length; i++){

            url2.add(Config.getConfigValue(this, confNames2[i]));


        }

        String Serv = Config.getConfigValue(this, "serverList");
        String[]  servers = Serv.split(",");

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


        String[] finalUrl = {HomeServer_url, CloudServer1_url, CloudServer2_url};
        String[] finalUrl1 = {HomeServer_userData, CloudServer1_userData, CloudServer2_userData};

        String[] servers = Server_List.split(",");


        EditText password = (EditText) findViewById(R.id.enter_password);
        EditText password2 = (EditText) findViewById(R.id.re_enter_password);

        Button submit_button = (Button) findViewById(R.id.submit);

        //submit_button.setText(Arrays.toString(file_size));

   submit_button.setOnClickListener(new View.OnClickListener() {
            String checkPassword = null;
            String pwd = null;

            @Override
                public void onClick (View v){

                long startTime = System.nanoTime();

               //Get the passwords the user enters
                String pwd = password.getText().toString();
                String pwd2 = password2.getText().toString();

                //Check if the two passwords match

                String checkPassword = checkPwd(pwd, pwd2);

                //Do in background

                AsyncTask<String, String, String> psss =  new  AsyncTask<String, String, String>(){

                    @Override
                    protected String doInBackground(String... strings) {
                        String Result = null;

                        if (checkPassword == "Matching") {
                            try {
                                Result = execSetup(pwd, Secret_s, String.valueOf(startTime));
                            } catch (NoSuchAlgorithmException | JSONException | KeyManagementException e) {
                                e.printStackTrace();
                            }


                        }
                        return Result;
                    }
                };

                psss.execute();

            }




                private String checkPwd(String pwd, String pwd2){
                String check = null;

                //check if the two passwords match
                if (!pwd2.equals(pwd)) {

                    password2.setTextColor(Color.RED);
                    Toast.makeText(PasswordActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    check = "Not_Matching";
                    //return check;


                } else {

                    submit_button.setText("Submitted");
                    submit_button.setBackgroundColor(Color.GRAY);

                    password.setTextColor(Color.TRANSPARENT);
                    password2.setTextColor(Color.TRANSPARENT);

                    check = "Matching";

                    //Toast.makeText(PasswordActivity.this, "Submitted", Toast.LENGTH_SHORT).show();




                }

                    return check;
                }



            private String execSetup(String pwd, String Secret_s, String startTime) throws NoSuchAlgorithmException, JSONException, KeyManagementException {

                //Generate SetupID


                BigInteger SetupID1 = new BigInteger(len, randNum);
                BigInteger SetupID2 = new BigInteger(len, randNum);


                String SetupID =  SetupID1.toString() + SetupID2.toString() ;



                String[] S1_s = execPart1(pwd, SetupID);
                //S1, sharex_time, SharePrTime, Ec_Time}
                    String S1_share = S1_s[0];
                    String sharex_Time= S1_s[1];
                    String sharePr_Time = S1_s[2];
                    String Ec_Time = S1_s[3];

                    //BigInteger S1 = new BigInteger(S1_s);
                    BigInteger S1 = new BigInteger(S1_s[0]);

                    String Secret_recon = execPart2(pwd, S1);
                    //String Message = SendWalletApp(Secret_recon, Secret_s, S1, pwd);
                 String Message = SendWalletApp( Secret_s, S1_share, pwd, Secret_recon, startTime,sharex_Time, sharePr_Time, Ec_Time );

                 StatusMessage(Message, SetupID);


                    return Message;

            }


            private String[] execPart1(String pwd, String SetupID) {
                String[] S1 = null;
                Part1 objPart1 = new Part1();

                try {
                    S1 = objPart1.Start( Secret,p,q,g, pwd, userValues_file, secret_file, Ec_file, servers_file, finalUrl,finalUrl1, servers, SetupID);
                } catch (JSONException | IOException | ParseException | KeyManagementException | NoSuchAlgorithmException | InterruptedException e) {
                    e.printStackTrace();
                }

                return S1;
            }



            private String execPart2(String pwd, BigInteger S1) {

                //Generating phaseID

                BigInteger phaseID1 = new BigInteger(len, randNum);
                BigInteger phaseID2 = new BigInteger(len, randNum);


                String phaseID = phaseID1 + phaseID1.toString();

                Part2 objPart2 = new Part2();
                String[] Secret_recon = null;


                try {
                    Secret_recon = objPart2.Recon_Secret(S1,p,q,g, pwd, Ec_file, servers_file, secret_file, userValues_file, finalUrl, finalUrl1, servers, phaseID);
                } catch (IOException | ParseException | InterruptedException | NoSuchAlgorithmException | JSONException | KeyManagementException e) { e.printStackTrace();
                }

                return Secret_recon[2];
            }



            private String SendWalletApp( String Secret_s, String S1, String pwd, String Secret_recon, String startTime, String sharex_Time, String sharePr_Time, String Ec_Time) throws NoSuchAlgorithmException, JSONException, KeyManagementException {

                String message = null;

                if(Secret_recon.equals(Secret_s)){
                        message = "Success!";

                        Intent intent = getPackageManager().getLaunchIntentForPackage(package_name);
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra("Name", "Setup");
                        intent.putExtra("S1", S1);
                        intent.putExtra("StartTime", startTime);
                        intent.putExtra("sharex_Time", sharex_Time);
                        intent.putExtra("sharePr_Time", sharePr_Time);
                        intent.putExtra("Ec_Time", Ec_Time);


                        intent.setType("text/plain");
                        startActivity(intent);




                   }


                else{
                    message = "Fail!";

                    execSetup(pwd, Secret_s, startTime);
                }

                return message;
            }

            private void StatusMessage(String Message, String SetupID) throws NoSuchAlgorithmException, JSONException, KeyManagementException {
                SetupStatus objStatus = new SetupStatus();

                for (int i = 0; i < 3; i++) {
                    objStatus.status(Message, finalUrl[i], servers[i], SetupID);

                }




            }

        });

    }

}

