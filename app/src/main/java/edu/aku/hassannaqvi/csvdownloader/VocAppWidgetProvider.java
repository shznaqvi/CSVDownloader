package edu.aku.hassannaqvi.csvdownloader;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import edu.aku.hassannaqvi.csvdownloader.core.DatabaseHelper;
import edu.aku.hassannaqvi.csvdownloader.models.Words;

public class VocAppWidgetProvider extends AppWidgetProvider {

    static RemoteViews views;
    TextView synm;
    TextView word;
    private List<Words> allWords;
    private Context mContext;
    private DatabaseHelper db;
    private int[] appWidgetIds;
    private AppWidgetManager appWidgetManager;

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//Retrieve the time//


        //Construct the RemoteViews object//

        views = new RemoteViews(context.getPackageName(), R.layout.vocapp_widget);
        views.setTextViewText(R.id.word, String.valueOf(appWidgetId));

//Retrieve and display the time//

        //views.setTextViewText(R.id.word,);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://code.tutsplus.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.word, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        mContext = context;
        this.appWidgetIds = appWidgetIds;
        this.appWidgetManager = appWidgetManager;

        db = new DatabaseHelper(mContext);
        try {
            allWords = db.getAllWords(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int appWidgetId : appWidgetIds) {
            updateWord(appWidgetId);
        }
        // Perform this loop procedure for each App Widget that belongs to this provider
/*        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, Vocabulary2Activity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            views = new RemoteViews(context.getPackageName(), R.layout.vocapp_widget);
            views.setOnClickPendingIntent(R.id.button, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }*/
    }


    private void updateWord(int appWidgetId) {
        int i;
        //i = (int) (Math.random() * (allWords.size() - 1) + 1);
        i = 0;
        if (!allWords.isEmpty()) {
            // Log.d("ID", "onChanged: " + allWords.get(0).getId());
            //Date currentTime = Calendar.getInstance().getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss aaa", Locale.getDefault());
            String currentTime = sdf.format(new Date());
            //db.incrementViews(allWords.get(0).getId());

            int rnd = new Random().nextInt(allWords.size());

            views = new RemoteViews(mContext.getPackageName(), R.layout.vocapp_widget);

            views.setTextViewText(R.id.word, allWords.get(rnd).getWord());
            views.setTextViewText(R.id.synm, allWords.get(rnd).getTrans());
            views.setTextViewText(R.id.updateTime, currentTime);


            Intent intent = new Intent(mContext, Vocabulary2Activity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.word, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);

            Data data = new Data.Builder()
                    .putString("table", "words")
                    //.putString("columns", "_id, sysdate")
                    // .putString("where", where)
                    .build();
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            PeriodicWorkRequest saveRequest = new PeriodicWorkRequest.Builder(DataDownWorkerALLPeriodic.class, 15, TimeUnit.MINUTES).setInputData(data).setConstraints(constraints)
                    // Constraints
                    .build();

            WorkManager.getInstance().enqueueUniquePeriodicWork("Vocabulary2Activity", ExistingPeriodicWorkPolicy.KEEP, saveRequest);
        }
    }
}
