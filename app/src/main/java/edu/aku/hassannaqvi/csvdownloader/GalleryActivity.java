package edu.aku.hassannaqvi.csvdownloader;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.io.File;

import edu.aku.hassannaqvi.csvdownloader.databinding.ActivityGalleryBinding;

public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";
    //ImageView selectedImage;
    ActivityGalleryBinding bi;
    private File sdDir;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_gallery);


        bi.setCallback(this);
        sdDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "VOCAPP");
        Log.d(TAG, "onCreate: " + sdDir);
        //Log.d(TAG, "onCreate: Length"+ sdDir.listFiles(file -> (file.getPath().endsWith(".jpg") || file.getPath().endsWith(".jpeg"))).length);

        if (sdDir.exists()) {
            File[] files = sdDir.listFiles(file -> (file.getPath().endsWith(".jpg") || file.getPath().endsWith(".jpeg")));
            if (files.length > 0) {
                Log.d("Files", "Count: " + files.length);


                // Gallery gallery = (Gallery) findViewById(R.id.gallery);
                // selectedImage=(ImageView)findViewById(R.id.imageView);
                bi.gallery.setSpacing(1);
                final GalleryImageAdapter galleryImageAdapter = new GalleryImageAdapter(this, files);
                bi.gallery.setAdapter(galleryImageAdapter);


                bi.gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        // show the selected Image
                        //selectedImage.setImageResource(galleryImageAdapter.mImageIds[position]);
                        bi.imageView.setBackground(Drawable.createFromPath(galleryImageAdapter.mImageIds[position]));
                    }
                });
            }
        } else {

            try {
                //sdDir.mkdirs();
                Log.d("Files", "Directory Created" + sdDir.mkdirs());

            } catch (SecurityException e) {
                Log.d("Files", e.getMessage());

            }

            Log.d("Files", "0");

        }
    }

    //public File[] GetPhotos(){

    /*     *//*String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = baseDir + "/Android/data/edu.aku.hassannaqvi.csvdownloader/files/";*//*
        String path = sdDir;
        if (sdDir.exists()) {
            File[] files = sdDir.listFiles(file -> (file.getPath().endsWith(".jpg") || file.getPath().endsWith(".jpeg")));

        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
//        Log.d("Files", "Size: "+ files.length);*/

    //      return files;

        /*for (int i = 0; i < files.length; i++)
        {

            Log.d("Files", "FileName:" + files[i].getName());
        }
        return files;*/

//    }
}