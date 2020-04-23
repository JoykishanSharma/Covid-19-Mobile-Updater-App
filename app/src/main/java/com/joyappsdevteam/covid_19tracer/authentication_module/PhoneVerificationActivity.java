package com.joyappsdevteam.covid_19tracer.authentication_module;

import android.Manifest;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.joyappsdevteam.covid_19tracer.R;
import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends AppCompatActivity {

    //Variable Declarations
    String verificationCodeBySystem;
    private EditText reg_mobile_no_editText, otp_verification_EditText;
    private TextView resend_otp, send_otp_text, textView;
    private LinearLayout resend_otp_linearLayout;
    private CardView reg_send_otp, verify_otp_cardView;
    private Boolean verificationError = false;
    private ProgressBar progress_circular;
    private String userPhoneNumber;
    private int MULTIPLE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_verify_activity);

        //Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Attaching variables with xml "phone_verify_activity" views
        reg_mobile_no_editText = findViewById(R.id.reg_phone);
        reg_send_otp = findViewById(R.id.send_otp_cardView);
        resend_otp = findViewById(R.id.resend_otp);
        send_otp_text = findViewById(R.id.send_otp_text);
        resend_otp_linearLayout = findViewById(R.id.resend_otp_linearLayout);
        textView = findViewById(R.id.textView);
        progress_circular = findViewById(R.id.progress_circular);
        otp_verification_EditText = findViewById(R.id.otp_verification);
        verify_otp_cardView = findViewById(R.id.verify_otp_cardView);


        //setting up onClickListener on the Send OTP CardView
        reg_send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //retrieving typed Mobile number from editText to a String variable
                String reg_mobile_no = reg_mobile_no_editText.getText().toString().trim();

                //Checking if the Mobile number is empty or less than 10 digit
                if (TextUtils.isEmpty(reg_mobile_no) || reg_mobile_no.length() < 10) {
                    reg_mobile_no_editText.setError("Valid Number is required");
                } else {
                    //Checking if the App is connected to internet of not
                    if (isConnected()) {
                        //checking if Phone Call permission is granted by Android or not
                        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                        Manifest.permission.CALL_PHONE) +
                                ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.INTERNET)
                                == PackageManager.PERMISSION_GRANTED) {

                            //making progress bar visible to User
                            progress_circular.setVisibility(View.VISIBLE);

                            //method call for sending OTP to Mobile Number for Verification
                            sendVerificationCode(reg_mobile_no);

                            //Checking if there were any problem while verification
                            if (!verificationError) {

                                // We are reusing the same xml layout for OTP input by making few Views Visible and Invisible

                                //Changing title text
                                send_otp_text.setText("Enter the OTP code sent at +91" + reg_mobile_no);

                                //Making Mobile Number editText Invisible and OTP editText Visible
                                reg_mobile_no_editText.setVisibility(View.INVISIBLE);
                                otp_verification_EditText.setVisibility(View.VISIBLE);

                                //Making "SEND OTP cardView" Invisible and "Verify OTP" cardView Visible
                                reg_send_otp.setVisibility(View.INVISIBLE);
                                verify_otp_cardView.setVisibility(View.VISIBLE);

                                //making "Resend OTP" Views Visible
                                resend_otp_linearLayout.setVisibility(View.VISIBLE);

                                //Storing Mobile Number to a String variable for future reference
                                userPhoneNumber = reg_mobile_no;
                            }
                        }
                        //if the Phone Call permission is not granted, the App will request for Phone Call permission
                        else {
                            requestRuntimePermission();
                        }
                    //if the App is not Connected with Internet, it will display this Toast message
                    } else {
                        Toast.makeText(PhoneVerificationActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //setting up onClickListener on the Verify OTP CardView
        verify_otp_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //retrieving typed OTP from editText to a String variable
                String verificationCodeByUser = otp_verification_EditText.getText().toString().trim();

                //making progress bar visible to User
                progress_circular.setVisibility(View.VISIBLE);

                //Checking if the verification Code typed by User is empty or less than 6 digit
                if (TextUtils.isEmpty(verificationCodeByUser) || verificationCodeByUser.length() < 6) {
                    reg_mobile_no_editText.setError("Valid Number is required");
                } else {
                    //Checking if the App is connected to internet of not
                    if (isConnected()) {
                        //Code Typed by User is verified with this method call
                        verifyCode(verificationCodeByUser);
                    }
                    //if the App is not Connected with Internet, it will display this Toast message
                    else
                        Toast.makeText(PhoneVerificationActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //setting up onClickListener on the Resend OTP TextView
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // We are reusing the same xml layout for OTP input by making few Views Visible and Invisible

                //Changing title text
                send_otp_text.setText("We will send you a One Time Password on this mobile number");

                //Making Mobile Number editText Visible and OTP editText Invisible
                reg_mobile_no_editText.setVisibility(View.VISIBLE);
                otp_verification_EditText.setVisibility(View.INVISIBLE);

                //Making "SEND OTP cardView" Visible and "Verify OTP" cardView Invisible
                reg_send_otp.setVisibility(View.VISIBLE);
                verify_otp_cardView.setVisibility(View.INVISIBLE);

                //making "Resend OTP" Views Invisible
                resend_otp_linearLayout.setVisibility(View.INVISIBLE);

                //making progress bar Invisible to User
                progress_circular.setVisibility(View.INVISIBLE);


            }
        });
    }

    private void requestRuntimePermission() {

        /* Android provides a utility method, "shouldShowRequestPermissionRationale()", that returns true
        * if the Android has previously denied the request, and returns false if a user has denied a permission
        * and selected the Don't ask again option in the permission request dialog. */

        //To know More about Runtime Permission request, Go to https://developer.android.com/training/permissions/requesting

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {

            //Showing AlertDialog for explaining "Why we need this Permission in the App?"
            new AlertDialog.Builder(PhoneVerificationActivity.this)
                    .setTitle("Permission needed")
                    .setMessage("These permissions are needed for the app to call Helpline Number directly on a Button Click.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Requesting User to give Direct Phone Call Permission and Internet Access Permission(Optional)
                            ActivityCompat.requestPermissions(PhoneVerificationActivity.this,
                                    new String[] {Manifest.permission.CALL_PHONE,
                                            Manifest.permission.INTERNET},
                                    MULTIPLE_PERMISSION_CODE);
                            // MULTIPLE_PERMISSION_CODE is an
                            // app-defined int constant. The callback method "onRequestPermissionsResult" gets the
                            // result of the request.
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();


        } else {
            //Requesting User to give Direct Phone Call Permission and Internet Access Permission(Optional)
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CALL_PHONE,
                            Manifest.permission.INTERNET},
                    MULTIPLE_PERMISSION_CODE);
        }
    }

    //This Method gets Call Automatically afer "requestRuntimePermission" method for checking purpose
    //This Method Checks if the User has Granted permission or Not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MULTIPLE_PERMISSION_CODE)  {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //Permission Granted
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void sendVerificationCode(String reg_mobile_no) {
        //
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + reg_mobile_no,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack
            = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {

                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            verificationError = true;
            Toast.makeText(PhoneVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String codeByUser) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);
        otp_verification_EditText.setText("******");

    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(PhoneVerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            savedVerifyState(userPhoneNumber);
                            progress_circular.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(PhoneVerificationActivity.this, TakeUsernameAndLocationActivity.class));
                        } else {
                            verificationError = true;
                            Toast.makeText(PhoneVerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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

    private void savedVerifyState(String UserNumber) {
        SharedPreferences sp = getSharedPreferences("phoneVerified", MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putBoolean("phoneVerifiedComplete", true);
        et.putString("mobile_no",UserNumber);
        et.apply();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(PhoneVerificationActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit Application")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        endTask();
                        onDestroy();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
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

