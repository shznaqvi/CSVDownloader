package edu.aku.hassannaqvi.csvdownloader;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class getDataCsv extends AsyncTask<String, String, String> {

    private static final Object APP_NAME = "GSED";
    private final String TAG = "GetUCs()";
    HttpURLConnection urlConnection;
    private Context mContext;
    private URL serverURL = null;
    private ProgressDialog pd;

    public getDataCsv(Context context) {
        mContext = context;
    }

    public getDataCsv(Context context, URL url) {
        mContext = context;
        serverURL = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(mContext);
        pd.setTitle("Syncing UCs");
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
                url = new URL("http://43.245.131.159:8080/dss/api/getdata.php");
            } else {
                url = serverURL;
            }
            urlConnection = (HttpURLConnection) url.openConnection();
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
                json.put("table", "childlist");
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
                    Log.i(TAG, "UCs In: " + line);
                    result.append(line);
                }
            }
        } catch (java.net.SocketTimeoutException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
            return null;
        } catch (java.io.IOException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());

            return null;
        } finally {
//            urlConnection.disconnect();
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute: Starting");

        //Do something with the JSON string
        if (result != null) {
            String json = result;
            if (json.length() > 0) {
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    CSVWriter writer = null;
                    //String csv = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/childlist.csv"); // Here csv file name is MyCsvFile.csv

                    File csvFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + APP_NAME);

                    if (!csvFolder.exists()) {
                        csvFolder.mkdirs();
                    }

                    File csvFile = new File(csvFolder + File.separator + "childlist.csv");

                    writer = new CSVWriter(new FileWriter(csvFile));
                    List<String[]> data = new ArrayList<String[]>();
                    /*
                    // SAMPLE DATA FROM SERVER
                                        {
                                                "gsedid": "0081",
                                                "child_name": "FOKAIHA",
                                                "mother_name": "SABIRA",
                                                "dob": "19-05-2004"
                                        },
                    */
                    data.add(new String[]{"gsedid", "child_name", "mother_name", "dob"});
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectCC = jsonArray.getJSONObject(i);
                        data.add(new String[]{
                                jsonObjectCC.getString("gsedid"),
                                jsonObjectCC.getString("child_name"),
                                jsonObjectCC.getString("mother_name"),
                                jsonObjectCC.getString("dob"),

                        });

                    }
                    writer.writeAll(data); // data is adding to csv

                    writer.close();
                    //callRead();
                    pd.setTitle("Success");
                    pd.setMessage("CSV File saved successfully.");
                    pd.show();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    pd.setTitle("Error");
                    pd.setMessage(e.getMessage());
                    pd.show();
                }
            } else {
                pd.setMessage("Received: " + json.length() + "");
                pd.show();
            }
        } else {
            pd.setTitle("Connection Error");
            pd.setMessage("Server not found!");
            pd.show();
        }
    }

}