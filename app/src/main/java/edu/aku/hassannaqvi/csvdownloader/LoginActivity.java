package edu.aku.hassannaqvi.csvdownloader;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.aku.hassannaqvi.csvdownloader.core.AppInfo;
import edu.aku.hassannaqvi.csvdownloader.core.DatabaseHelper;
import edu.aku.hassannaqvi.csvdownloader.core.MainApp;
import edu.aku.hassannaqvi.csvdownloader.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    protected static LocationManager locationManager;

    ActivityLoginBinding bi;
    Spinner spinnerDistrict;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String DirectoryName;
    DatabaseHelper db;
    ArrayAdapter<String> provinceAdapter;
    int attemptCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_login);
        bi.setCallback(this);
        MainApp.appInfo = new AppInfo(this);

        bi.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //   viewPager.setCurrentItem(tab.getPosition());
                int p = tab.getPosition();
                if (tab.getPosition() == 0) {
                    bi.fldGrpRegister.setVisibility(View.GONE);
                    bi.fldGrpRegister.playSoundEffect(SoundEffectConstants.NAVIGATION_DOWN);
                } else {
                    bi.fldGrpRegister.setVisibility(View.VISIBLE);
                    bi.fldGrpRegister.playSoundEffect(SoundEffectConstants.NAVIGATION_UP);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
// initiate a MultiAutoCompleteTextView
// set adapter to fill the data in suggestion list
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

        };

        Calendar today = Calendar.getInstance();

        Log.d(TAG, "onCreate: Date: " + today.get(Calendar.YEAR) + "-" + today.get(Calendar.MONTH) + "-" + today.get(Calendar.DAY_OF_MONTH));
        bi.dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        LoginActivity.this, date, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        List<String> countries = new ArrayList<>();

        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.add(l.getDisplayCountry() + " (" + iso + ")");
            Log.d(TAG, "onCreate: " + l.getDisplayCountry() + " (" + iso + ")");
        }

        ArrayAdapter<String> countriesList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        bi.country.setAdapter(countriesList);

// set threshold value 1 that help us to start the searching from first character
        bi.country.setThreshold(2);
// set tokenizer that distinguish the various substrings by comma
        // bi.country.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        List<String> languages = new ArrayList<>();

        for (String iso : Locale.getISOLanguages()) {
            Locale l = new Locale(iso);
            languages.add(l.getDisplayLanguage() + " (" + iso + ")");
            Log.d(TAG, "onCreate: " + l.getDisplayLanguage() + " (" + iso + ")");
        }

        ArrayAdapter<String> languagesList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languages);
        bi.motherTongue.setAdapter(languagesList);

// set threshold value 1 that help us to start the searching from first character
        bi.motherTongue.setThreshold(2);


    }

    public void login(View view) {

        startActivity(new Intent(this, Vocabulary2Activity.class));

    }

    /*
        private void callUsersWorker() {


            Constraints mConstraints = new Constraints
                    .Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            final OneTimeWorkRequest usersWorkRequest1 = new OneTimeWorkRequest
                    .Builder(FetchUsersWorker.class)
                    .setConstraints(mConstraints)
                    .build();

            WorkManager.getInstance().enqueue(usersWorkRequest1);

            WorkManager.getInstance().getWorkInfoByIdLiveData(usersWorkRequest1.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {


                           */
/* WorkManager.getInstance().enqueue(usersWorkRequest1);

                        WorkManager.getInstance().getWorkInfoByIdLiveData(WorkRequest.getId())
                                .observe(this, new Observer<WorkInfo>() {
                                            @Override
                                            public void onChanged(WorkInfo workInfo) {

                                                switch (workInfo.getState()) {
                                                    case ENQUEUED:
                                                        // TODO: Show alert here
                                                        break;
                                                    case RUNNING:
                                                        // TODO: Remove alert, if running
                                                        break;
                                                    case SUCCEEDED:
                                                        // TODO: after complete
                                                        break;
                                                    case FAILED:
                                                        break;
                                                    case BLOCKED:
                                                        break;
                                                    case CANCELLED:
                                                        break;
                                                }

                                            }
                                        }
                                );*//*


                        if (workInfo.getState() != null &&
                                workInfo.getState() == WorkInfo.State.SUCCEEDED) {

                            //updateDB();

                            bi.pbarMR.setVisibility(View.GONE);
                            bi.btnSignin.setVisibility(View.VISIBLE);
                       //     bi.syncData.setVisibility(View.GONE);

                        }
                        if (workInfo.getState() != null &&
                                workInfo.getState() == WorkInfo.State.FAILED) {
                            bi.pbarMR.setVisibility(View.GONE);
                        //    bi.syncData.setVisibility(View.VISIBLE);

                        }
                        if (workInfo.getState() != null &&
                                workInfo.getState() == WorkInfo.State.RUNNING) {
                            bi.pbarMR.setVisibility(View.VISIBLE);
                            bi.btnSignin.setVisibility(View.GONE);
                       //     bi.syncData.setVisibility(View.GONE);

                        }
                    }
                });
    }
*/
    private void updateLabel(Calendar cal) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        bi.dob.setText(sdf.format(cal.getTime()));
    }

    public void changeLayout(View view) {

        switch (view.getId()) {
            case R.id.tabLogin:
                bi.fldGrpRegister.setVisibility(View.GONE);
                break;
            case R.id.tabRegister:
                bi.fldGrpRegister.setVisibility(View.VISIBLE);
                break;

        }
    }
}


