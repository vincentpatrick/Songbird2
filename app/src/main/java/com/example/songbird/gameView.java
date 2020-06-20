package com.example.songbird;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//surfaceview is used when you have to change screen content very quickly
public class gameView extends SurfaceView implements  Runnable{
    private Thread thread; //initialize a thread
    private boolean isPlaying, isGameOver = false;//a variable to check if game is still playing or not
    private int screenX, screenY; //a variable for the screen on our x and our y axis
    //variable made public and static so that it can be accessable from other java class
    private int high_score;//a variable that keeps track of high score
    public static float screenRatioX, screenRatioY;//a variable to check the screen ratio, different phone have different screen ratio
    private songbird bird;
    private Paint paint;
    private ghost[] ghosts;//an array of ghost
    private Background background1, background2; //create a background variable
    //we need 2 backgroundinstances to help the background music
    private List<Bullet> bullets;//create a list of bullets
    private Random rand;
    private SharedPreferences preferences;
    private gameActivity activity;

    //gameview constructor
    public gameView(gameActivity activity, int screenX, int screenY){
        super(activity);

        this.activity = activity;
        preferences =activity.getSharedPreferences("game",Context.MODE_PRIVATE);

        //initialize the screen and the background
        this.screenX= screenX;
        this.screenY = screenY;
        screenRatioX = 1920f/screenX ;//screenratio is the 1920pixels in the x axis divided by its size in x axis
        screenRatioY = 1080f/screenY;//screenratio is the 1080pixels in the y axis divided by its size in y axis

        //set the background
        background1= new Background(screenX, screenY, getResources());
        //second background wont be on the screen, it will be placed when our screen ends on the x axis
        background2= new Background(screenX, screenY, getResources());

        bird = new songbird(this,screenY, getResources());

        background2.X=screenX;

        //initialize new paint object in the constructor
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.RED);

        //initiallize bullet list in the constructor
        bullets = new ArrayList<>();

        //initialize ghost array
        ghosts =new ghost[4];//there will be 4 ghost in the screen at a time

        for(int i =0; i<4;i++){
            ghost ghost = new ghost(getResources());
            //add ghost to its ghost array
            ghosts[i]= ghost;
        }
        //intialize randomclass
        rand = new Random();
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
        //check if bird is going up, if yes reduce its y axis value by 30 and multiply with screen ratio y
        if(bird.is_going_up)
            bird.y-=30*screenRatioY;
        else
            bird.y+=30*screenRatioY;
        //avoid bird from going offscreen

        //if bird reach the top of the screen, then set the birds y axis to 0, so that it stays on screen
        if(bird.y<0)
            bird.y =0;
        //if bird goes offscreen from the bottom, set the bird to stay at the bottom of the screen
        if(bird.y>=screenY - bird.height)
        bird.y = screenY-bird.height;

        List<Bullet> garbage = new ArrayList<>(); // list for the removed bullets
        for (Bullet bullet: bullets){
            //check if bullet goes off screen
            if(bullet.x> screenX)
                //add offscreen bullets to the garbage
                garbage.add(bullet);
            //move the bullet 50 pixels to the right
            bullet.x +=50*screenRatioX;

            //run through each ghost and check if bullet hits the ghost
            for (ghost ghost:ghosts){
                //if bullet collides with ghost
                if(Rect.intersects(ghost.getCollisionShape(),bullet.getCollisionShape())) {
                    //reward the player with one point for shooting a ghost
                    high_score++;
                    ghost.x = -500;//ghost goes offscreen
                    bullet.x = screenX + 500; //bullet also goes offscreen
                    //the bullet shoots the ghost
                    ghost.wasShot = true;
                    //if the bullet misses the ghost, the game will end
                }
            }
        }
        //remove every bullets that are available in the garbage list
        for (Bullet bullet:garbage)
            bullets.remove(bullet);
        for(ghost ghost:ghosts) {
            //set how fast do you want the ghost to go
            ghost.x -= ghost.speed;

            if (ghost.x + ghost.width < 0) {//check if the ghost go offscreen
                /*
                if(!ghost.wasShot){//ghost is not shot but is still off the screen
                    isGameOver = true;
                    return;
                }*/
                //set the speed limit of the ghost to 30
                int bound = (int) (30 * screenRatioX);
                ghost.speed = rand.nextInt(bound);//gives a random speed for our bird
                //there's a chance the speed of the ghost to be 0 and we dont want that
                if (ghost.speed < 10 * screenRatioX) {
                    //set the speed of the ghost to 10
                    ghost.speed = (int) (10 * screenRatioX);
                }
                ghost.x = screenX;//ghost will come from the right side
                ghost.y = rand.nextInt(screenY - ghost.height);//ghost will appear from random position
                ghost.wasShot=false;
            }

            if(Rect.intersects(ghost.getCollisionShape(), bird.getCollisionShape())){
                //if bird and ghost collides
                isGameOver= true;
                return;
            }
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

            //draw ghost
            for(ghost ghost:ghosts)
                canvas.drawBitmap(ghost.getGhost(),ghost.x,ghost.y,paint);
            //Display the high score
            canvas.drawText(high_score+"",screenX/2f,170,paint);
            if(isGameOver){
                //if game is over,set is playing to false which will break the thread
                isPlaying = false;
                //draw a dead songbird bitmap
                canvas.drawBitmap(bird.getDead(),bird.x,bird.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting();
                return;
            }

            canvas.drawBitmap(bird.getBird(),bird.x, bird.y, paint);
            //another for loop to iterate through list of bullets
            for(Bullet bullet: bullets)
                //draw the bullet
                canvas.drawBitmap(bullet.bullet,bullet.x, bullet.y,paint);

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

    //give player control over songbird
    //gives player the ability to go up and down
    @Override
    public boolean onTouchEvent(MotionEvent event){
        //based on the player action, use switch case
        switch(event.getAction()){
            //if user takes his thumb off ,bird will go down
            case MotionEvent.ACTION_DOWN:
                if(event.getX()<screenX/2){
                    bird.is_going_up =true;
                }
                break;
            case MotionEvent.ACTION_UP:
                bird.is_going_up =false;
                if(event.getX()> screenX/2){
                    //touchevent for shooting bullet comes from right side of the screen
                    bird.toShoot++;
                }
                break;

        }
        return true;
    }

    private void saveIfHighScore(){
        //save high score
        if(preferences.getInt("high score", 0)<high_score){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("high score", high_score);
            editor.apply();
        }
    }
    private void waitBeforeExiting(){
        //theprogram will wait
        try {
            Thread.sleep(3000);//thread sleep for 3 seconds
            activity.startActivity(new Intent(activity,MainActivity.class));//set an intent to change to main activity
            activity.finish();//after 3 seconds finish the game activity
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void newBullet() {
        Bullet bullet = new Bullet(getResources());
        //set the position where bullet is going to shoot
        bullet.x= bird.x +bird.width;
        bullet.y = bird.y+(bird.height/2);
        bullets.add(bullet);
    }
}
