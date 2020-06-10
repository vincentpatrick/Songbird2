package com.example.songbird;

import android.content.Context;
import android.view.SurfaceView;

public class gameView extends SurfaceView implements Runnable {
    //initialize thread
    private Thread thread;
    private boolean isPlaying; //check whether game is still playing or not
    public gameView(Context context) {
        super(context);
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

    }

    private void draw(){


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
