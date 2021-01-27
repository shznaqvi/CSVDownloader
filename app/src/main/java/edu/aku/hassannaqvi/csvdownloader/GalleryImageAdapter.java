package edu.aku.hassannaqvi.csvdownloader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.io.File;

public class GalleryImageAdapter extends BaseAdapter {


    private final Context mContext;
    public String[] mImageIds;

    public GalleryImageAdapter(Context context, File[] files) {
        mContext = context;
        mImageIds = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            mImageIds[i] = files[i].getName();
            Log.d("Files", "FileName:" + files[i].getName());
        }
    }

    public int getCount() {
        return mImageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // Override this method according to your need
    public View getView(int index, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(mContext);

        i.setBackground(Drawable.createFromPath(mImageIds[index]));
        //i.setImageResource(mImageIds[index]);
        i.setLayoutParams(new Gallery.LayoutParams(200, 200));

        i.setScaleType(ImageView.ScaleType.FIT_XY);


        return i;
    }

}