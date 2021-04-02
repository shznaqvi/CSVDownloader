package edu.aku.hassannaqvi.csvdownloader;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;


public class getDataUsers extends AsyncTask<String, String, String> {

    private static final Object APP_NAME = "GSED";
    private final String TAG = "GetCSVs()";
    private final Context mContext;
    HttpsURLConnection urlConnection;
    private String dssid = null;
    private URL serverURL = null;
    private ProgressDialog pd;

    public getDataUsers(Context context) {
        mContext = context;
    }

    public getDataUsers(Context context, String dssid) {
        mContext = context;
        this.dssid = dssid;
    }

    public getDataUsers(Context context, URL url) {
        mContext = context;
        serverURL = url;
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
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(mContext);
        pd.setTitle("Syncing All CSVs");
        pd.setMessage("Getting connected to server...");
        pd.show();
        Log.d(TAG, "onPreExecute: Starting");
    }

    @Override
    protected String doInBackground(String... args) {
        Log.d(TAG, "doInBackground: Starting");
        StringBuilder result = new StringBuilder();

        URL url = null;
        try {
            Log.d(TAG, "doInBackground: Trying...");
            if (serverURL == null) {
                url = new URL("https://vcoe1.aku.edu/tpvics/api/getdataenc.php");
                //  url = new URL("http://3.249.206.243/aku/api/getdata.php");
            } else {
                url = serverURL;
            }
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(100000 /* milliseconds */);
            urlConnection.setConnectTimeout(150000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setUseCaches(false);
            urlConnection.connect();

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            JSONObject json = new JSONObject();
            try {
                json.put("table", "users");

                Log.d(TAG, "json.put: Done");
            } catch (JSONException e1) {
                e1.printStackTrace();
                Log.d(TAG, e1.getMessage());
            }
            Log.d(TAG, "downloadUrl: " + json.toString());
            wr.writeBytes(json.toString());
            wr.flush();
            wr.close();
            Log.d(TAG, "doInBackground: " + urlConnection.getResponseCode());
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    Log.i(TAG, "CSVs In: " + line);
                    result.append(line);
                }
            }
        } catch (java.net.SocketTimeoutException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());

            return null;
        } finally {
//            urlConnection.disconnect();
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String results) {
        Log.d(TAG, "onPostExecute: Encrypted: " + results);
        Toast.makeText(mContext, results, Toast.LENGTH_SHORT).show();
        String result = decrypt(results);
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onPostExecute: Decrypted: " + result);
        //Do something with the JSON string
        if (result != null) {
            if (result.length() > 2) {
                String json = result;
                if (json.length() > 0) {

                    Log.d(TAG, "onPostExecute: " + result);

                }
            } else {

                pd.setTitle("Data Error");
                pd.setMessage("Record not found!");
                pd.show();
            }
        } else {
            pd.setTitle("Connection Error");
            pd.setMessage("Server not found!");
            pd.show();
        }
    }
}