package com.example.dodrone.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.dodrone.DoDroneActivity;
import com.example.dodrone.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    /*
    //constant time delay (2500 means 2.5 sec)
    private final int SPLASH_DELAY = 2500;

    //Fields (Widgets)
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setBackgroundDrawable(null);

        //Methods to call
        initializeView();
        animateLogo();
        goToMainActivity();
    }

    private void initializeView() {
        imageView = findViewById(R.id.logo);
    }

    private void animateLogo() {
        //method to animate the logo
        Animation fadingInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadingInAnimation.setDuration(SPLASH_DELAY);

        imageView.startAnimation(fadingInAnimation);
    }
    private void goToMainActivity() {
        //this method will take user to main after animation
        new  Handler(Looper.myLooper()).postDelayed(()->{
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_DELAY);

    }*/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
