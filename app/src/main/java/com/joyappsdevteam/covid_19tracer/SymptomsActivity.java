package com.joyappsdevteam.covid_19tracer;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Objects;

public class SymptomsActivity extends AppCompatActivity {

    private ImageView back_arrow;
    private CardView symptoms_fever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        back_arrow = findViewById(R.id.back_arrow3);
        symptoms_fever = findViewById(R.id.symptoms_fever);


        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        symptoms_fever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSymptomDetails();
            }
        });
    }

    private void showSymptomDetails(){
        final Dialog alertDialog = new Dialog(SymptomsActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.symptoms_detail);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView symptomsName = alertDialog.findViewById(R.id.symptoms_name);
        ImageView closeButton = alertDialog.findViewById(R.id.closeAlertDialog);
        TextView symptomsOverview = alertDialog.findViewById(R.id.fever_overview);
        TextView symptomWhatToLookTitle = alertDialog.findViewById(R.id.symptoms_what_to_look_title);
        TextView symptomWhatToLook = alertDialog.findViewById(R.id.symptom_what_to_look);
        TextView symptomWhenToSeeADoctorTitle = findViewById(R.id.symptom_when_to_see_a_doctor_title);
        TextView symptomWhenToSeeADoctor = findViewById(R.id.symptom_what_to_see_a_doctor);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        alertDialog.setCancelable(false);
    }
}
