package com.xapps.karbala.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class CommonImageTarget implements Target {


    private ImageView imageView;


    public CommonImageTarget(ImageView imageView ) {
        this.imageView = imageView;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }



    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
    }
}
