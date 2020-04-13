package com.joyappsdevteam.covid_19tracer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PreventionActivity extends AppCompatActivity {

    private ImageView backButton;
    private CardView clickForNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevention);

        backButton = findViewById(R.id.back_arrow5);
        clickForNow = findViewById(R.id.clickForNow);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clickForNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.testing_for_prevention_activity);
            }
        });
    }
}
