package edu.aku.hassannaqvi.csvdownloader;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private static final String APP_NAME = "GSED";
    //tivityMainBinding bi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //i = DataBindingUtil.setContentView(this, R.layout.activity_main);

        MainApp.child = new Child();
        // bi.setChild(MainApp.child);
    }


    public void downloadCSV(View view) {


            new getDataCsv(this).execute();

    }


    public void downloadPrepop(View view) {
        //      bi.serverURL.setError(null);


        new getEligibilityCsv(this).execute();

    }

   /* public void getChild(View view) {

        if (bi.dssid.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a valid Parents_Study_ID", Toast.LENGTH_SHORT).show();
        } else {

            new getDataCsv(this, bi.dssid.getText().toString()).execute();


        }

    }*/

    public void setThis(View view) {
        //bi.gsedid.setText(MainApp.child.getGsedId());
        MainApp.child.setGsedId("99999999");

    }
}