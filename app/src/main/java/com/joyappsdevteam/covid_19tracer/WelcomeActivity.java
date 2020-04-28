package com.joyappsdevteam.covid_19tracer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.joyappsdevteam.covid_19tracer.authentication_module.PhoneVerificationActivity;
import com.joyappsdevteam.covid_19tracer.authentication_module.TakeUsernameAndLocationActivity;
import com.joyappsdevteam.covid_19tracer.home_module.HomeActivity;

public class WelcomeActivity extends AppCompatActivity {

    //A handle class mainly deal with the task which need to be executed on a single thread
    //For more Detail, Go to https://developer.android.com/reference/android/os/Handler
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

                //The following code will execute after the 2 seconds.
                try {

                    //Retrieving the values from SharedPreference objects.
                    SharedPreferences sharedPreferences = getSharedPreferences("phoneVerified", MODE_PRIVATE);
                    boolean isPhoneVerificationComplete = sharedPreferences.getBoolean("phoneVerifiedComplete", false);

                    SharedPreferences sharedPref = getSharedPreferences("UserDetails", MODE_PRIVATE);
                    boolean IsUserDetailsSaved = sharedPref.getBoolean("user_details", false);

                    //Checking if PhoneVerification is Done and User Details is also taken from user or not or Vice Versa
                    if (isPhoneVerificationComplete && IsUserDetailsSaved){

                        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                        startActivity(intent);

                        //This is Optional
                        //This is a small piece of code to give a animation affect when moving from one Activity to another
                        overridePendingTransition(R.anim.custom_slide_in_right,R.anim.custom_slide_out_left);
                    }
                    else if(isPhoneVerificationComplete && !IsUserDetailsSaved){
                        Intent intent = new Intent(WelcomeActivity.this, TakeUsernameAndLocationActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.custom_slide_in_right,R.anim.custom_slide_out_left);
                    }
                    else {
                        Intent intent = new Intent(WelcomeActivity.this, PhoneVerificationActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }

                    //Let's Finish Splash Activity since we don't want to show this when user press back button.
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000);  // Give a 2 seconds delay.
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
        mWaitHandler.removeCallbacksAndMessages(null);
    }
}
