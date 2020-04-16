package com.joyappsdevteam.covid_19tracer;

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
import android.widget.Button;
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
import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends AppCompatActivity {

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

        reg_mobile_no_editText = findViewById(R.id.reg_phone);
        reg_send_otp = findViewById(R.id.send_otp_cardView);
        resend_otp = findViewById(R.id.resend_otp);
        send_otp_text = findViewById(R.id.send_otp_text);
        resend_otp_linearLayout = findViewById(R.id.resend_otp_linearLayout);
        textView = findViewById(R.id.textView);
        progress_circular = findViewById(R.id.progress_circular);
        otp_verification_EditText = findViewById(R.id.otp_verification);
        verify_otp_cardView = findViewById(R.id.verify_otp_cardView);


        reg_send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reg_mobile_no = reg_mobile_no_editText.getText().toString().trim();

                if (TextUtils.isEmpty(reg_mobile_no) || reg_mobile_no.length() < 10) {
                    reg_mobile_no_editText.setError("Valid Number is required");
                } else {
                    if (isConnected()) {
                        //ask permission -- Internet -- User Location -- Phone Call --
                        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                                ContextCompat.checkSelfPermission(getApplicationContext(),
                                        Manifest.permission.CALL_PHONE) +
                                ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.INTERNET)
                                == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(PhoneVerificationActivity.this, "You have already granted this permission!", Toast.LENGTH_SHORT).show();


                            //check registration and send OTP
                            //custom enter OTP activity


                            progress_circular.setVisibility(View.VISIBLE);
                            sendVerificationCode(reg_mobile_no);

                            if (!verificationError) {

                                send_otp_text.setText("Enter the OTP code sent at +91" + reg_mobile_no);

                                reg_mobile_no_editText.setVisibility(View.INVISIBLE);
                                otp_verification_EditText.setVisibility(View.VISIBLE);

                                reg_send_otp.setVisibility(View.INVISIBLE);
                                verify_otp_cardView.setVisibility(View.VISIBLE);

                                resend_otp_linearLayout.setVisibility(View.VISIBLE);

                                userPhoneNumber = reg_mobile_no;
                            }
                        }
                        else {
                            requestRuntimePermission();
                        }

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

        verify_otp_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificationCodeByUser = otp_verification_EditText.getText().toString().trim();
                progress_circular.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(verificationCodeByUser) || verificationCodeByUser.length() < 6) {
                    reg_mobile_no_editText.setError("Valid Number is required");
                } else {
                    if (isConnected()) {
                        verifyCode(verificationCodeByUser);
                    } else
                        Toast.makeText(PhoneVerificationActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_otp_text.setText("We will send you a One Time Password on this mobile number");

                reg_mobile_no_editText.setVisibility(View.VISIBLE);
                otp_verification_EditText.setVisibility(View.INVISIBLE);

                reg_send_otp.setVisibility(View.VISIBLE);
                verify_otp_cardView.setVisibility(View.INVISIBLE);

                resend_otp_linearLayout.setVisibility(View.INVISIBLE);

                progress_circular.setVisibility(View.INVISIBLE);


            }
        });
    }

    private void requestRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(PhoneVerificationActivity.this)
                    .setTitle("Permission needed")
                    .setMessage("These permissions are needed for the app to work properly.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(PhoneVerificationActivity.this,
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.CALL_PHONE,
                                            Manifest.permission.INTERNET},
                                    MULTIPLE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.INTERNET},
                    MULTIPLE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void sendVerificationCode(String reg_mobile_no) {

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

