package edu.aku.hassannaqvi.csvdownloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;

import java.util.concurrent.TimeUnit;


public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    private static final String TAG = "BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // BOOT_COMPLETED” start Service
        Log.w("boot_broadcast_poc", "starting PeriodicWorkRequest...");
        Data data = new Data.Builder()
                .putString("table", "words")
                //.putString("columns", "_id, sysdate")
                // .putString("where", where)
                .build();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest saveRequest = new PeriodicWorkRequest.Builder(DataDownWorkerALLPeriodic.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
                TimeUnit.MILLISECONDS).setInputData(data).setConstraints(constraints)
                // Constraints
                .build();
        Toast.makeText(context, "NotifyingDailyService", Toast.LENGTH_LONG).show();
        Log.i(TAG, "NotifyingDailyService");
    }
}