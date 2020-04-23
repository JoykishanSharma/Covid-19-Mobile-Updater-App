package com.joyappsdevteam.covid_19tracer.authentication_module;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joyappsdevteam.covid_19tracer.home_module.HomeActivity;
import com.joyappsdevteam.covid_19tracer.R;
import java.util.ArrayList;
import java.util.List;

public class TakeUsernameAndLocationActivity extends AppCompatActivity {

    //Variable Declaration
    private AppCompatSpinner spinner;
    private EditText regName,regEmail;
    private CardView lets_go_cardView;
    private String selectedItem;
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_username_and_location);

        //hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Attaching variables with xml "activity_take_username_and_location.xml" views
        spinner = findViewById(R.id.spinner);
        regName = findViewById(R.id.reg_name);
        regEmail = findViewById(R.id.reg_email);
        lets_go_cardView = findViewById(R.id.lets_go_cardView);

        //Creating A ArrayList of type String to store the names of Indian State and Union Territories
        List<String> categories = new ArrayList<>();
        categories.add(0,"Select your state");
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

        //Creating ArrayAdapter for given ArrayList with each item having the attributes of "spinner_item.xml"
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,categories);

        //Dropdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //taking selected item name to "selectedItem" variable
                selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });

        //setting up onClickListener on the Let's Go CardView
        lets_go_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //retrieving typed Username and Email Address from editText to a String variables
                String userName = regName.getText().toString().trim();
                String userEmail = regEmail.getText().toString().trim();

                //Checking if the Email Address is empty or not
                if (TextUtils.isEmpty(userName)) {
                    regName.setError("empty");
                }
                //Checking if the Email Address is empty or not OR Email Address is a proper mail address or not
                else if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    regEmail.setError("Invalid Email Address");
                }

                else if (selectedItem.equals("Select your state")) {
                    Toast.makeText(TakeUsernameAndLocationActivity.this,"Select your location",Toast.LENGTH_SHORT).show();
                } else {
                    //Checking if the App is connected to internet of not
                    if (isConnected()) {
                        //We Save the Username, Email Address and User Location(current state) for displaying it in Settings module
                        savedUserDetail(userName,userEmail,selectedItem);

                        //starting "HomeActivity" using implicit Intent
                        startActivity(new Intent(TakeUsernameAndLocationActivity.this, HomeActivity.class));
                    } else
                        Toast.makeText(TakeUsernameAndLocationActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //This Method checks the Connectivity state of app in real-time
    //For more details, Go to https://developer.android.com/reference/android/net/ConnectivityManager
    private boolean isConnected() {
        //here "cm" object is pointing to the Connective service of the phone
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //checks if "cm" is null or not.
        //if "cm" is null, this method will terminate and continue with outer methods
        //else continue as it is.
        assert cm != null;

        //here "netinfo" store the information about the active Connected Network.
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        //checks if "netinfo" is null or not and also Checks the state of connectivity
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {

            //determines and checks for type of connectivity from where the network is stable
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            //returns true if connection is available and false if connection is not available
            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        } else
            return false;
    }

    private void savedUserDetail(String name, String email, String location) {

        //Retrieving the values from SharedPreference objects.
        SharedPreferences sharedPreferences = getSharedPreferences("phoneVerified", MODE_PRIVATE);
        final String mobileNo = sharedPreferences.getString("mobile_no",null);

        //Storing data in App directory using SharedPreference
        SharedPreferences sp = getSharedPreferences("UserDetails", MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putBoolean("user_details", true);
        et.putString("username",name);
        et.putString("emailAddress",email);
        et.putString("location",location);
        et.apply();

        //creating UserDetails Object and putting values into it
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
                //"userDetails" object is pushed of stored into firebase database
                 availReff.push().setValue(userDetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do nothing
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit Application")
                .setMessage("Are you sure you want to exit?")
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
}