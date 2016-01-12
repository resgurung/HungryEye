package com.example.robin.hungryeye;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    Animation animation,animationFade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = (ImageView)findViewById(R.id.imageSplashView);
        //assign the animation object
        animation = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);
        //fade when animation finish
        animationFade = AnimationUtils.loadAnimation(getBaseContext(),R.anim.abc_fade_out);
        //start the animation
        imageView.startAnimation(animation);
        //animation listener
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //fade the animation when finish
                imageView.startAnimation(animationFade);
                //when animation finish start a intent to go to main activity
                finish();
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
