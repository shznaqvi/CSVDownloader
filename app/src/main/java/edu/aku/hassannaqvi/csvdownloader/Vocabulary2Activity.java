package edu.aku.hassannaqvi.csvdownloader;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.aku.hassannaqvi.csvdownloader.databinding.ActivityVocabulary2Binding;
import edu.aku.hassannaqvi.csvdownloader.models.DatabaseHelper;
import edu.aku.hassannaqvi.csvdownloader.models.Words;


public class Vocabulary2Activity extends AppCompatActivity {

    TextToSpeech t1;
    ActivityVocabulary2Binding bi;

    private final String TAG = "Vocabulary2Activity";
    private List<Words> allWords;
    Boolean favFlag = false;
    // private RecyclerView.Adapter fupsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_vocabulary2);


        bi.setCallback(this);
        setSupportActionBar(bi.toolbar);

        // bi.synm.setTypeface(Typeface.createFromAsset(this.getAssets(), "/ShabnamLightFD.ttf"));

        getWords();


        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                    t1.setPitch(0.8f);
                    t1.setSpeechRate(0.66f);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_favorite:
                item.setIcon(R.drawable.ic_favorite_filled);
                if (!favFlag) {
                    item.setIcon(R.drawable.ic_favorite_filled);
                    favFlag = true;

                    Toast.makeText(this, "Favorite Selected", Toast.LENGTH_SHORT).show();

                } else {
                    item.setIcon(R.drawable.ic_favorite);
                    favFlag = false;
                    Toast.makeText(this, "Favorite Removed", Toast.LENGTH_SHORT).show();

                }

        }
        return super.onOptionsItemSelected(item);
    }

    private void getWords() {
        bi.wViews.setBackgroundResource(0);
        DatabaseHelper db = new DatabaseHelper(Vocabulary2Activity.this); // Database Helper

        if (isConnected()) {
            Log.d(TAG, "getWords: Connected");
            bi.wmError.setError(null);
            bi.pBar3.setVisibility(View.VISIBLE);
//        String where = filter == null ? "" : " mrno='"+filter+"' ";
/*
        JSONObject json = new JSONObject();
        try {
            json.put("table", "childFollowup");
            //json.put("select", "sl2, sl4, sl5, sf6a");
            //json.put("filter", "sf5 = '" + bi.wfa101.getText().toString() + "'");
            //json.put("scrdt", MainApp.scrdt);
        } catch (JSONException e1) {
            e1.printStackTrace();
            //Log.d(TAG, "doWork: " + e1.getMessage());
        }
*/

            Data data = new Data.Builder()
                    .putString("table", "words")
                    //.putString("columns", "_id, sysdate")
                    // .putString("where", where)
                    .build();

            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

         /* final   PeriodicWorkRequest saveRequest =
                    new PeriodicWorkRequest.Builder(DataDownWorkerALL.class, 1, TimeUnit.HOURS).setInputData(data).setConstraints(constraints)
                            // Constraints
                            .build();*/

            final OneTimeWorkRequest workRequest1 = new OneTimeWorkRequest.Builder(DataDownWorkerALL.class).setInputData(data).setConstraints(constraints).build();
            WorkManager.getInstance().enqueue(workRequest1);
/*
        WorkManager.getInstance().enqueue(saveRequest);
*/


            WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest1.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {


                            //String progress = workInfo.getProgress().getString("progress");
                            //bi.wmError.setText("Progress: " + progress);
                            Log.d(TAG, "onChanged: " + workInfo.getState());

                            if (workInfo.getState() != null &&
                                    workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                Log.d(TAG, "onChanged: SUCCEEDED");
                                allWords = new ArrayList<Words>();

                                //Displaying the status into TextView
                                //mTextView1.append("\n" + workInfo.getState().name());
                                bi.wmError.setVisibility(View.GONE);
                                bi.pBar3.setVisibility(View.GONE);


                                String message = workInfo.getOutputData().getString("data");
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

                                    }
                                } catch (JSONException e) {
                                    bi.wmError.setText("JSON Error: " + message);
                                    bi.wmError.setVisibility(View.VISIBLE);
                                    Log.d("JSON Error", "onChanged: " + message);
                                    e.printStackTrace();


                                }
                                //fupsAdapter = new FollowupsAdapter((List<FollowUps>) allWords, FollowUpsList.this);
                                //recyclerView.setAdapter(fupsAdapter);

                                //bi.s3.setText(allWords.get(0).getSentcol3());
                                try {
                                    allWords = db.getAllWords();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d(TAG, "getWords: " + e.getMessage());
                                }

                                int i;
                                //i = (int) (Math.random() * (allWords.size() - 1) + 1);
                                i = 0;

                                Log.d("ID", "onChanged: " + allWords.get(i).getId());
                                db.incrementViews(allWords.get(i).getId());
                                bi.word.setText(allWords.get(i).getWord());
                                bi.synm.setText(allWords.get(i).getTrans());
                                bi.s1.setText(allWords.get(i).getSentcol1());
                                bi.s2.setText(allWords.get(i).getSentcol2());
                                bi.wViews.setText(db.getViews(allWords.get(i).getId()));
                                if (bi.wViews.getText().toString().equals("1")) {
                                    bi.wViews.setBackgroundResource(R.drawable.ic_star);
                                }

                            }
                            //mTextView1.append("\n" + workInfo.getState().name());
                            if (workInfo.getState() != null &&
                                    workInfo.getState() == WorkInfo.State.FAILED) {
                                Log.d(TAG, "onChanged: FAILED");
                                bi.pBar3.setVisibility(View.GONE);
                                String message = workInfo.getOutputData().getString("error");
                                bi.wmError.setText(message);
                                bi.wmError.setVisibility(View.VISIBLE);
                                try {
                                    allWords = db.getAllWords();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d(TAG, "getWords: " + e.getMessage());
                                }

                                int i;
                                //i = (int) (Math.random() * (allWords.size() - 1) + 1);
                                i = 0;

                                Log.d("ID", "onChanged: " + allWords.get(i).getId());
                                db.incrementViews(allWords.get(i).getId());
                                bi.word.setText(allWords.get(i).getWord());
                                bi.synm.setText(allWords.get(i).getTrans());
                                bi.s1.setText(allWords.get(i).getSentcol1());
                                bi.s2.setText(allWords.get(i).getSentcol2());
                                bi.wViews.setText(db.getViews(allWords.get(i).getId()));
                                if (bi.wViews.getText().toString().equals("1")) {
                                    bi.wViews.setBackgroundResource(R.drawable.ic_star);
                                }
                            }
                        }
                    });
        } else {
            Log.d(TAG, "getWords: Not Connected");
            try {
                allWords = db.getAllWords();
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "getWords: " + e.getMessage());
            }

            int i;
            //i = (int) (Math.random() * (allWords.size() - 1) + 1);
            i = 0;

            Log.d("ID", "onChanged: " + allWords.get(i).getId());
            db.incrementViews(allWords.get(i).getId());
            bi.word.setText(allWords.get(i).getWord());
            bi.synm.setText(allWords.get(i).getTrans());
            bi.s1.setText(allWords.get(i).getSentcol1());
            bi.s2.setText(allWords.get(i).getSentcol2());
            bi.wViews.setText(db.getViews(allWords.get(i).getId()));
            if (bi.wViews.getText().toString().equals("1")) {
                bi.wViews.setBackgroundResource(R.drawable.ic_star);
            }

        }


    }

    public void speakUp(View view) {
        String message = "";
        switch (view.getId()) {

            case R.id.btnWord:
                message = bi.word.getText().toString();
                break;
            case R.id.btnS1:
                message = bi.s1.getText().toString();
                break;
            case R.id.btnS2:
                message = bi.s2.getText().toString();
                break;

            case R.id.btnRefresh:
                getWords();
                break;
            case R.id.addWord1:
                toggleView();
                break;
            case R.id.addWord:
                addWords();
                break;


            default:
                message = bi.vName.getText().toString();
                break;
        }


        String toSpeak = message;
        Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

    }

    private void addWords() {
        bi.wmErrorW.setError(null);
        bi.pBar3W.setVisibility(View.VISIBLE);
        if (ValidateForm()) {

            try {
                UploadData();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            bi.wmErrorW.setError("Data validation failed!");
            bi.pBar3W.setVisibility(View.GONE);
        }


    }

    private boolean ValidateForm() {

        if (!bi.txtword.getText().toString().equals("")) {
            bi.txtword.setError(null);
        } else {
            bi.txtword.setError("Word cannot be blank.");
            Toast.makeText(this, "Word cannot be blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!bi.txttrans.getText().toString().equals("")) {
            bi.txttrans.setError(null);
        } else {
            bi.txttrans.setError("Word cannot be blank.");
            Toast.makeText(this, "Word cannot be blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!bi.txts1.getText().toString().equals("")) {
            bi.txts1.setError(null);
        } else {
            bi.txts1.setError("Word cannot be blank.");
            Toast.makeText(this, "Word cannot be blank", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private boolean UploadData() throws JSONException {


        JSONObject jsonWord = new JSONObject();

        jsonWord.put("word", bi.txtword.getText().toString());
        //jsonWord.put("trans", bi.txttrans.getText().toString());
        jsonWord.put("trans", bi.txttrans.getText().toString());
        jsonWord.put("sentcol1", bi.txts1.getText().toString());
        jsonWord.put("sentcol2", bi.txts2.getText().toString());


        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        Data data = new Data.Builder()
                .putString("table", "words")
                .putString("data", String.valueOf(jsonWord))
                .build();

        //This is the subclass of our WorkRequest

        OneTimeWorkRequest dataUpload = new OneTimeWorkRequest.Builder(DataUpWorkerALL.class).setInputData(data).setConstraints(constraints).build();
        WorkManager.getInstance().enqueue(dataUpload);


        WorkManager.getInstance().getWorkInfoByIdLiveData(dataUpload.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {


                        if (workInfo.getState() != null &&
                                workInfo.getState() == WorkInfo.State.SUCCEEDED) {

                            bi.wmErrorW.setVisibility(View.GONE);
                            bi.pBar3W.setVisibility(View.GONE);
                            //Displaying the status into TextView
                            //mTextView1.append("\n" + workInfo.getState().name());
                            String message = workInfo.getOutputData().getString("message");
                            //DatabaseHelper db = new DatabaseHelper(SectionWFFActivity.this); // Database Helper
                            StringBuilder sSyncedError = new StringBuilder();
                            JSONObject jsonObject;
                            try {

                                JSONArray json = new JSONArray(message);
                                for (int i = 0; i < json.length(); i++) {
                                    jsonObject = new JSONObject(json.getString(i));

                                    if (!jsonObject.getString("error").equals("1")) {

                                        if (jsonObject.getString("status").equals("1")) {

                                            //              db.updateSyncedFormsEN(jsonObject.getString("id"));  // UPDATE SYNCED

                                            Toast.makeText(Vocabulary2Activity.this, "Data saved successfully for: " + bi.txtword.getText().toString(), Toast.LENGTH_LONG).show();
                                            bi.wmErrorW.setText("Data saved successfully for: " + jsonObject.getString("id"));
                                            bi.wmErrorW.setVisibility(View.VISIBLE);
                                            bi.wmErrorW.setTextColor(getResources().getColor(R.color.green));

                                            final Handler handler = new Handler(Looper.getMainLooper());
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //Do something after 100ms
                                                    /*oF = new Intent(SectionWFFActivity.this, MainActivity.class);
                                                    startActivity(oF);*/

                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                            }, 3500);

                                        } else {
                                            sSyncedError.append("\nError: ").append(jsonObject.getString("message"));
                                        }
                                    } else {
                                        bi.wmErrorW.setText(jsonObject.getString("message"));
                                        bi.wmErrorW.setVisibility(View.VISIBLE);
                                        bi.wmErrorW.setTextColor(getResources().getColor(R.color.red));
                                        //bi.sf2.setError("MR No. already exists");
                                    }
                                }
                            } catch (JSONException e) {
                                bi.wmErrorW.setText("JSON Error: " + message);
                                bi.wmErrorW.setVisibility(View.VISIBLE);
                                Log.d("JSON Error", "onChanged: " + message);
                                e.printStackTrace();
                            }
                            //bi.sl2.setText(message);
                        }
                        //mTextView1.append("\n" + workInfo.getState().name());
                        if (workInfo.getState() != null &&
                                workInfo.getState() == WorkInfo.State.FAILED) {
                            bi.pBar3W.setVisibility(View.GONE);
                            String message = workInfo.getOutputData().getString("error");
                            bi.wmErrorW.setText(message);
                            bi.wmErrorW.setVisibility(View.VISIBLE);

                        }
                    }
                });
        return false;
    }

    public void toggleView() {
        if (bi.adminView.getVisibility() == View.GONE)
            if (bi.vName.getText().toString().trim().equals("all")) {
                bi.adminView.setVisibility(View.VISIBLE);
            } else if (bi.adminView.getVisibility() == View.VISIBLE)
                bi.adminView.setVisibility(View.GONE);
    }

    private void sendAccessibiltyEvent(String message) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.getApplicationContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager.isEnabled()) {
            AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_ANNOUNCEMENT);
            event.getText().add(message);
            accessibilityManager.sendAccessibilityEvent(event);
        }
    }

    private void displayNotification(String title, String task, int id) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("scrlog", "BLF", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "scrlog")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.drawable.ic_word);

        final int maxProgress = 100;
        int curProgress = 0;
        /*    notification.setProgress(length, curProgress, false);*/

        notificationManager.notify(id, notification.build());
    }

    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Log.d(TAG, "isConnected: ");
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public void loadView() {
        CardView cardView = bi.cardView;
        try {
            cardView.setDrawingCacheEnabled(true);

            Bitmap bitmap = loadBitmapFromView(cardView);
            cardView.setDrawingCacheEnabled(false);

            String mPath =
                    this.getExternalFilesDir(
                            Environment.DIRECTORY_PICTURES) + "/sid.jpg";

            File imageFile = new File(mPath);
            FileOutputStream outputStream = new
                    FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            shareWord(mPath);

        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public void shareWord(String path) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        File imageFileToShare = new File(path);
        Uri uri = FileProvider.getUriForFile(this, this.getPackageName(), imageFileToShare);
        //Uri uri = Uri.fromFile(imageFileToShare);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        //sendIntent.putExtra(Intent.EXTRA_TEXT, "https://google.com");
        sendIntent.setType("image/jpeg");
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));

        //  Intent shareIntent = Intent.createChooser(sendIntent, null);
        // startActivity(sendIntent);

    }

    public Bitmap loadBitmapFromView(View v) {

        DisplayMetrics dm = getResources().getDisplayMetrics();

        v.measure(View.MeasureSpec.makeMeasureSpec(v.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(v.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);
        bi.sentBy.setVisibility(View.GONE);
        bi.fldGrpButtons.setVisibility(View.VISIBLE);
        bi.btnS1.setVisibility(View.VISIBLE);
        bi.btnS2.setVisibility(View.VISIBLE);
        bi.btnWord.setVisibility(View.VISIBLE);

        return returnedBitmap;
    }

    public void shareUp(View view) {
        bi.fldGrpButtons.setVisibility(View.GONE);
        bi.btnS1.setVisibility(View.GONE);
        bi.btnS2.setVisibility(View.GONE);
        bi.btnWord.setVisibility(View.GONE);
        bi.sentBy.setVisibility(View.VISIBLE);
        if (bi.fldGrpButtons.getVisibility() == View.GONE) {
            loadView();
        } else {
            shareUp(view);
        }
    }
}