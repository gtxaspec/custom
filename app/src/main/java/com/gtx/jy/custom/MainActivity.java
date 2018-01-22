package com.gtx.jy.custom;


        import android.support.v7.app.AppCompatActivity;
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


public class MainActivity extends AppCompatActivity {

    TextView tv;
    TextView tv2;



    Button button1, button2, button3, button4, button5, button6, button7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //delete old files on app launch
        File file0 = new File("/data/data/com.gtx.jy.custom/install.sh");
        File file1 = new File("/data/data/com.gtx.jy.custom/restore.sh");
        File file2 = new File("/data/data/com.gtx.jy.custom/5009_rec_signed.zip");
        File file3 = new File("/data/data/com.gtx.jy.custom/p10_stock_rec.img");
        File file4 = new File("/data/data/com.gtx.jy.custom/recovery_new_keys.img");
        File file5 = new File("/data/data/com.gtx.jy.custom/reboot_custom.sh");
        File file6 = new File("/data/data/com.gtx.jy.custom/verify.sh");
        File file7 = new File("/cache/p10.img");
        File file8 = new File("/cache/p10_2.img");
        File file9 = new File("/cache/p10_3.img");
        File file10 = new File("/cache/p10_verify.img");
        File file11 = new File("/cache/recovery");
        File file12 = new File("/data/data/com.gtx.jy.custom/reboot_stock.sh");
        File file13 = new File("/data/data/com.gtx.jy.custom/openrecoveryscript_stock");
        File file14 = new File("/data/data/com.gtx.jy.custom/openrecoveryscript_custom");







        if (file0.exists())
            file0.delete();
        if (file1.exists())
            file1.delete();
        if (file2.exists())
            file2.delete();
        if (file3.exists())
            file3.delete();
        if (file4.exists())
            file4.delete();
        if (file5.exists())
            file5.delete();
        if (file6.exists())
            file6.delete();
        if (file7.exists())
            file7.delete();
        if (file8.exists())
            file8.delete();
        if (file9.exists())
            file9.delete();
        if (file10.exists())
            file10.delete();
        if (file11.exists())
            file11.delete();
        if (file12.exists())
            file12.delete();
        if (file13.exists())
            file13.delete();
        if (file14.exists())
            file14.delete();


        // setContentView(R.layout.activity_splash);
        // Intent intent = new Intent(this, splash.class);
        //  startActivity(intent);

        try {
            getRoot();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView4);

        //  tv.setText("Output :"+"\n"+runAsRoot());

        button1 = (Button) findViewById(R.id.button1);//get id of button 1
        button2 = (Button) findViewById(R.id.button2);//get id of button 2
        button3 = (Button) findViewById(R.id.button3);//get id of button 2
        button4 = (Button) findViewById(R.id.button4);//get id of button 2
        button5 = (Button) findViewById(R.id.button5);//get id of button 2
        button6 = (Button) findViewById(R.id.button6);//get id of button 2
        button7 = (Button) findViewById(R.id.button7);//get id of button 2


        button1.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                //tv.setText("Output :"+"\n"+runAsRoot());
                tv2.setText("Verify current user!");
                tv.setText("Cmd Output :" + "\n" + sudoForResult("whoami"));
            }
        });

        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                tv2.setText("Verifying bundled recovery file integrity...");

                copyFileOrDir("install.sh");
                copyFileOrDir("p10_stock_rec.img");
                copyFileOrDir("recovery_new_keys.img");
                sudoForResult("chmod 755 /data/data/com.gtx.jy.custom/install.sh");

                tv2.setText("Installing Custom Recovery...");

                tv.setText("Cmd Output :" + "\n" + sudoForResult("/data/data/com.gtx.jy.custom/install.sh"));

            }
        });


        button3.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                tv2.setText("Verifying bundled recovery file integrity...");

                copyFileOrDir("restore.sh");
                copyFileOrDir("p10_stock_rec.img");
                copyFileOrDir("recovery_new_keys.img");
                sudoForResult("chmod 755 /data/data/com.gtx.jy.custom/restore.sh");

                tv2.setText("Restoring stock recovery...");

                tv.setText("Cmd Output :" + "\n" + sudoForResult("/data/data/com.gtx.jy.custom/restore.sh"));
            }
        });
//stock
        button4.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                copyFileOrDir("reboot_stock.sh");
                copyFileOrDir("5009_rec_signed.zip");
                copyFileOrDir("openrecoveryscript_stock");

                sudoForResult("chmod 755 /data/data/com.gtx.jy.custom/reboot_stock.sh");
                tv.setText("Cmd Output :" + "\n" + sudoForResult("/data/data/com.gtx.jy.custom/reboot_stock.sh"));
            }
        });

        button5.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                copyFileOrDir("verify.sh");;

                tv2.setText("Verifying currently installed recovery...");

                sudoForResult("chmod 755 /data/data/com.gtx.jy.custom/verify.sh");
                tv.setText("Cmd Output :" + "\n" + sudoForResult("/data/data/com.gtx.jy.custom/verify.sh"));
            }
        });

        button6.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                sudoForResult("rm -rf /cache/recovery");
                finish();
            }
        });
//custom
        button7.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                copyFileOrDir("reboot_custom.sh");
                copyFileOrDir("5009_rec_signed.zip");
                copyFileOrDir("openrecoveryscript_custom");
                sudoForResult("chmod 755 /data/data/com.gtx.jy.custom/reboot_custom.sh");
                tv.setText("Cmd Output :" + "\n" + sudoForResult("/data/data/com.gtx.jy.custom/reboot_custom.sh"));
            }
        });



    }



    public static String sudoForResult(String... strings) {
        String res = "";
        DataOutputStream outputStream = null;
        InputStream response = null;
        try {
            Process su = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(su.getOutputStream());
            response = su.getInputStream();

            for (String s : strings) {
                outputStream.writeBytes(s + "\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            res = readFully(response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Closer.closeSilently(outputStream, response);
        }
        return res;
    }

    public static String readFully(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString("UTF-8");
    }


//copy files code to /data/data/com.gtx.jy.custom/

    private void copyFileOrDir(String path) {
        AssetManager assetManager = this.getAssets();
        String assets[] = null;
        try {
            assets = assetManager.list(path);
            if (assets.length == 0) {
                copyFile(path);
            } else {
                String fullPath = "/data/data/" + this.getPackageName() + "/" + path;
                File dir = new File(fullPath);
                if (!dir.exists())
                    dir.mkdir();
                for (int i = 0; i < assets.length; ++i) {
                    copyFileOrDir(path + "/" + assets[i]);
                }
            }
        } catch (IOException ex) {
            Log.e("tag", "I/O Exception", ex);
        }
    }

    private void copyFile(String filename) {
        AssetManager assetManager = this.getAssets();

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            String newFileName = "/data/data/" + this.getPackageName() + "/" + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }


    ///old


    private void getRoot() throws Exception {
        // Executes the command.
        Process p = getRuntime().exec("su");

    }
}


/*
        public String runAsRoot() {

        try {
            // Executes the command.
            //Process process = Runtime.getRuntime().exec("ls -l");
            //Process p = Runtime.getRuntime().exec("su -c mkdir /system/test");
            Process process = getRuntime().exec("whoami");

            // Reads stdout.
            // NOTE: You can write to stdin of the command using
            //       process.getOutputStream().
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();

            // Waits for the command to finish.
            process.waitFor();

            return output.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }




}

*/