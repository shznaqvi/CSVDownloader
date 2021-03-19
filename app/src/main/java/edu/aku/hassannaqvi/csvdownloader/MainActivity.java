package edu.aku.hassannaqvi.csvdownloader;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edu.aku.hassannaqvi.csvdownloader.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private static final String APP_NAME = "GSED";
    ActivityMainBinding bi;
    Cipher cipher;
    /* final static Base64.Encoder encorder = Base64.getEncoder();
     final static Base64.Decoder decorder = Base64.getDecoder();*/
    SecureRandom secureRandom;

    public static String encrypt(String plain) {
        try {
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec("asSa%s|n'$ crEed".getBytes(StandardCharsets.UTF_8), "AES"), new IvParameterSpec(iv));
            byte[] cipherText = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));

            byte[] ivAndCipherText = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, ivAndCipherText, 0, iv.length);
            System.arraycopy(cipherText, 0, ivAndCipherText, iv.length, cipherText.length);
            return Base64.encodeToString(ivAndCipherText, Base64.NO_WRAP);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String computeHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        byte[] byteData = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
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

    public static String decrypt(String encoded) {
        try {
            byte[] ivAndCipherText = Base64.decode(encoded, Base64.NO_WRAP);
            byte[] iv = Arrays.copyOfRange(ivAndCipherText, 0, 16);
            byte[] cipherText = Arrays.copyOfRange(ivAndCipherText, 16, ivAndCipherText.length);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec("asSa%s|n'$ crEed".getBytes(StandardCharsets.UTF_8), "AES"), new IvParameterSpec(iv));
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
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

        bi = DataBindingUtil.setContentView(this, R.layout.activity_main);


        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
        MainApp.child = new Child();
        String str = null;
        try {
            str = computeHash("password");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.d("TAG", "onCreate: " + str);
        Log.d("TAG", "onCreate: []" + decrypt("OGVQmk4qRZxX/JW9juQ0V43jU42zRBDbvwToSmJtiIE="));
        Log.d("TAG", "onCreate test: " + str.equals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8".toLowerCase()));
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
        JSONArray jsonParam = null;
        try {
            JSONObject jsonTable = new JSONObject();
            JSONObject jsonData = new JSONObject();
            JSONArray uploadData = new JSONArray();

            jsonData.put("username", "test1");
            jsonData.put("password", "test1");
            jsonData.put("dist_id", "901");
            jsonData.put("full_name", "test1");
            jsonData.put("auth_level", "0");
            jsonData.put("enabled", "1");
            uploadData.put(jsonData);
            jsonParam = new JSONArray();


            jsonTable.put("table", "users");

            jsonParam
                    .put(jsonTable)
                    .put(uploadData);

            Log.d("TAG", "Upload Begins: " + jsonTable.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String plaintext = jsonParam.toString();
        secureRandom = new SecureRandom();
        byte[] key = "asSa%s|n'$ crEed".getBytes();
        secureRandom.nextBytes(key);
        //SecretKey secretKey = new SecretKeySpec(key, "AES");
        //SecretKey secretKey = "asSa%s|n'$ crEed";


        String aadd = "From: <deviceid>, To: vcoe1.aku.edu";

            /*  cipher = Cipher.getInstance("AES/GCM/NoPadding");

            byte[] iv = new byte[cipher.getBlockSize()]; //NEVER REUSE THIS IV WITH SAME KEY
            secureRandom.nextBytes(iv);

            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv); //256 bit auth tag length
            Log.d("TAG", "onCreate: key"+secretKey);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            //cipher.updateAAD(aadd.getBytes());

            byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

           String encryptedText = Base64.encodeToString(iv, Base64.NO_WRAP) + Base64.encodeToString(cipherText, Base64.NO_WRAP);
            Toast.makeText(this, encryptedText, Toast.LENGTH_LONG + Toast.LENGTH_LONG).show();
            Log.d("TAG", "onCreate: encryptedText: " + encryptedText);

          *//*  return String.format("%s%s%s", new String(Base64.encode(iv)),
                    DELIMITER, new String(Base64.encode(cipherText)));*//*
         */
        //  byte[] key = "asSa%s|n'$ crEed".getBytes();
        SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
        Log.d("TAG", "onCreate: CBC-En " + encrypt(plaintext));
        Log.d("TAG", "onCreate: CBC-De " + decrypt("caFfG0AT4q7I9wjOleORJgoexKyhJ+DlEst5q+fG4oDkjvatGupxbLaGrxoNCL+EB3G0jYjh/L73JE4VfMyGXJVuSC2pFXSmHpD8xZpiPOA="));


    }

    public void checkRoot(View view) {
        try {
            Runtime.getRuntime().exec("su");
            Toast.makeText(this, "Rooted Device!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}