package com.example.songbird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.example.songbird.gameView.screenRatioX;
import static com.example.songbird.gameView.screenRatioY;

public class songbird {
    public boolean is_going_up = false; //check if bird is going up
    int x, y, width, height;
    int wingcounter=0;
    Bitmap bird1, bird2; //by replacing back and forth from bird1 and 2, it shows  an animation where the birds wings are flapping
    //constructor
    songbird(int screenY, Resources res){
        //initialize bird 1 and bird2
        bird1 = BitmapFactory.decodeResource(res, R.drawable.songbird1);
        bird2 = BitmapFactory.decodeResource(res, R.drawable.songbird2);

        //get the width and height of the bird
        width= bird1.getWidth();
        height =bird1.getHeight();

        //the image of bird is too big, so we reduce the size
        width /=4;
        height /=4;

        //multiply the width and height with its screenratio to make it compatible with different devices
        width*=(int)screenRatioX;
        height*=(int)screenRatioY;

        //resize bitmap
        bird1 = Bitmap.createScaledBitmap(bird1, width,height, false);
        bird2 = Bitmap.createScaledBitmap(bird2, width,height, false);

        //initially be centre vertically
        y = screenY/2;
        x=(int)(64 *screenRatioX);


    }
    Bitmap getBird(){
        if(wingcounter==0){
            wingcounter++;
            return bird1;
        }
        wingcounter--;
        return bird2;
    }

}
