package edu.aku.hassannaqvi.csvdownloader;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
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
import java.util.ArrayList;

import edu.aku.hassannaqvi.csvdownloader.core.DatabaseHelper;
import edu.aku.hassannaqvi.csvdownloader.core.MainApp;
import edu.aku.hassannaqvi.csvdownloader.models.Words;

import static edu.aku.hassannaqvi.csvdownloader.core.CreateTable.PROJECT_NAME;

public class DataDownWorkerALLPeriodic extends Worker {

    private static final Object APP_NAME = PROJECT_NAME;
    private final String TAG = "DataDownWorkerALLPeriod";

    // to be initialised by workParams
    private final Context mContext;
    private final String uploadTable;
    private final String uploadWhere;
    private final URL serverURL = null;
    private final String nTitle = "Enrolment";
    HttpURLConnection urlConnection;
    DatabaseHelper db;
    private String uploadColumns;
    private ProgressDialog pd;
    private int length;
    private Data data;
    private ArrayList<Words> allWords;


    public DataDownWorkerALLPeriodic(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
        uploadTable = workerParams.getInputData().getString("table");
        //uploadColumns = workerParams.getInputData().getString("columns");
        uploadWhere = workerParams.getInputData().getString("where");
        db = new DatabaseHelper(mContext); // Database Helper


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

        Log.d(TAG, "doWork: Starting");
        //displayNotification(nTitle, "Starting upload");

        StringBuilder result = new StringBuilder();

        URL url = null;
        try {
            if (serverURL == null) {
                url = new URL(MainApp._HOST_URL + MainApp._SERVER_GET_URL);
            } else {
                url = serverURL;
            }
            Log.d(TAG, "doWork: Connecting...");
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
            Log.d(TAG, "downloadURL: " + url);

            JSONArray jsonSync = new JSONArray();

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());

            JSONObject jsonTable = new JSONObject();
            JSONArray jsonParam = new JSONArray();

            jsonTable.put("table", uploadTable);
            //jsonTable.put("select", uploadColumns);
            jsonTable.put("filter", uploadWhere);
            jsonTable.put("limit", "3");
            jsonTable.put("orderby", "rand()");
            //jsonSync.put(uploadData);
            jsonParam
                    .put(jsonTable);
            // .put(jsonSync);

            Log.d(TAG, "Upload Begins: " + jsonTable.toString());


            wr.writeBytes(String.valueOf(jsonTable));
            wr.flush();
            wr.close();

            Log.d(TAG, "doInBackground: " + urlConnection.getResponseCode());

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Connection Response: " + urlConnection.getResponseCode());
                //displayNotification(nTitle, "Connection Established");

                length = urlConnection.getContentLength();
                Log.d(TAG, "Content Length: " + length);

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);

                }
                //displayNotification(nTitle, "Received Data");
                Log.d(TAG, "doWork(EN): " + result.toString());
            } else {

                Log.d(TAG, "Connection Response (Server Failure): " + urlConnection.getResponseCode());

                data = new Data.Builder()
                        .putString("error", String.valueOf(urlConnection.getResponseCode())).build();
                return Result.failure(data);
            }
        } catch (java.net.SocketTimeoutException e) {
            Log.d(TAG, "doWork (Timeout): " + e.getMessage());
            //displayNotification(nTitle, "Timeout Error: " + e.getMessage());
            data = new Data.Builder()
                    .putString("error", String.valueOf(e.getMessage())).build();
            return Result.failure(data);

        } catch (IOException | JSONException e) {
            Log.d(TAG, "doWork (IO Error): " + e.getMessage());
            //displayNotification(nTitle, "IO Error: " + e.getMessage());
            data = new Data.Builder()
                    .putString("error", String.valueOf(e.getMessage())).build();

            return Result.failure(data);

        } finally {
//            urlConnection.disconnect();
        }

        //Do something with the JSON string
        if (result != null) {
            //displayNotification(nTitle, "Starting Data Processing");

            //String json = result.toString();
            /*if (json.length() > 0) {*/
            //displayNotification(nTitle, "Data Size: " + result.length());


            // JSONArray jsonArray = new JSONArray(json);


            //JSONObject jsonObjectCC = jsonArray.getJSONObject(0);
            ///BE CAREFULL DATA.BUILDER CAN HAVE ONLY 1024O BYTES. EACH CHAR HAS 8 BYTES
            if (result.toString().length() > 10240) {
                data = new Data.Builder()
                        .putString("data", String.valueOf(result).substring(0, (10240 - 1) / 8)).build();
            } else {

                data = new Data.Builder()
                        .putString("data", String.valueOf(result)).build();
            }

            //displayNotification(nTitle, "Uploaded successfully");
            Log.d(TAG, "doWork: " + result);
            Log.d(TAG, "onChanged: SUCCEEDED");
            allWords = new ArrayList<Words>();

            //Displaying the status into TextView
            //mTextView1.append("\n" + workInfo.getState().name());


            String message = String.valueOf(result);
            StringBuilder sSyncedError = new StringBuilder();
            JSONObject jsonObject;
            try {

                JSONArray json = new JSONArray(message);
                allWords.clear();
                for (int i = 0; i < json.length(); i++) {
                    Words words = new Words();
                    if (!db.WordExists(new JSONObject(json.getString(i)).getString("id"))) {

                        db.syncWords(new JSONObject(json.getString(i)));
                        displayNotification("VocApp", "New Word: " + new JSONObject(json.getString(i)).getString("word"), new JSONObject(json.getString(i)).getInt("id"));

                    }
                    allWords.add(words.Hydrate(new JSONObject(json.getString(i))));
                    i = json.length();
                }
            } catch (JSONException e) {

                e.printStackTrace();

                data = new Data.Builder()
                        .putString("error", e.getMessage()).build();
                Log.d(TAG, "doWork: " + e.getMessage());

                return Result.failure(data);
            }
            //fupsAdapter = new FollowupsAdapter((List<FollowUps>) allWords, FollowUpsList.this);
            //recyclerView.setAdapter(fupsAdapter);

            //bi.s3.setText(allWords.get(0).getSentcol3());

        }


        return Result.success();

    }

    /*
     * The method is doing nothing but only generating
     * a simple notification
     * If you are confused about it
     * you should check the Android Notification Tutorial
     * */
    private void displayNotification(String title, String task, int id) {

        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(mContext, Vocabulary2Activity.class);

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("VOCAPP", "NEW_WORDS", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "VOCAPP")
                .setContentTitle(title)
                .setContentText(task)
                .setAutoCancel(true)
                .setColor(mContext.getResources().getColor(R.color.red))
                //.setContentText(mContext.getResources().getString(R.string.new_word))
                .setSmallIcon(R.drawable.ic_word);

        notification.setContentIntent(resultPendingIntent);

        final int maxProgress = 100;
        int curProgress = 0;
        /*    notification.setProgress(length, curProgress, false);*/

        notificationManager.notify(id, notification.build());
    }

 /*   static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager, int appWidgetId) {



        Intent intent = new Intent(context, UpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, 0);


        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget);
        remoteViews.setOnClickPendingIntent(R.id.LinearLayout01, pendingIntent);

        remoteViews.setTextViewText(R.id.widget_textview, text);

        // Tell the widget manager
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }*/
}