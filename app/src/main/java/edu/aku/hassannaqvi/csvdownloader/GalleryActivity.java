package edu.aku.hassannaqvi.csvdownloader;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import edu.aku.hassannaqvi.csvdownloader.databinding.ActivityGalleryBinding;

public class GalleryActivity extends AppCompatActivity {

    ImageView selectedImage;
    ActivityGalleryBinding bi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_gallery);


        bi.setCallback(this);

        //Gallery gallery = (Gallery) findViewById(R.id.gallery);
        //selectedImage=(ImageView)findViewById(R.id.imageView);
        bi.gallery.setSpacing(1);
        final GalleryImageAdapter galleryImageAdapter = new GalleryImageAdapter(this);
        bi.gallery.setAdapter(galleryImageAdapter);


        bi.gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // show the selected Image
                selectedImage.setImageResource(galleryImageAdapter.mImageIds[position]);
            }
        });
    }
}