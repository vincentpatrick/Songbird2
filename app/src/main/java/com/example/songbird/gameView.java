package com.example.songbird;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

//surfaceview is used when you have to change screen content very quickly
public class gameView extends SurfaceView implements  Runnable{
    private Thread thread; //initialize a thread
    private boolean isPlaying;//a variable to check if game is still playing or not
    private int screenX, screenY; //a variable for the screen on our x and our y axis
    private float screenRatioX, screenRatioY;//a variable to check the screen ratio, different phone have different screen ratio

    private Paint paint;
    private Background background1, background2; //create a background variable
    //we need 2 backgroundinstances to help the background music
    //gameview constructor
    public gameView(Context context, int screenX, int screenY){
        super(context);

        //initialize the screen and the background
        this.screenX= screenX;
        this.screenY = screenY;
        screenRatioX = 1920f/screenX ;//screenratio is the 1920pixels in the x axis divided by its size in x axis
        screenRatioY = 1080f/screenY;//screenratio is the 1080pixels in the y axis divided by its size in y axis

        //set the background
        background1= new Background(screenX, screenY, getResources());
        //background wont be on the screen, it will be placed when our screen ends on the x axis
        background2= new Background(screenX, screenY, getResources());

        background2.X=screenX;

        //initialize new paint object in the constructor
        paint = new Paint();
    }

    @Override
    public void run() {
        //create a while loop and this loop will run only while is still playing
        while(isPlaying) {
            //give initial position to the
            update();
            draw();
            sleep();
        }
    }

    private void update(){
        //change the position of our background on the x axis by 10 pixel
        //y axis will stay the same
        background1.X -=10*screenRatioX;// by multiplaying 10 with screenratioX, this 10 will be made compatible on the number for the screen
        background2.X -=10*screenRatioX;//by multiplaying 10 with screenratioY, this 10 will be made compatible on the number for the screen
        //background will be move by 10 pixel towards the left
        //soon our background will go off screen, at that time, we place the background again after the screen ends
        if(background1.X +background1.background.getWidth()<0){
            background1.X = screenX;
        }
        if(background2.X +background2.background.getWidth()<0){
            background2.X = screenX;
        }
    }

    private void draw(){
        //draw background on the canvas
        //ensure that surface object has been successfully initiated
        if(getHolder().getSurface().isValid()){
            //return current canvas that is being displayed on the screen
            Canvas canvas =getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.X, background1.Y, paint);
            canvas.drawBitmap(background2.background, background2.X, background2.Y, paint);
            //this uses the canvast to draw on the screen
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep(){
        //wait for 17 milliseconds
        //divide 1 second with 17 millisecond so that it returns 60 frames per second
        //therefore this runs in 60fps
        //in 1 second,update position of image nd update it 60 times
        try {
            Thread.sleep(17);

        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void resume(){
        isPlaying =true; //set to true when game is running
        //initialize the thread object
        thread = new Thread(this);
        //starting the thread will call the run function
        thread.start();
    }

    public void pause(){
        //pause when our game is called

        //terminate the thread

        try{
            isPlaying = false;//set to false when the game pauses
            thread.join(); //calling this terminate the thread
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }


}
