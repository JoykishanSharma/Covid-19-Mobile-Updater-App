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
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends AppCompatActivity {

    private EditText reg_mobile_no_editText;
    private TextView resend_otp,send_otp_text,textView;
    private LinearLayout resend_otp_linearLayout;
    private CardView reg_send_otp;
    private Boolean firstTime = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_verify_activity);

        //Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        reg_mobile_no_editText = findViewById(R.id.reg_phone);
        reg_send_otp = findViewById(R.id.send_otp_cardView);
        resend_otp = findViewById(R.id.resend_otp);
        send_otp_text = findViewById(R.id.send_otp_text);
        resend_otp_linearLayout = findViewById(R.id.resend_otp_linearLayout);
        textView = findViewById(R.id.textView);



        reg_send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reg_mobile_no = reg_mobile_no_editText.getText().toString().trim();

               if (TextUtils.isEmpty(reg_mobile_no) || reg_mobile_no.length() < 10) {
                    reg_mobile_no_editText.setError("Valid Number is required");
                } else {
                    if (isConnected()) {
                        //ask permission -- Internet -- User Location -- Phone Call --
                        //check registration and send OTP
                        //custom enter OTP activity
                        if (firstTime){
                            send_otp_text.setText("Enter OTP sent to " + "+91" + reg_mobile_no);
                            firstTime = false;
                        }

                        reg_mobile_no_editText.setText("");
                        reg_mobile_no_editText.setHint(" * * * * * ");
                        reg_mobile_no_editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(6)});
                        resend_otp_linearLayout.setVisibility(View.VISIBLE);

                        if (textView.getText().equals("Generate OTP")){

                        }


                        if (textView.getText().equals("VERIFY")){
                            Toast.makeText(PhoneVerificationActivity.this, "Successful", Toast.LENGTH_SHORT).show();

                        }

                        textView.setText("VERIFY");


                        //also fetch current state he is living in
                        //Register
                        //registerAccount(reg_name,reg_mobile_no);
                        //Store user phone no, Name, state to database via Firebase

                        //For Now
                        //startActivity(new Intent(PhoneVerificationActivity.this,HomeActivity.class));
                    } else {
                        Toast.makeText(PhoneVerificationActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_otp_text.setText("We will send you a One Time Password on this mobile number");
                reg_mobile_no_editText.setText("");
                reg_mobile_no_editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
                reg_mobile_no_editText.setHint("Enter Mobile Number");
                resend_otp_linearLayout.setVisibility(View.INVISIBLE);
                textView.setText("Generate OTP");
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

