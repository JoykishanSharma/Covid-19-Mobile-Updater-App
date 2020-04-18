package com.joyappsdevteam.covid_19tracer.home_module;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.joyappsdevteam.covid_19tracer.R;

import java.util.Objects;

public class SymptomsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back_arrow;
    private CardView symptomsFever,symptomsDryCough,symptomsShortnessOfBreath,symptomsAchesAndPains,
            symptomsHeadache,symptomsRunningNose,symptomsSoreThroat,symptomsDiarrhea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        back_arrow = findViewById(R.id.back_arrow3);
        cardViewReferencing();

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showSymptomDetails(int sName, int sLargeImage, int sOverview, int sWhatToLook, int sWhenToWhenToSeeADoctor) {
        final Dialog alertDialog = new Dialog(SymptomsActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.symptoms_detail);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView symptomsName = alertDialog.findViewById(R.id.symptoms_name);
        ImageView closeButton = alertDialog.findViewById(R.id.closeAlertDialog);
        ImageView symptomDetailLargeImage = alertDialog.findViewById(R.id.symptom_detail_large_image);
        TextView symptomsOverview = alertDialog.findViewById(R.id.fever_overview);
        TextView symptomWhatToLook = alertDialog.findViewById(R.id.symptom_what_to_look);
        TextView symptomWhenToSeeADoctor = alertDialog.findViewById(R.id.symptom_what_to_see_a_doctor);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        symptomsName.setText(sName);
        symptomDetailLargeImage.setImageResource(sLargeImage);
        symptomsOverview.setText(sOverview);
        symptomWhatToLook.setText(sWhatToLook);
        symptomWhenToSeeADoctor.setText(sWhenToWhenToSeeADoctor);

        alertDialog.show();
        alertDialog.setCancelable(false);
    }

    private void cardViewReferencing() {
        symptomsFever = findViewById(R.id.symptoms_fever);
        symptomsFever.setOnClickListener(this);
        symptomsDryCough = findViewById(R.id.symptoms_DryCough);
        symptomsDryCough.setOnClickListener(this);
        symptomsShortnessOfBreath = findViewById(R.id.symptoms_shortnessOfBreath);
        symptomsShortnessOfBreath.setOnClickListener(this);
        symptomsAchesAndPains = findViewById(R.id.symptoms_achesAndPains);
        symptomsAchesAndPains.setOnClickListener(this);
        symptomsHeadache = findViewById(R.id.symptoms_headache);
        symptomsHeadache.setOnClickListener(this);
        symptomsRunningNose = findViewById(R.id.symptoms_running_nose);
        symptomsRunningNose.setOnClickListener(this);
        symptomsSoreThroat = findViewById(R.id.symptoms_sore_throat);
        symptomsSoreThroat.setOnClickListener(this);
        symptomsDiarrhea = findViewById(R.id.symptoms_diarrhea);
        symptomsDiarrhea.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.symptoms_fever:
                showSymptomDetails(R.string.fever_symptom_name,R.drawable.fever_image,R.string.fever_data_overview,
                        R.string.fever_data_what_to_look,R.string.fever_data_when_to_see_a_doctor);
                break;

            case R.id.symptoms_DryCough:
               showSymptomDetails(R.string.dry_cough_symptom_name,R.drawable.cough_image,R.string.dry_cough_data_overview,
                       R.string.dry_cough_data_what_to_look,R.string.dry_cough_data_when_to_see_a_doctor);
                break;

            case R.id.symptoms_shortnessOfBreath:
                showSymptomDetails(R.string.shortness_of_breath_symptom_name,R.drawable.shortness_of_breath_image,
                        R.string.shortness_of_breath_data_overview,R.string.shortness_of_breath_data_what_to_look,
                        R.string.shortness_of_breath_data_when_to_see_a_doctor);
                break;

            case R.id.symptoms_achesAndPains:
                showSymptomDetails(R.string.aches_and_pains_symptom_name,R.drawable.ache_and_pain_image,
                        R.string.aches_and_pains_data_overview,R.string.aches_and_pains_data_what_to_look,
                        R.string.aches_and_pains_data_when_to_see_a_doctor);
                break;

            case R.id.symptoms_headache:
                showSymptomDetails(R.string.headache_symptom_name,R.drawable.headache_image,R.string.headache_data_overview,
                        R.string.headache_data_what_to_look,R.string.headache_data_when_to_see_a_doctor);
                break;

            case R.id.symptoms_running_nose:
                showSymptomDetails(R.string.running_nose_symptom_name,R.drawable.running_nose_image,R.string.running_nose_data_overview,
                        R.string.running_nose_data_what_to_look,R.string.running_nose_data_when_to_see_a_doctor);
                break;

            case R.id.symptoms_sore_throat:
                showSymptomDetails(R.string.sore_throat_symptom_name,R.drawable.sore_throat_image,R.string.sore_throat_data_overview,
                        R.string.sore_throat_data_what_to_look,R.string.shortness_of_breath_data_when_to_see_a_doctor);
                break;

            case R.id.symptoms_diarrhea:
                showSymptomDetails(R.string.diarrhea_symptom_name,R.drawable.diarrhea_image,R.string.diarrhea_data_overview,
                        R.string.diarrhea_data_what_to_look,R.string.diarrhea_data_when_to_see_a_doctor);
                break;

            default:
                break;
        }
    }


}
