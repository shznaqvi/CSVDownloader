package edu.aku.hassannaqvi.csvdownloader;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String APP_NAME = "GSED";
    //tivityMainBinding bi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        //i = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
        MainApp.child = new Child();
        String str = null;
        try {
            str = computeHash("Password");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.d("TAG", "onCreate: " + str);
        Log.d("TAG", "onCreate test: " + str.equals("E7CF3EF4F17C3999A94F2C6F612E8A888E5B1026878E4E19398B23BD38EC221A".toLowerCase()));
        // bi.setChild(MainApp.child);

       /* TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei="";
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            imei=telephonyManager.getImei();
        }
        else
        {
            imei=telephonyManager.getDeviceId();
        }*/
        //Toast.makeText(this, getDeviceId(this), Toast.LENGTH_SHORT).show();
    }

    public String computeHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        byte[] byteData = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        Log.d("TAG", "computeHash: bytetostring" + byteData.toString());
        Log.d("TAG", "computeHash: byte test" + byteData.equals("0xE7CF3EF4F17C3999A94F2C6F612E8A888E5B1026878E4E19398B23BD38EC221A"));
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        Log.d("TAG", "computeHash: " + sb.toString());
        return sb.toString();
    }
/*    public static String getDeviceId(Context context) {

        String deviceId;


            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }


        return deviceId;
    }*/


    public void downloadCSV(View view) {


            new getDataCsv(this).execute();

    }


    public void downloadPrepop(View view) {
        //      bi.serverURL.setError(null);


        new getEligibilityCsv(this).execute();

    }

   /* public void getChild(View view) {

        if (bi.dssid.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a valid Parents_Study_ID", Toast.LENGTH_SHORT).show();
        } else {

            new getDataCsv(this, bi.dssid.getText().toString()).execute();


        }

    }*/

    public void setThis(View view) {
        //bi.gsedid.setText(MainApp.child.getGsedId());
        MainApp.child.setGsedId("99999999");

    }


}