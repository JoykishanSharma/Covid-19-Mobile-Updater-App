package com.joyappsdevteam.covid_19tracer;

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
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneVerificationActivity extends AppCompatActivity {

    private EditText reg_name_editText, reg_mobile_no_editText;
    private CardView reg_submit;
    private TextView whyNeedData;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_verify_activity);

        reg_name_editText = findViewById(R.id.reg_name);
        reg_mobile_no_editText = findViewById(R.id.reg_mobile_no);
        reg_submit = findViewById(R.id.submit_cardView);
        whyNeedData = findViewById(R.id.why_need_data);


        mAuth = FirebaseAuth.getInstance();

        whyNeedData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //terms and condition activity here
            }
        });

        reg_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reg_name = reg_name_editText.getText().toString().trim();
                String reg_mobile_no = reg_mobile_no_editText.getText().toString().trim();

                if (TextUtils.isEmpty(reg_name)) {
                    reg_name_editText.setError("Email Address is Empty");
                } else if (TextUtils.isEmpty(reg_mobile_no)) {
                    reg_mobile_no_editText.setError("Password is Empty");
                } else {
                    if (isConnected()) {
                        //ask permission -- Internet -- User Location -- Phone Call --
                        //check registration and send OTP
                        //custom enter OTP activity
                        //also fetch current state he is living in
                        //Register
                        //registerAccount(reg_name,reg_mobile_no);
                        //Store user phone no, Name, state to database via Firebase

                        //For Now
                        startActivity(new Intent(PhoneVerificationActivity.this,HomeActivity.class));
                    } else {
                        Toast.makeText(PhoneVerificationActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
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

    private void registerAccount(String email, String password){
        //phone Verification via Firebase
    }

    private void savedLoginState(){
        SharedPreferences sp = getSharedPreferences("logged_in", MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putBoolean("is_logged", true);
        et.apply();
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

