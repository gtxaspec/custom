package com.gtx.jy.custom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import static java.lang.Runtime.getRuntime;

import android.os.Bundle;
import android.os.Handler;
import java.io.*;
import android.widget.*;
import static java.lang.Runtime.getRuntime;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.util.Log;
import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import java.util.Timer;
import java.util.TimerTask;
import android.view.*;

public class splash extends AppCompatActivity {
    private boolean isRootGiven;
    TextView tv7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        tv7 = (TextView) findViewById(R.id.TextView7);


        Thread th = new Thread() {
            public void run() {
                try {
                    isRootGiven();
                    sleep(2000);

              //  } catch (InterruptedException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (isRootGiven() == true) {
                        Log.d("CUSTOM:", "root is TURE!");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv7.setText("Root Granted!");
                            }
                        });
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                            Log.d("CUSTOM:", "root is FALSE");
                        //display text that root must be run

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv7.setText("You must grant Root Privileges in order to use this program.");
                            }
                        });



                        }
                    }
                }
        };


        th.start();

    }




    public static boolean isRootAvailable(){
        for(String pathDir : System.getenv("PATH").split(":")){
            if(new File(pathDir, "su").exists()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRootGiven(){
        if (isRootAvailable()) {
            Process process = null;
            try {
                Log.d("CUSTOM:", "testing for ROOT");
                process = Runtime.getRuntime().exec(new String[]{"su", "-c", "id"});
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String output = in.readLine();
                if (output != null && output.toLowerCase().contains("uid=0"))
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (process != null)
                    process.destroy();
            }
        }

        return false;
    }

}