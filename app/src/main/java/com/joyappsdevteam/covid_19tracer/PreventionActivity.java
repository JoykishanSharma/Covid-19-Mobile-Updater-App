package com.joyappsdevteam.covid_19tracer;

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

import java.util.Objects;

public class PreventionActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backButton;
    private CardView preventionWashHand,preventionSocialDistance,preventionCoverMouthWhenSneeze,preventionWearFacemask,
        preventionDontTouchFace,preventionStayAtHome,preventionDoSomeExercise,preventionsEatHealthy,preventionCleanAndDisinfect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevention);

        backButton = findViewById(R.id.back_arrow5);

        cardViewReferencing();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showPreventionDetails(int pName, int pLargeImage, int pData, int pTopic1, int pAns1, int pTopic2, int pAns2, int pTopic3, int pAns3) {
        final Dialog alertDialog = new Dialog(PreventionActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.preventions_details);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView preventionName = alertDialog.findViewById(R.id.prevention_name);
        ImageView closeButton = alertDialog.findViewById(R.id.closeAlertDialog);
        ImageView symptomDetailLargeImage = alertDialog.findViewById(R.id.prevention_detail_large_image);
        TextView preventionData = alertDialog.findViewById(R.id.prevention_data);
        TextView preventionDetailsTopic1 = alertDialog.findViewById(R.id.prevention_details_topic1);
        TextView preventionDetailsAns1 = alertDialog.findViewById(R.id.prevention_details_ans1);
        TextView preventionDetailsTopic2 = alertDialog.findViewById(R.id.prevention_details_topic2);
        TextView preventionDetailsAns2 = alertDialog.findViewById(R.id.prevention_details_ans2);
        TextView preventionDetailsTopic3 = alertDialog.findViewById(R.id.prevention_details_topic3);
        TextView preventionDetailsAns3 = alertDialog.findViewById(R.id.prevention_details_ans3);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        preventionName.setText(pName);
        symptomDetailLargeImage.setImageResource(pLargeImage);
        preventionData.setText(pData);
        preventionDetailsTopic1.setText(pTopic1);
        preventionDetailsAns1.setText(pAns1);
        preventionDetailsTopic2.setText(pTopic2);
        preventionDetailsAns2.setText(pAns2);
        preventionDetailsTopic3.setText(pTopic3);
        preventionDetailsAns3.setText(pAns3);

        alertDialog.show();
        alertDialog.setCancelable(false);
    }

    private void cardViewReferencing() {
        preventionWashHand = findViewById(R.id.prevention_wash_hand);
        preventionWashHand.setOnClickListener(this);
        preventionSocialDistance = findViewById(R.id.prevention_social_distance);
        preventionSocialDistance.setOnClickListener(this);
        preventionCoverMouthWhenSneeze = findViewById(R.id.prevention_cover_mouth_when_sneeze);
        preventionCoverMouthWhenSneeze.setOnClickListener(this);
        preventionWearFacemask = findViewById(R.id.prevention_wear_facemask);
        preventionWearFacemask.setOnClickListener(this);
        preventionDontTouchFace = findViewById(R.id.prevention_dont_touch_face);
        preventionDontTouchFace.setOnClickListener(this);
        preventionStayAtHome = findViewById(R.id.prevention_stay_at_home);
        preventionStayAtHome.setOnClickListener(this);
        preventionDoSomeExercise = findViewById(R.id.prevention_do_some_exercise);
        preventionDoSomeExercise.setOnClickListener(this);
        preventionsEatHealthy = findViewById(R.id.preventions_eat_healthy);
        preventionsEatHealthy.setOnClickListener(this);
        preventionCleanAndDisinfect = findViewById(R.id.prevention_clean_and_disinfect);
        preventionCleanAndDisinfect.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.prevention_wash_hand:
                showPreventionDetails(R.string.wash_hand_prevention_name,R.drawable.clean_your_hands_often,
                        R.string.wash_hand_prevention_data, R.string.wash_hand_prevention_details_topic1,
                        R.string.wash_hand_prevention_details_ans1, R.string.wash_hand_prevention_details_topic2,
                        R.string.wash_hand_prevention_details_ans2, R.string.wash_hand_prevention_details_topic3,
                        R.string.wash_hand_prevention_details_ans3);
                break;

            case R.id.prevention_social_distance:
                showPreventionDetails(R.string.social_distancing_prevention_name,R.drawable.avoid_close_contact,
                        R.string.social_distancing_prevention_data, R.string.social_distancing_prevention_details_topic1,
                        R.string.social_distancing_prevention_details_ans1, R.string.social_distancing_prevention_details_topic2,
                        R.string.social_distancing_prevention_details_ans2, R.string.social_distancing_prevention_details_topic3,
                        R.string.social_distancing_prevention_details_ans3);
                break;

            case R.id.prevention_cover_mouth_when_sneeze:
                showPreventionDetails(R.string.cover_your_mouth_prevention_name,R.drawable.cover_coughs_and_sneezes,
                        R.string.cover_your_mouth_prevention_data, R.string.cover_your_mouth_prevention_details_topic1,
                        R.string.cover_your_mouth_prevention_details_ans1, R.string.cover_your_mouth_prevention_details_topic2,
                        R.string.cover_your_mouth_prevention_details_ans2, R.string.cover_your_mouth_prevention_details_topic3,
                        R.string.cover_your_mouth_prevention_details_ans3);
                break;

            case R.id.prevention_wear_facemask:
                showPreventionDetails(R.string.wear_facemask_prevention_name,R.drawable.wear_a_facemask_if_you_are_sick,
                        R.string.wear_facemask_prevention_data, R.string.wear_facemask_prevention_details_topic1,
                        R.string.wear_facemask_prevention_details_ans1, R.string.wear_facemask_prevention_details_topic2,
                        R.string.wear_facemask_prevention_details_ans2, R.string.wear_facemask_prevention_details_topic3,
                        R.string.wear_facemask_prevention_details_ans3);
                break;

            case R.id.prevention_dont_touch_face:
                showPreventionDetails(R.string.avoid_touching_face_prevention_name,R.drawable.dont_touch_your_face,
                        R.string.avoid_touching_face_prevention_data, R.string.avoid_touching_face_prevention_details_topic1,
                        R.string.avoid_touching_face_prevention_details_ans1, R.string.avoid_touching_face_prevention_details_topic2,
                        R.string.avoid_touching_face_prevention_details_ans2, R.string.avoid_touching_face_prevention_details_topic3,
                        R.string.avoid_touching_face_prevention_details_ans3);
            break;

            case R.id.prevention_stay_at_home:
                showPreventionDetails(R.string.stay_home_stay_safe_prevention_name,R.drawable.stay_at_home,
                        R.string.stay_home_stay_safe_prevention_data, R.string.stay_home_stay_safe_prevention_details_topic1,
                        R.string.stay_home_stay_safe_prevention_details_ans1, R.string.stay_home_stay_safe_prevention_details_topic2,
                        R.string.stay_home_stay_safe_prevention_details_ans2, R.string.stay_home_stay_safe_prevention_details_topic3,
                        R.string.stay_home_stay_safe_prevention_details_ans3);
                break;

            case R.id.prevention_do_some_exercise:
                showPreventionDetails(R.string.do_some_exercise_prevention_name,R.drawable.do_some_exercise,
                        R.string.do_some_exercise_prevention_data, R.string.do_some_exercise_prevention_details_topic1,
                        R.string.do_some_exercise_prevention_details_ans1, R.string.do_some_exercise_prevention_details_topic2,
                        R.string.do_some_exercise_prevention_details_ans2, R.string.do_some_exercise_prevention_details_topic3,
                        R.string.do_some_exercise_prevention_details_ans3);
                break;

            case R.id.preventions_eat_healthy:
                showPreventionDetails(R.string.stay_hygiene_eat_healthy_prevention_name,R.drawable.stay_at_home,
                        R.string.stay_hygiene_eat_healthy_prevention_data, R.string.stay_hygiene_eat_healthy_prevention_details_topic1,
                        R.string.stay_hygiene_eat_healthy_prevention_details_ans1, R.string.stay_hygiene_eat_healthy_prevention_details_topic2,
                        R.string.stay_hygiene_eat_healthy_prevention_details_ans2, R.string.stay_hygiene_eat_healthy_prevention_details_topic3,
                        R.string.stay_hygiene_eat_healthy_prevention_details_ans3);
                break;

            case R.id.prevention_clean_and_disinfect:
                showPreventionDetails(R.string.clean_and_disinfect_prevention_name,R.drawable.clean_and_disinfect,
                        R.string.clean_and_disinfect_prevention_data, R.string.clean_and_disinfect_prevention_details_topic1,
                        R.string.clean_and_disinfect_prevention_details_ans1, R.string.clean_and_disinfect_prevention_details_topic2,
                        R.string.clean_and_disinfect_prevention_details_ans2, R.string.clean_and_disinfect_prevention_details_topic3,
                        R.string.clean_and_disinfect_prevention_details_ans3);
                break;

            default:
                break;
        }
    }


}
