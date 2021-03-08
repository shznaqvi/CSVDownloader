package edu.aku.hassannaqvi.csvdownloader;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class getDataCsv extends AsyncTask<String, String, String> {

    private static final Object APP_NAME = "GSED";
    private final String TAG = "GetCSVs()";
    private final Context mContext;
    HttpURLConnection urlConnection;
    private String dssid = null;
    private URL serverURL = null;
    private ProgressDialog pd;

    public getDataCsv(Context context) {
        mContext = context;
    }

    public getDataCsv(Context context, String dssid) {
        mContext = context;
        this.dssid = dssid;
    }

    public getDataCsv(Context context, URL url) {
        mContext = context;
        serverURL = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(mContext);
        pd.setTitle("Syncing All CSVs");
        pd.setMessage("Getting connected to server...");
        pd.show();
        Log.d(TAG, "onPreExecute: Starting");
    }

    @Override
    protected String doInBackground(String... args) {
        Log.d(TAG, "doInBackground: Starting");
        StringBuilder result = new StringBuilder();

        URL url = null;
        try {
            Log.d(TAG, "doInBackground: Trying...");
            if (serverURL == null) {
                //    url = new URL("http://43.245.131.159:8080/dss/api/getdata.php");
                url = new URL("http://3.249.206.243/aku/api/getdata.php");
            } else {
                url = serverURL;
            }
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

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            JSONObject json = new JSONObject();
            try {
                json.put("table", "gsed_enroll_info_csv");
                if (dssid != null) {
                    json.put("filter", "parent_study_id_key = '" + this.dssid + "' ");
                }
                Log.d(TAG, "json.put: Done");
            } catch (JSONException e1) {
                e1.printStackTrace();
                Log.d(TAG, e1.getMessage());
            }
            Log.d(TAG, "downloadUrl: " + json.toString());
            wr.writeBytes(json.toString());
            wr.flush();
            wr.close();
            Log.d(TAG, "doInBackground: " + urlConnection.getResponseCode());
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    Log.i(TAG, "CSVs In: " + line);
                    result.append(line);
                }
            }
        } catch (java.net.SocketTimeoutException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
            return null;
        } catch (java.io.IOException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());

            return null;
        } finally {
//            urlConnection.disconnect();
        }

        return result.toString();
    }

    public static String decrypt(String encoded) {
        try {
            byte[] ivAndCipherText = Base64.decode(encoded, Base64.NO_WRAP);
            byte[] iv = Arrays.copyOfRange(ivAndCipherText, 0, 16);
            byte[] cipherText = Arrays.copyOfRange(ivAndCipherText, 16, ivAndCipherText.length);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec("asSa%s|n'$ crEed".getBytes(StandardCharsets.UTF_8), "AES"), new IvParameterSpec(iv));
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String results) {
        Log.d(TAG, "onPostExecute: Encrypted: " + results);
        Toast.makeText(mContext, results, Toast.LENGTH_SHORT).show();
        String result = decrypt(results);
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onPostExecute: Decrypted: " + result);
        //Do something with the JSON string
        if (result != null) {
            if (result.length() > 2) {
                String json = result;
                if (json.length() > 0) {
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        CSVWriter writer = null;
                        //String csv = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/childlist.csv"); // Here csv file name is MyCsvFile.csv
//                    sdDir = new File("/storage/emulated/0/com/forms/");
                        //File csvFolder = new File("/storage/emulated/0/com/forms/");


                        if (dssid == null) {

                            String[] folders = {
                                    "GSED SF-Media",
                                    "GSED Anthropometry-media",
                                    "GSED CPAS FSS-media",
                                    "GSED HOME-media",
                                    "GSED LF-media",
                                    "GSED PHQ9-media",
                                    "GSED PSY-media"


                            };
                            //File csvFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + APP_NAME);

                            for (String folder : folders) {
                                File csvFolder = new File("/storage/emulated/0/com/forms/" + folder);

                                if (!csvFolder.exists()) {
                                    csvFolder.mkdirs();
                                }

                                File csvFile = new File(csvFolder + File.separator + "gsed_enroll_info_csv.csv");

                                writer = new CSVWriter(new FileWriter(csvFile));
                                List<String[]> data = new ArrayList<String[]>();
                    /*
                    // SAMPLE DATA FROM SERVER
                                        {
                                                "gsedid": "0081",
                                                "child_name": "FOKAIHA",
                                                "mother_name": "SABIRA",
                                                "dob": "19-05-2004"
                                        },
                    */
                                data.add(new String[]{
                                        ChildContract.ChildTable.COLUMN_DATE_FORM,
                                        ChildContract.ChildTable.COLUMN_SITE,
                                        ChildContract.ChildTable.COLUMN_PARENT_STUDY_ID_KEY,
                                        ChildContract.ChildTable.COLUMN_GSED_ID,
                                        ChildContract.ChildTable.COLUMN_CHILD_NAME,
                                        ChildContract.ChildTable.COLUMN_MOTHER_NAME,
                                        ChildContract.ChildTable.COLUMN_FATHER_NAME,
                                        ChildContract.ChildTable.COLUMN_CHILD_SEX,
                                        ChildContract.ChildTable.COLUMN_DOB,
                                        ChildContract.ChildTable.COLUMN_CARE_GIVER,
                                        ChildContract.ChildTable.COLUMN_CAREGIVER_NAME,
                                        ChildContract.ChildTable.COLUMN_STUDY_TYPE,
                                        ChildContract.ChildTable.COLUMN_CONC_VALIDITY_YESNO,
                                        ChildContract.ChildTable.COLUMN_RELIABILITY_YESNO,
                                        ChildContract.ChildTable.COLUMN_INTERRATE_YESNO,
                                        ChildContract.ChildTable.COLUMN_INTRARATER_YESNO,
                                        ChildContract.ChildTable.COLUMN_PRED_VALIDITY_YESNO,
                                        ChildContract.ChildTable.COLUMN_COVID_MAIN,
                                        ChildContract.ChildTable.COLUMN_DATE_COVID,
                                        ChildContract.ChildTable.COLUMN_PSY_MAIN,
                                        ChildContract.ChildTable.COLUMN_DATE_PSY,
                                        ChildContract.ChildTable.COLUMN_PSY_IRATER,
                                        ChildContract.ChildTable.COLUMN_PSY_TEST_RETEST,
                                        ChildContract.ChildTable.COLUMN_PSY_CONCURRENT,
                                        ChildContract.ChildTable.COLUMN_PSY_PREDICTIVE,
                                        ChildContract.ChildTable.COLUMN_SF_MAIN,
                                        ChildContract.ChildTable.COLUMN_DATE_SF,
                                        ChildContract.ChildTable.COLUMN_SF_IRATER,
                                        ChildContract.ChildTable.COLUMN_SF_TEST_RETEST,
                                        ChildContract.ChildTable.COLUMN_SF_CONCURRENT,
                                        ChildContract.ChildTable.COLUMN_SF_PREDICTIVE,
                                        ChildContract.ChildTable.COLUMN_PHQ9_MAIN,
                                        ChildContract.ChildTable.COLUMN_DATE_PHQ9,
                                        ChildContract.ChildTable.COLUMN_CPAS_MAIN,
                                        ChildContract.ChildTable.COLUMN_DATE_CPAS,
                                        ChildContract.ChildTable.COLUMN_HOME_MAIN,
                                        ChildContract.ChildTable.COLUMN_DATE_HOME,
                                        ChildContract.ChildTable.COLUMN_ANTHRO_MAIN,
                                        ChildContract.ChildTable.COLUMN_DATE_ANTHRO,
                                        ChildContract.ChildTable.COLUMN_ANTHRO_PREDICTIVE,
                                        ChildContract.ChildTable.COLUMN_LF_MAIN,
                                        ChildContract.ChildTable.COLUMN_DATE_LF,
                                        ChildContract.ChildTable.COLUMN_LF_IRATER,
                                        ChildContract.ChildTable.COLUMN_LF_TEST_RETEST,
                                        ChildContract.ChildTable.COLUMN_LF_CONCURRENT,
                                        ChildContract.ChildTable.COLUMN_LF_PREDICTIVE,
                                });
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectCC = jsonArray.getJSONObject(i);
                                    data.add(new String[]{
                                            jsonObjectCC.getString("date_form").equals("null") ? "" : jsonObjectCC.getString("date_form"),
                                            jsonObjectCC.getString("site"),
                                            jsonObjectCC.getString("parent_study_id_key"),
                                            jsonObjectCC.getString("gsed_id"),
                                            jsonObjectCC.getString("child_name"),
                                            jsonObjectCC.getString("mother_name"),
                                            jsonObjectCC.getString("father_name"),
                                            jsonObjectCC.getString("child_sex"),
                                            jsonObjectCC.getString("dob"),
                                            jsonObjectCC.getString("care_giver"),
                                            jsonObjectCC.getString("caregiver_name"),
                                            jsonObjectCC.getString("study_type"),
                                            jsonObjectCC.getString("conc_validity_yesno"),
                                            jsonObjectCC.getString("reliability_yesno"),
                                            jsonObjectCC.getString("interrate_yesno"),
                                            jsonObjectCC.getString("intrarater_yesno"),
                                            jsonObjectCC.getString("pred_validity_yesno"),
                                            jsonObjectCC.getString("COVID_MAIN"),
                                            jsonObjectCC.getString("date_covid").equals("null") ? "" : jsonObjectCC.getString("date_covid"),
                                            jsonObjectCC.getString("PSY_MAIN"),
                                            jsonObjectCC.getString("date_psy").equals("null") ? "" : jsonObjectCC.getString("date_psy"),
                                            jsonObjectCC.getString("PSY_IRATER"),
                                            jsonObjectCC.getString("PSY_TEST_RETEST"),
                                            jsonObjectCC.getString("PSY_CONCURRENT"),
                                            jsonObjectCC.getString("PSY_PREDICTIVE"),
                                            jsonObjectCC.getString("SF_MAIN"),
                                            jsonObjectCC.getString("date_sf").equals("null") ? "" : jsonObjectCC.getString("date_sf"),
                                            jsonObjectCC.getString("SF_IRATER"),
                                            jsonObjectCC.getString("SF_TEST_RETEST"),
                                            jsonObjectCC.getString("SF_CONCURRENT"),
                                            jsonObjectCC.getString("SF_PREDICTIVE"),
                                            jsonObjectCC.getString("PHQ9_MAIN"),
                                            jsonObjectCC.getString("date_phq9").equals("null") ? "" : jsonObjectCC.getString("date_phq9"),
                                            jsonObjectCC.getString("CPAS_MAIN"),
                                            jsonObjectCC.getString("date_cpas").equals("null") ? "" : jsonObjectCC.getString("date_cpas"),
                                            jsonObjectCC.getString("HOME_MAIN"),
                                            jsonObjectCC.getString("date_home").equals("null") ? "" : jsonObjectCC.getString("date_home"),
                                            jsonObjectCC.getString("ANTHRO_MAIN"),
                                            jsonObjectCC.getString("date_anthro").equals("null") ? "" : jsonObjectCC.getString("date_anthro"),
                                            jsonObjectCC.getString("ANTHRO_PREDICTIVE"),
                                            jsonObjectCC.getString("LF_MAIN"),
                                            jsonObjectCC.getString("date_lf").equals("null") ? "" : jsonObjectCC.getString("date_lf"),
                                            jsonObjectCC.getString("LF_IRATER"),
                                            jsonObjectCC.getString("LF_TEST_RETEST"),
                                            jsonObjectCC.getString("LF_CONCURRENT"),
                                            jsonObjectCC.getString("LF_PREDICTIVE"),


                                    });

                                }
                                writer.writeAll(data); // data is adding to csv

                                writer.close();
                            }
                            //callRead();
                            pd.setTitle("Success");
                            pd.setMessage("All CSV Files saved successfully.");
                            pd.show();
                        } else {

                            // CHILD
                            pd.setTitle("Success");
                            pd.setMessage("Child data fetched successfully.");
                            pd.show();
                            Log.d(TAG, "onPostExecute: getChild: " + result);
                            JSONObject jsonObjectCC = jsonArray.getJSONObject(0);
                            MainApp.child = new Child(

                                    jsonObjectCC.getString("date_form").equals("null") ? "" : jsonObjectCC.getString("date_form"),
                                    jsonObjectCC.getString("site"),
                                    jsonObjectCC.getString("parent_study_id_key"),
                                    jsonObjectCC.getString("gsed_id"),
                                    jsonObjectCC.getString("child_name"),
                                    jsonObjectCC.getString("mother_name"),
                                    jsonObjectCC.getString("father_name"),
                                    jsonObjectCC.getString("child_sex"),
                                    jsonObjectCC.getString("dob"),
                                    jsonObjectCC.getString("care_giver"),
                                    jsonObjectCC.getString("caregiver_name"),
                                    jsonObjectCC.getString("study_type"),
                                    jsonObjectCC.getString("conc_validity_yesno"),
                                    jsonObjectCC.getString("reliability_yesno"),
                                    jsonObjectCC.getString("interrate_yesno"),
                                    jsonObjectCC.getString("intrarater_yesno"),
                                    jsonObjectCC.getString("pred_validity_yesno"),
                                    jsonObjectCC.getString("COVID_MAIN"),
                                    jsonObjectCC.getString("date_covid").equals("null") ? "" : jsonObjectCC.getString("date_covid"),
                                    jsonObjectCC.getString("PSY_MAIN"),
                                    jsonObjectCC.getString("date_psy").equals("null") ? "" : jsonObjectCC.getString("date_psy"),
                                    jsonObjectCC.getString("PSY_IRATER"),
                                    jsonObjectCC.getString("PSY_TEST_RETEST"),
                                    jsonObjectCC.getString("PSY_CONCURRENT"),
                                    jsonObjectCC.getString("PSY_PREDICTIVE"),
                                    jsonObjectCC.getString("SF_MAIN"),
                                    jsonObjectCC.getString("date_sf").equals("null") ? "" : jsonObjectCC.getString("date_sf"),
                                    jsonObjectCC.getString("SF_IRATER"),
                                    jsonObjectCC.getString("SF_TEST_RETEST"),
                                    jsonObjectCC.getString("SF_CONCURRENT"),
                                    jsonObjectCC.getString("SF_PREDICTIVE"),
                                    jsonObjectCC.getString("PHQ9_MAIN"),
                                    jsonObjectCC.getString("date_phq9").equals("null") ? "" : jsonObjectCC.getString("date_phq9"),
                                    jsonObjectCC.getString("CPAS_MAIN"),
                                    jsonObjectCC.getString("date_cpas").equals("null") ? "" : jsonObjectCC.getString("date_cpas"),
                                    jsonObjectCC.getString("HOME_MAIN"),
                                    jsonObjectCC.getString("date_home").equals("null") ? "" : jsonObjectCC.getString("date_home"),
                                    jsonObjectCC.getString("ANTHRO_MAIN"),
                                    jsonObjectCC.getString("date_anthro").equals("null") ? "" : jsonObjectCC.getString("date_anthro"),
                                    jsonObjectCC.getString("ANTHRO_PREDICTIVE"),
                                    jsonObjectCC.getString("LF_MAIN"),
                                    jsonObjectCC.getString("date_lf").equals("null") ? "" : jsonObjectCC.getString("date_lf"),
                                    jsonObjectCC.getString("LF_IRATER"),
                                    jsonObjectCC.getString("LF_TEST_RETEST"),
                                    jsonObjectCC.getString("LF_CONCURRENT"),
                                    jsonObjectCC.getString("LF_PREDICTIVE")


                            );
                            pd.hide();
                            Toast.makeText(mContext, "Child info updated", Toast.LENGTH_LONG).show();

                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        pd.setTitle("Error");
                        pd.setMessage(e.getMessage());
                        pd.show();
                    }
                } else {
                    pd.setMessage("Received: " + json.length() + "");
                    pd.show();
                }
            } else {

                pd.setTitle("Data Error");
                pd.setMessage("Record not found!");
                pd.show();
            }
        } else {
            pd.setTitle("Connection Error");
            pd.setMessage("Server not found!");
            pd.show();
        }
    }
}