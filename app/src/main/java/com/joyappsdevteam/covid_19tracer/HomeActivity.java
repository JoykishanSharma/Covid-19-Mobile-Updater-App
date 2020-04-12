package com.joyappsdevteam.covid_19tracer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private Handler mWaitHandler = new Handler();
    private CardView homeToMapCardView,homeToInfoCardView,homeToNewsCardView,
            all_symptoms,all_preventions;
    private ImageView settings_image;
    private TextView see_detail_map,more_helpline_nos,call_helpline_no,helpful_text,
            confirm_cases_india,recover_cases_india,death_cases_india,last_update_india_textView,
            confirm_cases_state_level,recover_cases_state_level,death_cases_state_level,last_update_state_level,your_state_name,text_username;
    private RequestQueue requestQueue;
    private String userCurrentState;

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
        settings_image = findViewById(R.id.settings);
        confirm_cases_india = findViewById(R.id.confirm_cases_india);
        recover_cases_india = findViewById(R.id.recover_cases_india);
        death_cases_india = findViewById(R.id.death_cases_india);
        last_update_india_textView = findViewById(R.id.last_update_india_textView);
        confirm_cases_state_level = findViewById(R.id.confirm_cases_state_level);
        recover_cases_state_level = findViewById(R.id.recover_cases_state_level);
        death_cases_state_level = findViewById(R.id.death_cases_state_level);
        last_update_state_level = findViewById(R.id.last_update_state_textView);
        your_state_name = findViewById(R.id.your_state_name);
        text_username = findViewById(R.id.text_username);


        requestQueue = Volley.newRequestQueue(this);


        SharedPreferences sp = getSharedPreferences("UserDetails", MODE_PRIVATE);
        String username = sp.getString("username", null);
        String currentLocation = sp.getString("location", null);

        if (username.equals(null)) username = "Your Name";
        if (currentLocation.equals(null)) currentLocation = "Your State";

        text_username.setText(username);
        parseJsonData(currentLocation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;

                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        return true;

                    case R.id.info:
                        startActivity(new Intent(getApplicationContext(),InfoActivity.class));
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        return true;

                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(),NewsActivity.class));
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        return true;
                }
                return false;
            }
        });

        homeToMapCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,WebViewWorldActivity.class));
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

                startActivity(new Intent(HomeActivity.this,HelplineNumberActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
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
                startActivity(new Intent(HomeActivity.this,SymptomsActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
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

        settings_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //testing
                //startActivity(new Intent(HomeActivity.this,TakeUsernameAndLocationActivity.class));


                //open settings activity
                //terms and conditions
                //privacy policies
                //dark mode if possible
                //contributions and attributes to sites and links
                //about us
            }
        });
    }

    private void parseJsonData(final String currentState) {
        String url = "https://api.covid19india.org/data.json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("statewise");

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject indianState = jsonArray.getJSONObject(i);

                        String state = indianState.getString("state");
                        String lastUpdatedTime = indianState.getString("lastupdatedtime");
                        String confirmedCases = indianState.getString("confirmed");
                        String recoveredCases = indianState.getString("recovered");
                        String deathCases = indianState.getString("deaths");

                        if (state.equals("Total")){
                            last_update_india_textView.setText("Last Update : " + lastUpdatedTime);
                            confirm_cases_india.setText(confirmedCases);
                            recover_cases_india.setText(recoveredCases);
                            death_cases_india.setText(deathCases);
                        }

                        if (state.equals(currentState)){
                            your_state_name.setText(currentState.toUpperCase());
                            last_update_state_level.setText("Last Update : " + lastUpdatedTime);
                            confirm_cases_state_level.setText(confirmedCases);
                            recover_cases_state_level.setText(recoveredCases);
                            death_cases_state_level.setText(deathCases);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
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
