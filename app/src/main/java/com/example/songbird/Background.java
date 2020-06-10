package com.example.songbird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.songbird.R;

public class Background {
    int X = 0 ,Y = 0;
    Bitmap background;
    Background(int screenX, int screenY, Resources res) {
        //create constructor for background class
        background = BitmapFactory.decodeResource(res, R.drawable.background);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
        
    }
}
