package edu.aku.hassannaqvi.csvdownloader;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;

import java.util.Locale;

import edu.aku.hassannaqvi.csvdownloader.databinding.ActivityVocabularyBinding;

public class VocabularyActivity extends AppCompatActivity {

    TextToSpeech t1;

    ActivityVocabularyBinding bi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_vocabulary);
        bi.setCallback(this);

        // bi.synm.setTypeface(Typeface.createFromAsset(this.getAssets(), "/ShabnamLightFD.ttf"));

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
            case R.id.btnWord2:
                message = bi.word2.getText().toString();
                break;
            case R.id.btnS12:
                message = bi.s12.getText().toString();
                break;
            case R.id.btnS22:
                message = bi.s22.getText().toString();
                break;

            case R.id.btnWord3:
                message = bi.word3.getText().toString();
                break;
            case R.id.btnS13:
                message = bi.s13.getText().toString();
                break;


            case R.id.btnWord4:
                message = bi.word4.getText().toString();
                break;
            case R.id.btnS14:
                message = bi.s14.getText().toString();
                break;
            case R.id.btnS24:
                message = bi.s24.getText().toString();
                break;

            case R.id.btnWord5:
                message = bi.word5.getText().toString();
                break;
            case R.id.btnS15:
                message = bi.s15.getText().toString();
                break;
            case R.id.btnS25:
                message = bi.s25.getText().toString();
                break;


            default:
                message = bi.vName.getText().toString();
                break;
        }


        String toSpeak = message;
        Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

    }

    private void sendAccessibiltyEvent(String message) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.getApplicationContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager.isEnabled()) {
            AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_ANNOUNCEMENT);
            event.getText().add(message);
            accessibilityManager.sendAccessibilityEvent(event);
        }
    }

    private void displayNotification(String title, String task) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("scrlog", "BLF", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "scrlog")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher);

        final int maxProgress = 100;
        int curProgress = 0;
        /*    notification.setProgress(length, curProgress, false);*/

        notificationManager.notify(1, notification.build());
    }
}