package com.example.songbird;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

public class gameActivity extends AppCompatActivity {
    //gameview class
    private gameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make the activity to be full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //get the screen size
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        //instantiate gameview object
        //get the size of screen on x axis and the y axis
        gameView = new gameView(this, point.x, point.y);

        setContentView(gameView);
    }
    @Override
    //pause gameview
    protected void onPause(){
       // when activity pauses, game is pause
        super.onPause();
        gameView.pause();
    }
    @Override
    //start gameview
    protected void onResume(){
        //when activity resumes, game is resume
        super.onResume();
        gameView.resume();
    }

}
