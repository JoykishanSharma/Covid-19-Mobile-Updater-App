package com.joyappsdevteam.covid_19tracer.info_module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.joyappsdevteam.covid_19tracer.R;
import com.joyappsdevteam.covid_19tracer.home_module.HomeActivity;
import com.joyappsdevteam.covid_19tracer.maps_module.MapsActivity;
import com.joyappsdevteam.covid_19tracer.news_module.NewsActivity;

public class InfoActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView mail_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.info);
        mail_temp = findViewById(R.id.mail_temp);

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

        //Email the Developer using Intent
        mail_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL,new String[] {"joyappsdevteam@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT,"Query Related to \"Covid-19 Tracer App\"");
                        i.putExtra(Intent.EXTRA_TEXT, "Email message here...");
                        try {
                            startActivity(Intent.createChooser(i,"Send mail..."));
                        }
                        catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(InfoActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(InfoActivity.this,HomeActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }
}
