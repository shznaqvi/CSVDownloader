package edu.aku.hassannaqvi.csvdownloader;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String PROJECT_NAME = "TMK_EL";
    private static final String APP_NAME = "GSED";
    EditText serverURL;

    TextView mTextView2;
    TextView mTextView3;
    TextView mTextViewS;
    LinearLayout mReport;
    File sdDir;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    File[] files = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


        setContentView(R.layout.activity_main);
        mTextViewS = findViewById(R.id.mTextViewS);
        mReport = findViewById(R.id.report);


        sharedPref = getSharedPreferences("src", MODE_PRIVATE);
        editor = sharedPref.edit();
        sdDir = new File("/storage/emulated/0/Pictures/UenTmkEl2020/uploaded/");
        upDatePhotoCount();
    }

    public void upDatePhotoCount() {
        if (sdDir.exists()) {
            Log.d("DIR", "onCreate: " + sdDir.getName());
            File[] files = sdDir.listFiles(file -> (file.getPath().endsWith(".jpg") || file.getPath().endsWith(".jpeg")));


            if (files.length > 0) {
                // WORK MANAGER
                mTextViewS.setText((files.length + " Photos remaining"));
                //This is for setting Contraints for sync


            } else {
                mTextViewS.setText("0 Photos remaining");

            }
        } else {
            mTextViewS.setText("This app is not compatible for your device");
        }
    }

    public void chechPhotos(View view) {
        File[] files = sdDir.listFiles(file -> (file.getPath().endsWith(".jpg") || file.getPath().endsWith(".jpeg")));

        mTextViewS.setText(files.length + " Photos remaining");
    }

    public void btnUploadPhotos(View view) {
        mReport.removeAllViews();
        /*File directory = new File(this.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), PROJECT_NAME);*/
        Log.d("Directory", "uploadPhotos: " + sdDir);
        if (sdDir.exists()) {
            File[] files = sdDir.listFiles(file -> (file.getPath().endsWith(".jpg") || file.getPath().endsWith(".jpeg")));
            mTextViewS.setText(files.length + " Photos remaining");
            Log.d("Files", "Count: " + files.length);
            if (files.length > 0) {
                int fcount = Math.min(files.length, 300);
                for (int i = 0; i < fcount; i++) {

                    File fileZero = files[i];
                    TextView textView = new TextView(this);
                    textView.setText("PROCESSING: " + fileZero.getName());
                    textView.setId(i);
                    mReport.addView(textView);
                    Log.d("Files", "FileName:" + fileZero.getName());
                    //   SyncAllPhotos syncAllPhotos = new SyncAllPhotos(file.getName(), this);

                    Constraints constraints = new Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build();

                    Data data = new Data.Builder()
                            .putString("filename", fileZero.getName()).build();

                    //This is the subclass of our WorkRequest

                    OneTimeWorkRequest photoUpload = new OneTimeWorkRequest.Builder(PhotoUploadWorker2.class).setInputData(data).setConstraints(constraints).build();


                    WorkManager.getInstance().enqueue(photoUpload);
                    //Listening to the work status
                    int finalI = i;
                    final TextView[] mTextView1 = new TextView[1];

                    WorkManager.getInstance().getWorkInfoByIdLiveData(photoUpload.getId())
                            .observe(this, new Observer<WorkInfo>() {

                                @Override
                                public void onChanged(@Nullable WorkInfo workInfo) {
                                    //mTextViewS.append("\n" + workInfo.getState().name());
                                    //Displaying the status into TextView
                                    // mTextViewS.append("\n"+workInfo.getState().name());
                                    mTextView1[0] = findViewById(finalI);
                                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                        String message = workInfo.getState().name() + ": " + workInfo.getOutputData().getString("message");
                                        mTextView1[0].setText(message);
                                        mTextView1[0].setTextColor(Color.parseColor("#007f00"));

                                        upDatePhotoCount();
                                    }

                                    if (workInfo.getState() == WorkInfo.State.FAILED) {
                                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        String error = workInfo.getState().name() + ": " + workInfo.getOutputData().getString("error");
                                        mTextView1[0].setText(error);
                                        mTextView1[0].setTextColor(Color.RED);

                                        upDatePhotoCount();
                                    }

                                    if (workInfo.getState() == WorkInfo.State.CANCELLED) {
                                        //String message = workInfo.getState().name() + ": " + workInfo.getOutputData().getString("message");
                                        mTextView1[0].setText("CANCELLED: " + fileZero.getName());
                                        mTextView1[0].setTextColor(Color.RED);

                                        upDatePhotoCount();
                                    }
                                    String fCount = String.valueOf(workInfo.getOutputData().getInt("fCount", 0));
                                    Toast.makeText(MainActivity.this, "Files Left: " + fCount, Toast.LENGTH_SHORT).show();
                                }
                            });
/*
                    if (i%25 == 0){
                        try {
                            //3000 ms delay to process upload of next file.
                            Thread.sleep(12500);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }*/

                }
                editor.putString("LastPhotoUpload", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
                editor.apply();

            } else {
                mTextViewS.setText(files.length + " Photos remaining");
                Toast.makeText(this, "No photos to upload.", Toast.LENGTH_SHORT).show();
            }
        } else {
            mTextViewS.setText("This app is not compatible with your device.");

            Toast.makeText(this, "No photos were taken", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        // Toast.makeText(this, "Back Press Not Allowed", Toast.LENGTH_SHORT).show();
    }


}