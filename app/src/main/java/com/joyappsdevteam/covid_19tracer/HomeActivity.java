package com.joyappsdevteam.covid_19tracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private Handler mWaitHandler = new Handler();
    private CardView homeToMapCardView,homeToInfoCardView,homeToNewsCardView,
            all_symptoms,all_preventions,all_nearby_covid19_hospital,report_patient;
    private ImageView settings_image;
    private TextView see_detail_map,more_helpline_nos,call_helpline_no,helpful_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        homeToMapCardView = findViewById(R.id.home_to_map_cardview);
        homeToInfoCardView = findViewById(R.id.home_to_info_cardview);
        homeToNewsCardView = findViewById(R.id.home_to_news_cardview);
        see_detail_map = findViewById(R.id.see_detail_map);
        more_helpline_nos = findViewById(R.id.more_helpline_nos);
        call_helpline_no = findViewById(R.id.call_helpline_no);
        helpful_text = findViewById(R.id.helpful_text);
        all_symptoms = findViewById(R.id.all_symptoms);
        all_preventions = findViewById(R.id.all_preventions);
        all_nearby_covid19_hospital = findViewById(R.id.all_nearby_covid19_hospital);
        report_patient = findViewById(R.id.report_patient);
        settings_image = findViewById(R.id.settings);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;

                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.info:
                        startActivity(new Intent(getApplicationContext(),InfoActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(),NewsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        homeToMapCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,MapsActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        homeToInfoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,InfoActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        homeToNewsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,NewsActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        see_detail_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,MapsActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        more_helpline_nos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nearest Police Station number with address and all necessary contact list
                //List of Helpline numbers of all start
                //with direct call phone
            }
        });

        call_helpline_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialogAlert for confirming the call
                //Direct call from App
            }
        });

        all_symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open Symptoms Activity
                //add subActivities if needed
                //with images and videos
            }
        });

        all_preventions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open Prevention Activity
                //add subActivities if needed
                //with images and videos
            }
        });

        all_nearby_covid19_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //List All nearby Covid19 hospital, clinics, helps,
                //Try to add map if possible
            }
        });

        report_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Report new Suspicious cases in your locality,
                //share location for the suspect
                //full history, everything in last 20days -- meetups, party, reunions, pets, everything
            }
        });

        settings_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open settings activity
                //terms and conditions
                //privacy policies
                //dark mode if possible
                //contributions and attributes to sites and links
                //about us
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Close App")
                .setMessage("Are you sure you want to close this App?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            endTask();
                            onDestroy();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void endTask() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        }
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
        mWaitHandler.removeCallbacksAndMessages(null);
    }

}
