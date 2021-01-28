package edu.aku.hassannaqvi.csvdownloader;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.net.MalformedURLException;
import java.net.URL;

import edu.aku.hassannaqvi.csvdownloader.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String APP_NAME = "GSED";
    ActivityMainBinding bi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);


        bi = DataBindingUtil.setContentView(this, R.layout.activity_main);

        MainApp.child = new Child();
        bi.setChild(MainApp.child);
    }


    public void downloadCSV(View view) {
        bi.serverURL.setError(null);

        if (bi.serverURL.getText().toString().equals("")) {
            new getDataCsv(this).execute();
        } else {
            try {
                new getDataCsv(this, new URL(bi.serverURL.getText().toString())).execute();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(this, "Server URL is incorrect.", Toast.LENGTH_LONG).show();
                bi.serverURL.setError("Server URL is incorrect. \nError: " + e.getMessage());
            }

        }
    }


    public void downloadPrepop(View view) {
        bi.serverURL.setError(null);

        if (bi.serverURL.getText().toString().equals("")) {
            new getEligibilityCsv(this).execute();
        } else {
            try {
                new getEligibilityCsv(this, new URL(bi.serverURL.getText().toString())).execute();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(this, "Server URL is incorrect.", Toast.LENGTH_LONG).show();
                bi.serverURL.setError("Server URL is incorrect. \nError: " + e.getMessage());
            }

        }
    }

    public void getChild(View view) {

        if (bi.dssid.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a valid Parents_Study_ID", Toast.LENGTH_SHORT).show();
        } else {

            new getDataCsv(this, bi.dssid.getText().toString()).execute();


        }

    }

    public void setThis(View view) {
        //bi.gsedid.setText(MainApp.child.getGsedId());
        MainApp.child.setGsedId("99999999");

    }
}