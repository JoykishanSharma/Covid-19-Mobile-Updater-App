package com.joyappsdevteam.covid_19tracer.home_module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.joyappsdevteam.covid_19tracer.WelcomeActivity;
import com.joyappsdevteam.covid_19tracer.info_module.InfoActivity;
import com.joyappsdevteam.covid_19tracer.maps_module.MapsActivity;
import com.joyappsdevteam.covid_19tracer.news_module.NewsActivity;
import com.joyappsdevteam.covid_19tracer.R;
import com.joyappsdevteam.covid_19tracer.maps_module.WebViewWorldActivity;
import com.joyappsdevteam.covid_19tracer.settings_module.SettingsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    //Global Variables reference
    BottomNavigationView bottomNavigationView;
    private Handler mWaitHandler = new Handler();
    private CardView homeToMapCardView, homeToInfoCardView, homeToNewsCardView,
            all_symptoms, all_preventions;
    private ImageView settings_image;
    private TextView see_detail_map, more_helpline_nos, call_helpline_no, helpful_text,
            confirm_cases_india, recover_cases_india, death_cases_india, last_update_india_textView,
            confirm_cases_state_level, recover_cases_state_level, death_cases_state_level, last_update_state_level,
            your_state_name, text_username,state_helpline_text,state_helpline_number;
    private RequestQueue requestQueue;
    private String currentUserStateHelpline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Attaching variables with xml "phone_verify_activity.xml" views
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
        state_helpline_text = findViewById(R.id.state_helpline_text);
        state_helpline_number = findViewById(R.id.state_helpline_number);


        //When this Activity is called, we check for internet connect so that we can update the Covid-19 cases
        //If there is no internet connection, a AlertDialog will be displayed
        //If there is a active internet connection, this Activity will continue to execute as it is
        if (!isConnected()){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("No Internet Connection")
                    .setMessage("Please connect to Internet and Relaunch the App")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //closing the app and destroying all background process
                            endTask();
                            onDestroy();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }

        //Volley is an HTTP library that makes networking for Android apps easier to connect.
        //For More Go to --> https://developer.android.com/training/volley
        //here we are initalizing a requestQueue with the help Volley.
        requestQueue = Volley.newRequestQueue(this);

        //udating covid cases
        updateData();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;

                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;

                    case R.id.info:
                        startActivity(new Intent(getApplicationContext(), InfoActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;

                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                }
                return false;
            }
        });

        final String[] updateText = {
                "Help out the elderly by bringing them their groceries and other essentials.",
                "Stand against FAKE news and illegit WhatsApp forwards! Do NOT forward a message until you verify the content it contains.",
                "There is no evidence that hot weather will stop the virus! You can! Stay home, stay safe.",
                "Our brothers from the North-East are just as Indian as you! Help everyone during this crisis.",
                "Plan ahead! Take a minute and check how much supplies you have at home. Planning lets you buy exactly what you need. ",
                "If you have any medical queries, reach out to your state helpline, district administration or trusted doctors!",
                "The virus does not discriminate. Why do you? DO NOT DISCRIMINATE. We are all Indians!",
                "Help the medical fraternity by staying at home!",
                "Get in touch with your local NGO's and district administration to volunteer for this cause.",
                "If you have symptoms and suspect you have coronavirus - reach out to your doctor or call state helplines. Get help.",
                "This will pass too. Enjoy your time at home and spend quality time with your family! Things will be normal soon."
        };
        final Handler handler = new Handler();
        handler.post(new Runnable() {

            int randomNumber = new Random().nextInt(10);

            @Override
            public void run() {
                helpful_text.setText(updateText[randomNumber]);
                randomNumber = new Random().nextInt(10);
                /*if (i == updateText.length) {
                    handler.removeCallbacks(this);
                } else {*/
                    //10 sec
                    handler.postDelayed(this, 1000 * 10);

            }
        });

        homeToMapCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    startActivity(new Intent(HomeActivity.this, WebViewWorldActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else Toast.makeText(HomeActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });

        homeToInfoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, InfoActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        homeToNewsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NewsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        see_detail_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isConnected()){
                    startActivity(new Intent(HomeActivity.this, MapsActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else Toast.makeText(HomeActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();

            }
        });

        more_helpline_nos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, HelplineNumberActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        call_helpline_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUserStateHelpline.equals("")){
                    Toast.makeText(HomeActivity.this,"Couldn't find helpline.\nPLease Refresh!",Toast.LENGTH_SHORT).show();
                }else {
                    //show dialogAlert for confirming the call
                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("Direct Call")
                            .setMessage("Are you sure you want to call this number?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with call operation
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:" + currentUserStateHelpline));
                                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                                                Manifest.permission.CALL_PHONE)) {
                                            ActivityCompat.requestPermissions(HomeActivity.this,
                                                    new String[] {Manifest.permission.CALL_PHONE,
                                                            Manifest.permission.INTERNET},
                                                    1);
                                        }
                                        return;
                                    }
                                    startActivity(callIntent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        all_symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open Symptoms Activity
                startActivity(new Intent(HomeActivity.this,SymptomsActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        all_preventions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open Prevention Activity
                startActivity(new Intent(HomeActivity.this,PreventionActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        settings_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open settings activity
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            }
        });

        helpful_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomNumber = new Random().nextInt(10);
                helpful_text.setText(updateText[randomNumber]);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
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
                            your_state_name.setText(toTitleCase(currentState));
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

    //retrieving user details from shared Preference and setting them to username and State name TextViews
    private void updateData(){
        SharedPreferences sp = getSharedPreferences("UserDetails", MODE_PRIVATE);
        String username = sp.getString("username", null);
        String currentLocation = sp.getString("location", null);

        if (username.equals(null)) username = "Your Name";
        if (currentLocation.equals(null)) currentLocation = "Your State";

        text_username.setText(toTitleCase(username));
        state_helpline_text.setText(currentLocation + " helpline");
        showStateHelplineNumber(currentLocation);
        parseJsonData(currentLocation);
    }

    private void showStateHelplineNumber(String stateName){
        switch (stateName) {

            case "Andhra Pradesh":
                currentUserStateHelpline = "08662410978";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Arunachal Pradesh":
                currentUserStateHelpline = "09436055743";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Assam":
                currentUserStateHelpline = "06913347770";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Bihar":
            case "Gujarat ":
            case "Goa":
            case "Himachal Pradesh":
            case "Jharkhand":
            case "Karnataka":
            case "Madhya Pradesh":
            case "Punjab":
            case "Sikkim":
            case "Telangana":
            case "Uttarakhand":
            case "Dadra and Nagar Haveli":
            case "Daman and Diu":
            case "Lakshadweep":
            case "Puducherry":
            case "Chhattisgarh":
                currentUserStateHelpline = "104";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Haryana":
                currentUserStateHelpline = "08558893911";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Kerala":
                currentUserStateHelpline = "04712552056";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Maharashtra":
                currentUserStateHelpline = "02026127394";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Manipur":
                currentUserStateHelpline = "03852411668";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Meghalaya":
                currentUserStateHelpline = "108";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Mizoram":
                currentUserStateHelpline = "102";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Nagaland":
                currentUserStateHelpline = "07005539653";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Odisha":
                currentUserStateHelpline = "09439994859";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Rajasthan":
                currentUserStateHelpline = "01412225624";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Tamil Nadu":
                currentUserStateHelpline = "04429510500";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Tripura":
                currentUserStateHelpline = "03812315879";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Uttar Pradesh":
                currentUserStateHelpline = "18001805145";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "West Bengal":
                currentUserStateHelpline = "1800313444222";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Andaman and Nicobar Islands":
                currentUserStateHelpline = "03192232102";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Chandigarh":
                currentUserStateHelpline = "09779558282";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Delhi":
                currentUserStateHelpline = "01122307145";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Jammu and Kashmir":
                currentUserStateHelpline = "01912520982";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            case "Ladakh":
                currentUserStateHelpline = "01982256462";
                state_helpline_number.setText(currentUserStateHelpline);
                break;

            default:
                currentUserStateHelpline = "";
                break;
        }
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
    protected void onStart() {
        super.onStart();
        updateData();
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
                            //onDestroy();
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

    @Override
    public void onResume(){
        super.onResume();

        if (!isConnected()){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("No Internet Connection")
                    .setMessage("Please connect to Internet and Relaunch the App")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            endTask();
                            onDestroy();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        else updateData();


    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

}
