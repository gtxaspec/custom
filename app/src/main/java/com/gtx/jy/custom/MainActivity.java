package com.gtx.jy.custom;

import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.os.Handler;
        import java.io.*;

import android.view.Window;
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

public class MainActivity extends AppCompatActivity {

    TextView tv, tv2;
    Button button1, button2, button3, button4, button5, button6, button7, button8;
    Handler handler;
    Timer timer=new Timer();//Used for a delay to provide user feedback

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView4);
       // getRoot();
        delete_stale();
        extract_files();
        set_permissions();
    }


   /* public void getRoot(){
        try {
            Process p = getRuntime().exec("su");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void extract_files() {
        copyFileOrDir("custom.sh");
        copyFileOrDir("openrecoveryscript_stock");
    }

    public void set_permissions() {
        sudoForResult("chmod 755 /data/data/com.gtx.jy.custom/custom.sh");
    }

    public void delete_stale() {
        //delete stale files on app launch
        File file0 = new File("/data/data/com.gtx.jy.custom/custom.sh");
        File file1 = new File("/data/data/com.gtx.jy.custom/5009_rec_signed.zip");
        File file2 = new File("/data/data/com.gtx.jy.custom/p10_stock_rec.img");
        File file3 = new File("/data/data/com.gtx.jy.custom/recovery_new_keys.img");
        File file4 = new File("/data/data/com.gtx.jy.custom/openrecoveryscript_stock");

        if (file0.exists()) file0.delete();
        if (file1.exists()) file1.delete();
        if (file2.exists()) file2.delete();
        if (file3.exists()) file3.delete();
        if (file4.exists()) file4.delete();
    }


    public void InstalCustomClick(View arg0) {
        //install custom recovery
                tv2.setText(R.string.Verifying_bundled_recovery_file_integrity);
                tv2.setText(R.string.Installing_Custom_Recovery);
                sudoForResult("chmod 755 /data/data/com.gtx.jy.custom/custom.sh");
                copyFileOrDir("recovery_new_keys.img");
                copyFileOrDir("p10_stock_rec.img");
                tv.setText(R.string.gen_message_Console_Output + "\n" + sudoForResult("/data/data/com.gtx.jy.custom/custom.sh install_custom_recovery"));
    }

    public void VerifyUserClick(View arg0) {
        //Verify which user the app is running as, useful to see if root was successfully granted
        // tv.setText("Output :"+"\n"+runAsRoot());
                tv2.setText(R.string.Verify_current_user);
                tv.setText(R.string.gen_message_Console_Output + "\n" + sudoForResult("whoami"));
            }


    public void RestoreStockRecoveryClick(View arg0) {
        //restore stock recovery
                tv2.setText("@string/Verifying_bundled_recovery_file_integrity");
                tv2.setText(R.string.Restoring_stock_recovery);
                sudoForResult("chmod 755 /data/data/com.gtx.jy.custom/custom.sh");
                copyFileOrDir("recovery_new_keys.img");
                copyFileOrDir("p10_stock_rec.img");
                tv.setText(R.string.gen_message_Console_Output + "\n" + sudoForResult("/data/data/com.gtx.jy.custom/custom.sh restore_stock_recovery"));

    }


    public void InstallGTXRomClick(View arg0) {
        //reboot and begin installation of modified STOCK rom
                Switch dataswitch = findViewById(R.id.switch1);
                boolean noDataEnabled = dataswitch.isChecked();
                if(noDataEnabled) {
                    sudoForResult("chmod 755 /data/data/com.gtx.jy.custom/custom.sh");
                    tv.setText(R.string.gen_message_Console_Output + "\n" + sudoForResult("/data/data/com.gtx.jy.custom/custom.sh touch_upgrade"));
                    //Log.d("CUSTOM:", "ONLY SHOW WHEN TRUE:" + noDataEnabled);
                }
                sudoForResult("chmod 755 /data/data/com.gtx.jy.custom/custom.sh");
                copyFileOrDir("5009_rec_signed.zip");
                tv.setText(R.string.gen_message_Console_Output + "\n" + sudoForResult("/data/data/com.gtx.jy.custom/custom.sh reboot_custom_gtx"));
    }

    private Handler mHandler = new Handler();

    public void VerifyRecoveryClick(View arg0) {


//verify which recovery is currently installed in the users jy unit
        tv2.setText(R.string.Verifying_currently_installed_recovery);


        sudoForResult("chmod 755 /data/data/com.gtx.jy.custom/custom.sh");
                copyFileOrDir("recovery_new_keys.img");
                copyFileOrDir("p10_stock_rec.img");
                tv.setText(R.string.gen_message_Console_Output + "\n" + sudoForResult("/data/data/com.gtx.jy.custom/custom.sh verify_custom_recovery"));





    }

    public void ExitClick(View arg0) {
                sudoForResult("chmod 755 /data/data/com.gtx.jy.custom/custom.sh");
                sudoForResult("rm -rf /cache/recovery");
                finish();
    }

    public void RebootTWRPClick(View arg0) {
        //reboot to custom recovery, if installed, load into TWRP for system maintenance
                sudoForResult("chmod 755 /data/data/com.gtx.jy.custom/custom.sh");
                copyFileOrDir("5009_rec_signed.zip");
                tv.setText(R.string.gen_message_Console_Output + "\n" + sudoForResult("/data/data/com.gtx.jy.custom/custom.sh reboot_to_twrp"));
    }


    //run commands as superuser
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


//copy temp files code to /data/data/com.gtx.jy.custom/

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



}

