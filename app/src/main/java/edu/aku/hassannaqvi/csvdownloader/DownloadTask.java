package edu.aku.hassannaqvi.csvdownloader;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadTask extends AsyncTask<String, Integer, List<String>> {
    // Before the tasks execution

    HttpURLConnection urlConnection;
    TextView mTV;
    private Context mContext;
    private URL serverURL = null;

    public DownloadTask(Context context, TextView mTV) {

        mContext = context;
        this.mTV = mTV;
    }

    public DownloadTask(Context context, URL url) {
        mContext = context;
        serverURL = url;

    }

    protected void onPreExecute() {

        mTV.setText(mTV.getText() + "\nStarting task....");
    }

    // Do the task in background/non UI thread
    protected List<String> doInBackground(String... tasks) {
        // Get the number of task
        int count = tasks.length;
        // Initialize a new list
        List<String> taskList = new ArrayList<>(count);

        // Loop through the task
        for (int i = 0; i < count; i++) {
            // Do the current task task here
            String currentTask = tasks[i];
            taskList.add(currentTask);

            // Sleep the UI thread for 1 second
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

                /*
                    publishProgress
                        void publishProgress (Progress... values)
                            This method can be invoked from doInBackground(Params...) to publish
                            updates on the UI thread while the background computation is still
                            running. Each call to this method will trigger the execution of
                            onProgressUpdate(Progress...) on the UI thread.
                            onProgressUpdate(Progress...) will not be called if
                            the task has been canceled.

                        Parameters
                            values Progress: The progress values to update the UI with.
                */
            // Publish the async task progress
            // Added 1, because index start from 0
            publishProgress((int) (((i + 1) / (float) count) * 100));

            // If the AsyncTask cancelled
            if (isCancelled()) {
                break;
            }
        }
        // Return the task list for post execute
        return taskList;
    }


    /*
        onProgressUpdate
            void onProgressUpdate (Progress... values)
                Runs on the UI thread after publishProgress(Progress...) is invoked.
                The specified values are the values passed to publishProgress(Progress...).

            Parameters
                values Progress: The values indicating progress.
    */
    // After each task done
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        mTV.setText(mTV.getText() + "\nCompleted...." + progress[0] + "%");
    }

    // When all async task done
    protected void onPostExecute(List<String> result) {
        mTV.setText(mTV.getText() + "\n\nDone....");
        for (int i = 0; i < result.size(); i++) {
            mTV.setText(mTV.getText() + "\n" + result.get(i));
        }
    }
}