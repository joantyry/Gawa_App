package com.example.newapp;


import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


//From Stack Overflow
    public final class Config {

        private static final String TAG = "Helper";

        public static String getConfigValue(Context context, String name) {
            Resources resources = context.getResources();

            try {
                InputStream rawResource = resources.openRawResource(R.raw.config);
                Properties properties = new Properties();
                properties.load(rawResource);
                return properties.getProperty(name);
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Unable to find the config file: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "Failed to open config file.");
            }

            return null;
        }



        }





