package com.example.songbird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.songbird.gameView.screenRatioX;
import static com.example.songbird.gameView.screenRatioY;

public class ghost {
    public int speed=35;
    public boolean wasShot= true;
    int x=0, y , width, height, ghostCounter =1;

    Bitmap ghost1, ghost2;

    //constructor that accepts resources
    ghost(Resources res){
        //set animation
        ghost1 = BitmapFactory.decodeResource(res, R.drawable.ghost1);
        ghost2 = BitmapFactory.decodeResource(res, R.drawable.ghost2);

        //make the ghost smaller to fit the screen
        width = ghost1.getWidth();
        height = ghost2.getHeight();

        width /=6;
        height /=6;
        //for compatability multiply with screen ratio
        width =(int)(width*screenRatioX);
        height =(int)(height*screenRatioY);


        //resize bitmap
        ghost1 =Bitmap.createScaledBitmap(ghost1, width,height,false);
        ghost2 =Bitmap.createScaledBitmap(ghost2, width,height,false);

        y =-height;//ghost place off screen in the start


    }

    Bitmap getGhost(){
    //create an animation
        //if counter is 1, set the first ghost bitmap
        if(ghostCounter==1){
            ghostCounter++;// increase counter value
            return ghost1;
        }
        //if counter is 2, set the second ghost bitmap
        if(ghostCounter==2) {
            ghostCounter++;
            return ghost2;
        }
        ghostCounter =1;
        return ghost1;
    }

    //THIS FUNCTION CREATES  a rectangle around the ghost
    Rect getCollisionShape(){
        return new Rect(x, y,x+width,y+height);
    }
}
