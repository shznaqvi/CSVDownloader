package edu.aku.hassannaqvi.csvdownloader;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String APP_NAME = "GSED";

    EditText serverURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverURL = (EditText) findViewById(R.id.severURL);
    }


    public void downloadCSV(View view) {
        serverURL.setError(null);

        if (serverURL.getText().toString().equals("")) {
            new getDataCsv(this).execute();
        } else {
            try {
                new getDataCsv(this, new URL(serverURL.getText().toString())).execute();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(this, "Server URL is incorrect.", Toast.LENGTH_LONG).show();
                serverURL.setError("Server URL is incorrect. \nError: " + e.getMessage());
            }

        }
    }


}