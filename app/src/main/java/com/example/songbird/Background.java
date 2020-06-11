package com.example.songbird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    int X = 0 ,Y = 0;
    Bitmap background;
    Background(int screenX, int screenY, Resources res) {
        //create constructor for background class
        //this take size of screen on x and y axis
        //it also take resource object to be used to decode the bitmap from drawable folder
        background = BitmapFactory.decodeResource(res, R.drawable.background);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);//resize background to fit in the entire screen
    }
}
