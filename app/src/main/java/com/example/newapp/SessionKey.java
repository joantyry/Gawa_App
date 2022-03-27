package com.example.newapp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SessionKey {
    public BigInteger[][] BVtau;
    public int[] I;
    public BigInteger xtil;
    //public BigInteger Zq;

    public String[] keySecret(BigInteger S1, BigInteger[][] BVtau, BigInteger[] I, String[] url2, BigInteger xtil, BigInteger p, BigInteger q, File servers_file, File secret_file) throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException, InterruptedException {


        //Read Y_primes from file

        JSONParser parser = new JSONParser();
        File path1 = servers_file.getAbsoluteFile();
        JSONArray obj2 = new JSONArray();


        try {
            obj2 = (JSONArray) parser.parse(new FileReader(path1));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }


        JSONObject Y_values = new JSONObject();

        for (int i = 0; i < 3; i++) {

            JSONObject object = (JSONObject) obj2.get(i);
            String serverName = (String) object.get("src");
            BigInteger y_prime = new BigInteger(String.valueOf(object.get("y_prime"))) ;
            //String y_prime = (String) object.get("y_prime");
            Y_values.put(serverName, y_prime);
        }

        BigInteger home = (BigInteger) Y_values.get("HomeServer");
        BigInteger cloud1 = (BigInteger) Y_values.get("CloudServer1");
        BigInteger cloud2 = (BigInteger) Y_values.get("CloudServer2");


        //Check online servers and compute keys

        BigInteger[] arr = {I[0], I[1], BVtau[2][0], BVtau[2][1], BVtau[2][2]};

        BigInteger sum = BigInteger.valueOf(0);

        GetReconValues objSRV = new GetReconValues();
        JSONArray serShares = new JSONArray();
        JSONObject serShares1 = new JSONObject();


        for (int i = 0; i < 5; i++) {
            sum = (sum.add(arr[i])).mod(p);
        }

        BigInteger[] y_Prime = new BigInteger[0];

        SecretShare share1 = new SecretShare(0, BigInteger.valueOf(0));
        SecretShare share2 = new SecretShare(0, BigInteger.valueOf(0));


        //List<BigInteger> onlineServers = Arrays.asList(I);
        ArrayList<BigInteger> onlineServers = new ArrayList<>(2);
        onlineServers.add(I[0]);
        onlineServers.add(I[1]);


        GetOneServerValue objGOne = new GetOneServerValue();
        JSONObject getOne = new JSONObject();
        String[] urls = null;

        BigInteger home_key = null;
        BigInteger cloud1_key = null;
        BigInteger cloud2_key = null;

        if (onlineServers.contains(BigInteger.valueOf(1)) && onlineServers.contains(BigInteger.valueOf(2))) {
            //secret ="yes";

            y_Prime = new BigInteger[]{home, cloud1};

            home_key = (sum.add(y_Prime[0].modPow(xtil, p))).mod(p);


            cloud1_key = (sum.add(y_Prime[1].modPow(xtil, p))).mod(p);


            urls = new String[]{url2[0], url2[1]};

        } else if (onlineServers.contains(BigInteger.valueOf(1)) && onlineServers.contains(BigInteger.valueOf(3))) {


            y_Prime = new BigInteger[]{home, cloud2};

            home_key = (sum.add(y_Prime[0].modPow(xtil, p))).mod(p);

            cloud2_key = (sum.add(y_Prime[1].modPow(xtil, p))).mod(p);


            urls = new String[]{url2[0], url2[2]};

        } else {

            y_Prime = new BigInteger[]{cloud1, cloud2};

            cloud1_key = (sum.add(y_Prime[0].modPow(xtil, p))).mod(p);

            cloud2_key = (sum.add(y_Prime[1].modPow(xtil, p))).mod(p);


            urls = new String[]{url2[1], url2[2]};

        }


        for (int i = 0; i < 2; i++) {
            getOne = objGOne.getValue(urls[i]);

            //String Message_Name = (String) Nonce1.get("Name");
            int whi = 0;

            while (getOne.equals(null) || !String.valueOf(getOne.get("Name")).contentEquals("Reconstruction3")){

                Thread.sleep(100);
                getOne = objGOne.getValue(urls[i]);
                whi = whi + 1;

                if (whi == 500){
                    break;
                }
            }

            if (String.valueOf(getOne.get("Name")).contentEquals("Reconstruction3") ){

                serShares.add(getOne);
            }

          /*  String Name = (String) getOne.get("Name");

           if (!Name.equals("Reconstruction3")) {
                Thread.sleep(1000);
                getOne = objGOne.getValue(urls[i]);

            }


            serShares.add(getOne);*/


        }

        JSONArray DecValues = new JSONArray();

        //Decrypt Eshares and join shares using SecretSharing code format

        SecretShare share = null;

        ArrayList<SecretShare> Shares = new ArrayList<>(2);

        String Message = null;
        String[] Message2 = null;

        JSONObject Es = new JSONObject();
        JSONArray Ess = new JSONArray();

        for (int i = 0; i < 2; i++) {
            Es = (JSONObject) serShares.get(i);
            String src = (String) Es.get("src");

            String Eshare_str = String.valueOf(Es.get("EShare")) ;

            if (Eshare_str.equals("Wrong Password!")) {

                Message = "Wrong password";

            }

            else{

                BigInteger Eshare = new BigInteger(Eshare_str);

                if (src.equals("HomeServer")){

                    if (home_key == BigInteger.ZERO){
                       home_key = BigInteger.ONE;
                    }


                    BigInteger homeDec = home_key.add(Eshare).mod(p);
                   // BigInteger homeDec = Eshare.modPow(home_key, p);
                    share = new SecretShare(1, homeDec);
                }

                else if(src.equals("CloudServer1")){

                    if (cloud1_key == BigInteger.ZERO){
                        cloud1_key = BigInteger.ONE;
                    }

                    BigInteger cloud1Dec = cloud1_key.add(Eshare).mod(p);
                   // BigInteger cloud1Dec = Eshare.modPow(cloud1_key, p);
                    share = new SecretShare(2, cloud1Dec);
                }

                else{

                    if (cloud2_key == BigInteger.ZERO){
                        cloud2_key = BigInteger.ONE;
                    }

                    BigInteger cloud2Dec = cloud2_key.add(Eshare).mod(p);
                    //BigInteger cloud2Dec = Eshare.modPow(cloud2_key, p);
                    share = new SecretShare(3, cloud2Dec);

                }

                Shares.add(share);
                Message = "Correct password";
            }


        }



        if (Message.equals("Correct password")) {
            long startTime_Secret = System.nanoTime();


//Reconstruct S2

            SecretShare[] shares12 = new SecretShare[]{Shares.get(0), Shares.get(1)};

            BigInteger S2 = Shamir.combine(shares12, q);

//Read S1 from secret_file
           /* JSONParser parser2 = new JSONParser();
            File path2 = secret_file.getAbsoluteFile();
            JSONArray objS1 = (JSONArray) parser2.parse(new FileReader(path2));

            JSONObject objectS1 = (JSONObject) objS1.get(0);
            BigInteger S1 = BigInteger.valueOf((Long) objectS1.get("S1"));*/
           //Get S1 from dummy wallet

//Reconstruct the secret using S1 and S2
            SecretShare S1_s = new SecretShare(1, S1);
            SecretShare S2_s = new SecretShare(2, S2);

            SecretShare[] FinalShares = new SecretShare[]{S1_s, S2_s};

            BigInteger Secret = Shamir.combine(FinalShares, q);

            long totalTime_Secret = System.nanoTime() - startTime_Secret;

            Message2 = new String[]{Secret.toString(), String.valueOf(totalTime_Secret)};

        }

        else{

            Message2 = new String[]{Message, String.valueOf(0)};

        }


        return Message2;
       // return String.valueOf(Ess);

    }
}