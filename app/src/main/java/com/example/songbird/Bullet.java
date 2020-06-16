package com.example.songbird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.example.songbird.gameView.screenRatioX;
import static com.example.songbird.gameView.screenRatioY;

public class Bullet {
    int x, y ;
    Bitmap bullet;

    Bullet(Resources res){
        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet);
        //get width and height of the bullet
        int width = bullet.getWidth();
        int height = bullet.getHeight();


        width/=4;
        height/=4;

        //make compatible to different screen
        width*=(int)screenRatioX;
        height*=(int)screenRatioY;

        //resize the bullet
        bullet = Bitmap.createScaledBitmap(bullet, width, height,false);

    }
}
