package com.example.songbird;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make main activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        //when play button is clicked, then we will start the game
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, gameActivity.class));
            }
        });

        //SHOW HIGH SCORE
        TextView highscoreTxt = findViewById(R.id.highScoreTxt);
        final SharedPreferences preferences = getSharedPreferences("game",MODE_PRIVATE);
        highscoreTxt.setText("HighScore: "+preferences.getInt("high score",0));
    }
}
