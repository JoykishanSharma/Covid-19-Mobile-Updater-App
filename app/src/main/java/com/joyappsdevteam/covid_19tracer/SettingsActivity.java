package com.joyappsdevteam.covid_19tracer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    UserDetails userDetails;
    private ImageView backButton;
    private EditText newName, newEmail;
    private AppCompatSpinner spinner;
    private String newLocation_settings;
    private TextView mobileNumberSettings,aboutUs,attributes,privacy_policy,terms_and_conditions;
    private Button saveChanges;
    private String mobileNo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        backButton = findViewById(R.id.back_arrow4);
        newName = findViewById(R.id.new_name);
        newEmail = findViewById(R.id.new_email);
        spinner = findViewById(R.id.settings_spinner);
        mobileNumberSettings = findViewById(R.id.mobile_number_settings);
        saveChanges = findViewById(R.id.save_changes);
        aboutUs = findViewById(R.id.aboutUs);
        attributes = findViewById(R.id.attributes);
        privacy_policy = findViewById(R.id.privacy_policy);
        terms_and_conditions = findViewById(R.id.terms_and_conditions);

        SharedPreferences sharedPref = getSharedPreferences("UserDetails", MODE_PRIVATE);
        String oldName = sharedPref.getString("username", null);
        String oldEmail = sharedPref.getString("emailAddress", null);
        String oldLocation = sharedPref.getString("location", null);

        SharedPreferences sharedPreferences = getSharedPreferences("phoneVerified", MODE_PRIVATE);
        mobileNo = sharedPreferences.getString("mobile_no", null);

        assert oldName != null;
        newName.setText(toTitleCase(oldName));
        newEmail.setText(oldEmail);
        mobileNumberSettings.setText("+91-" + mobileNo + "  ");

        List<String> categories = new ArrayList<>();
        categories.add(0, "Select your state");
        categories.add("Andhra Pradesh");
        categories.add("Arunachal Pradesh");
        categories.add("Assam");
        categories.add("Bihar");
        categories.add("Chhattisgarh");
        categories.add("Goa");
        categories.add("Gujarat");
        categories.add("Haryana");
        categories.add("Himachal Pradesh");
        categories.add("Jharkhand");
        categories.add("Karnataka");
        categories.add("Kerala");
        categories.add("Madhya Pradesh");
        categories.add("Maharashtra");
        categories.add("Manipur");
        categories.add("Meghalaya");
        categories.add("Mizoram");
        categories.add("Nagaland");
        categories.add("Odisha");
        categories.add("Punjab");
        categories.add("Rajasthan");
        categories.add("Sikkim");
        categories.add("Tamil Nadu");
        categories.add("Telangana");
        categories.add("Tripura");
        categories.add("Uttarakhand");
        categories.add("Uttar Pradesh");
        categories.add("West Bengal");
        categories.add("Andaman and Nicobar Islands");
        categories.add("Chandigarh");
        categories.add("Dadra and Nagar Haveli");
        categories.add("Daman and Diu");
        categories.add("Delhi");
        categories.add("Jammu and Kashmir");
        categories.add("Ladakh");
        categories.add("Lakshadweep");
        categories.add("Puducherry");

        //Style and populate spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);

        //Dropdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setSelection(dataAdapter.getPosition(oldLocation));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    newLocation_settings = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNameToSave = newName.getText().toString().trim();
                String newEmailToSave = newEmail.getText().toString().trim();

                if (TextUtils.isEmpty(newNameToSave)) {
                    newName.setError("empty");
                } else if (TextUtils.isEmpty(newEmailToSave) || !Patterns.EMAIL_ADDRESS.matcher(newEmailToSave).matches()) {
                    newEmail.setError("Invalid Email Address");
                } else if (newLocation_settings.equals("Select your state")) {
                    Toast.makeText(SettingsActivity.this, "Select your location", Toast.LENGTH_SHORT).show();
                } else {
                    if (isConnected()) {
                        savedUserDetailsNew(newNameToSave, newEmailToSave, newLocation_settings);
                        Toast.makeText(getApplicationContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(SettingsActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,AboutUsActivity.class));
            }
        });

        attributes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    Intent intent = new Intent(SettingsActivity.this, WebViewActivity.class);
                    intent.putExtra("content_to_show", "privacy_policy");
                    startActivity(intent);
                }else Toast.makeText(SettingsActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });

        terms_and_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    Intent intent = new Intent(SettingsActivity.this, WebViewActivity.class);
                    intent.putExtra("content_to_show", "terms_and_conditions");
                    startActivity(intent);
                }else Toast.makeText(SettingsActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savedUserDetailsNew(String name, String email, String location) {
        SharedPreferences sp = getSharedPreferences("UserDetails", MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putBoolean("user_details", true);
        et.putString("username", name);
        et.putString("emailAddress", email);
        et.putString("location", location);
        et.apply();

        //creating UserDetails Object
        userDetails = new UserDetails();
        userDetails.setName(name);
        userDetails.setEmail(email);
        userDetails.setLocation(location);
        userDetails.setMobile(mobileNo);

        // storing data to firebase database
        final DatabaseReference availReff = FirebaseDatabase.getInstance().getReference().child("UserDetails");
        availReff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                availReff.push().setValue(userDetails);
                Log.i("data Saved to ", "Firebase Database");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do nothing
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
}
