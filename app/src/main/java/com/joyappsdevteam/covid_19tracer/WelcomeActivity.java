package com.joyappsdevteam.covid_19tracer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private Handler mWaitHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeScreen5Seconds();
    }

    private void welcomeScreen5Seconds(){
        mWaitHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                //The following code will execute after the 5 seconds.
                try {

                    //Go to next page i.e, start the next activity.
                    SharedPreferences sharedPreferences = getSharedPreferences("logged_in", MODE_PRIVATE);
                    boolean logged_in = sharedPreferences.getBoolean("is_logged", false);

                    if (logged_in){
                        Intent intent = new Intent(WelcomeActivity.this,HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.custom_slide_in_right,R.anim.custom_slide_out_left);
                    }
                    else {
                        Intent intent = new Intent(WelcomeActivity.this,HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }

                    //Let's Finish Splash Activity since we don't want to show this when user press back button.
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 5000);  // Give a 3 seconds delay.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
        mWaitHandler.removeCallbacksAndMessages(null);
    }
}
