package edu.aku.hassannaqvi.csvdownloader;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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

public class DataWorker extends Worker {

    private static final Object APP_NAME = "GSED";
    private final String TAG = "GetUCs()";
    HttpURLConnection urlConnection;
    private Context mContext;
    private URL serverURL = null;
    private ProgressDialog pd;
    private int length;

    public DataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /*
     * This method is responsible for doing the work
     * so whatever work that is needed to be performed
     * we will put it here
     *
     * For example, here I am calling the method displayNotification()
     * It will display a notification
     * So that we will understand the work is executed
     * */

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "doInBackground: Starting");
        displayNotification("BL_Random", "Starting Sync");

        StringBuilder result = new StringBuilder();

        URL url = null;
        try {
            Log.d(TAG, "doInBackground: Trying...");
            if (serverURL == null) {
                url = new URL("https://vcoe1.aku.edu/covidmat/api/getdata.php");
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
                json.put("table", "bl_random");
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
                displayNotification("BL_Random", "Connection Established");

                length = urlConnection.getContentLength();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    Log.i(TAG, "UCs In: " + line);
                    result.append(line);
                    displayNotification("BL_Random", line);

                }
            }
        } catch (java.net.SocketTimeoutException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
            displayNotification("BL_Random", "Timeout Error: " + e.getMessage());
            return Result.failure();

        } catch (java.io.IOException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
            displayNotification("BL_Random", "IO Error: " + e.getMessage());

            return Result.failure();

        } finally {
//            urlConnection.disconnect();
        }

        Log.d(TAG, "onPostExecute: Starting");
        displayNotification("BL_Random", "Received Data");
        List<String[]> data = new ArrayList<String[]>();
        //Do something with the JSON string
        if (result != null) {
            displayNotification("BL_Random", "Starting Data Processing");

            String json = result.toString();
            if (json.length() > 0) {
                displayNotification("BL_Random", "Data Size: " + json.length());

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
                                jsonObjectCC.getString("_id"),
                                jsonObjectCC.getString("dist_id"),
                                jsonObjectCC.getString("dist_name"),
                                jsonObjectCC.getString("sub_dist_name"),
                                jsonObjectCC.getString("hhno"),
                                jsonObjectCC.getString("cluster"),

                        });

                    }
                    writer.writeAll(data); // data is adding to csv

                    writer.close();
                    //callRead();

                } catch (JSONException | IOException e) {
                    e.printStackTrace();

                }
            } else {

            }
        } else {

        }

        displayNotification("BL_Random", data.size() + " Records Downloaded Successfully");
        return Result.success();
    }

    /*
     * The method is doing nothing but only generating
     * a simple notification
     * If you are confused about it
     * you should check the Android Notification Tutorial
     * */
    private void displayNotification(String title, String task) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher);

        final int maxProgress = 100;
        int curProgress = 0;
        notification.setProgress(length, curProgress, false);

        notificationManager.notify(1, notification.build());
    }
}