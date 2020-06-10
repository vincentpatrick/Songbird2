package com.example.songbird;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class gameView extends SurfaceView implements Runnable {
    //initialize thread
    private Thread thread;
    private boolean isPlaying; //check whether game is still playing or not
    private int screenX, screenY;
    private Paint paint;
    private Background background1, background2;
    public gameView(Context context , int screenX, int screenY) {
        super(context);

        this.screenX= screenX;
        this.screenY = screenY;
        background1= new Background(screenX, screenY, getResources());
        background2= new Background(screenX, screenY, getResources());
        background2.X=screenX;

        //initialize new paint object in the constructor
        paint = new Paint();
    }
    @Override
    public void run(){
        while(isPlaying){
            update();
            draw();
            sleep();
        }
    }

    private void update(){
        //change the position of our background on the x axis by 10 pixel
        //y axis will stay the same
        background1.X -=10;
        background2.X-=10;
        //background will be move by 10 pixel towards the left
        //soon our background will go off screen, at that time, we place the backgroung again
        if(background1.X +background1.background.getWidth()<0){
            background1.X = screenX;
        }
        if(background2.X +background2.background.getWidth()<0){
            background2.X = screenX;
        }
    }

    private void draw(){
        //draw background on the canvas
        //ensure that surface object has been successfully initated
        if(getHolder().getSurface().isValid()){
            //return current canvas that is being displayed on the screen
            Canvas canvas =getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.X, background2.Y, paint);
            canvas.drawBitmap(background2.background, background2.X, background2.Y, paint);
            //this uses the canvast to draw on the screen
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep(){
        //we wait for 17 milliseconds
        //divide 1 second with 17 millisecond so that it returns 60 frames per second
        try {
            Thread.sleep(17);

        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void resume(){
        thread = new Thread(this);
        //starting the thread will call the run function
        thread.start();
    }

    public void pause(){
        //pause when our game is called

        //terminate the thread

        try{
            isPlaying = false;
            thread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

}
