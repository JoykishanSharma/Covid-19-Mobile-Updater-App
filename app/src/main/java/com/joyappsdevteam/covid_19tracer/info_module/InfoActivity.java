package com.joyappsdevteam.covid_19tracer.info_module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.joyappsdevteam.covid_19tracer.R;
import com.joyappsdevteam.covid_19tracer.home_module.HomeActivity;
import com.joyappsdevteam.covid_19tracer.maps_module.MapsActivity;
import com.joyappsdevteam.covid_19tracer.news_module.NewsActivity;

public class InfoActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    CardView myth_busters_cardview,related_videos_cardview,faqs_cardView,healthy_parenting_cardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        myth_busters_cardview = findViewById(R.id.myth_busters_cardview);
        related_videos_cardview = findViewById(R.id.related_videos_cardview);
        faqs_cardView = findViewById(R.id.faqs_cardView);
        healthy_parenting_cardview = findViewById(R.id.healthy_parenting_cardview);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.info);

        related_videos_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    Intent i = new Intent(InfoActivity.this,InfoWebViewActivity.class);
                    i.putExtra("WhichWebViewToShow","related_videos");
                    startActivity(i);
                }
                else Toast.makeText(InfoActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });

        myth_busters_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    Intent i = new Intent(InfoActivity.this,InfoWebViewActivity.class);
                    i.putExtra("WhichWebViewToShow","myth_busters");
                    startActivity(i);
                }
                else Toast.makeText(InfoActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });

        faqs_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    Intent i = new Intent(InfoActivity.this,InfoWebViewActivity.class);
                    i.putExtra("WhichWebViewToShow","faqs");
                    startActivity(i);
                }
                else Toast.makeText(InfoActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });

        healthy_parenting_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    Intent i = new Intent(InfoActivity.this,InfoWebViewActivity.class);
                    i.putExtra("WhichWebViewToShow","healthy_parenting");
                    startActivity(i);
                }
                else Toast.makeText(InfoActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        return true;

                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        return true;

                    case R.id.info:
                        return true;

                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        return true;
                }
                return false;
            }
        });

    }

    private boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        } else
            return false;
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(InfoActivity.this,HomeActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }
}
