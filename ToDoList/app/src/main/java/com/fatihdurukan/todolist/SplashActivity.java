package com.fatihdurukan.todolist;

/**
 * Created by fatihdurukan on 20.12.2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fatihdurukan.todolist.SqliteHelper;

public class SplashActivity extends android.support.v7.app.AppCompatActivity {
    SqliteHelper mySqliteHelper;

    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        imageView.startAnimation(animation);



        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                    mySqliteHelper = new SqliteHelper(SplashActivity.this);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();


    }
}