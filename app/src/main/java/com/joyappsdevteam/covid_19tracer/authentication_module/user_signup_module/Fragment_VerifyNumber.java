package com.joyappsdevteam.covid_19tracer.authentication_module.user_signup_module;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.joyappsdevteam.covid_19tracer.R;
import com.joyappsdevteam.covid_19tracer.authentication_module.UserDetails;
import com.joyappsdevteam.covid_19tracer.home_module.HomeActivity;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Fragment_VerifyNumber extends Fragment {

    private CardView verifyOTP;
    private EditText otp_number_editText;
    private CoordinatorLayout mainLayout_coordinatorLayout;
    private TextView subheading_updateNumber,resend_otp_text;
    private String verificationID;
    private String mobileNumber_received;
    private ProgressBar progressBar_fragmentVerifyNumber;
    public boolean isTicking;
    private Runnable runnable;
    private CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        public void onTick(long millisUntilFinished) {
            resend_otp_text.setText("Resend in  00:" + millisUntilFinished / 1000 + "s");
            isTicking = true;
        }
        public void onFinish() {
            resend_otp_text.setText("RESEND");
            resend_otp_text.setEnabled(true);
            isTicking = false;
        }
    };
    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;
    private static final String TAG = "Fragment_VerifyNumber";
    private Handler mWaitHandler = new Handler();
    private AuthCredential credential;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_verify_number, container,false);

        verifyOTP = rootView.findViewById(R.id.verifyOTP_cardview);
        otp_number_editText = rootView.findViewById(R.id.otp_number);
        mainLayout_coordinatorLayout = rootView.findViewById(R.id.mainLayout_verifyNumber);
        subheading_updateNumber = rootView.findViewById(R.id.code_sent_to_number_text_sub_heading);
        resend_otp_text = rootView.findViewById(R.id.resend_otp);
        progressBar_fragmentVerifyNumber = rootView.findViewById(R.id.progressBar_fragmentVerifyNumber);

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be usable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        //FirebaseAuth user = FirebaseAuth.getInstance();
        // User is signed in
        //userID = user.getUid();

        //PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("userID", String.valueOf(userID)).apply();

        //Log.i("onActivityStart",String.valueOf(userID));

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //userID = user.getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        credential = EmailAuthProvider.getCredential(
                PreferenceManager.getDefaultSharedPreferences(getContext()).getString("email", ""),
                PreferenceManager.getDefaultSharedPreferences(getContext()).getString("password", "")
        );

        //resend countdown
        resend_otp_text.setEnabled(false);
        countDownTimer.start();

        //retrieving mobile number which was pass by Fragment_AddNumber
        assert getArguments() != null;
        mobileNumber_received = getArguments().getString("mobileNumber_passed");
        Log.i(TAG, "onCreateView: Phone Number Received from Previous fragment : " + String.valueOf(mobileNumber_received));

        //update number in sub-heading
        subheading_updateNumber.setText("6 digit code sent on +91-" + mobileNumber_received);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                Log.i(TAG, "onVerificationCompleted: Verification Completed");

                signInWithPhoneAuthCredential(credential);

                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.

                /*String code = credential.getSmsCode();
                if (code != null) {
                    //Calling this method to verify the code typed by User (which he/she got from SMS)
                    verifyCode(code);
                }*/
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    progressBar_fragmentVerifyNumber.setVisibility(View.INVISIBLE);
                    // Invalid request
                    showErrorSnackBar("Invalid Phone Number");
                }
                else if (e instanceof FirebaseTooManyRequestsException) {
                    progressBar_fragmentVerifyNumber.setVisibility(View.INVISIBLE);
                    // The SMS quota for the project has been exceeded
                    showErrorSnackBar("Verification Quota exceeded. \nTry Again after 60 seconds.");
                }
                //logging the exception
                e.printStackTrace();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //super.onCodeSent(s, forceResendingToken);


                Log.i(TAG, "onVerificationCompleted: CodeSent Completed");


                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                verificationID = verificationId;
                mResendToken = forceResendingToken;

                showNormalSnackBar("OTP Sent");
            }

        };

        //verify the code
        sendVerificationCode(mobileNumber_received);

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Hide Keyboard
                hideKeyboard();

                if (isInternetConnectionAvailable()) {
                    String otp_string = otp_number_editText.getText().toString().trim();

                    if (otp_string.isEmpty() || otp_string.length() <= 5) {
                        showErrorSnackBar("Invalid OTP");
                    }
                    else {
                        progressBar_fragmentVerifyNumber.setVisibility(View.VISIBLE);
                        //verify the code
                        verifyCode(otp_string);

                    }
                }
                else {
                    showNoInternetAlertDialog();
                }
            }
        });

        resend_otp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();

                //countDown Start
                countDownTimer.start();

                progressBar_fragmentVerifyNumber.setVisibility(View.VISIBLE);
                //resend Code
                resendVerificationCode(mobileNumber_received,mResendToken);
            }
        });

        return rootView;
    }

    private void linkTwoAccount(){
        Objects.requireNonNull(mAuth.getCurrentUser()).linkWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                            updateUI(user);
                            updateUserCredentialsOnDevice(mobileNumber_received);
                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user!=null){
            String userID = user.getUid();
            Log.i(TAG, "updateUI: "+String.valueOf(userID));
        }
    }

    private void sendVerificationCode(String mobileNumber){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobileNumber,       // Phone number to verify
                60,                         // Timeout duration
                TimeUnit.SECONDS,              // Unit of timeout
                TaskExecutors.MAIN_THREAD,     // Activity (for callback binding)
                mCallbacks);                   // OnVerificationStateChangedCallbacks
    }

    private void resendVerificationCode(String mobileNumber, PhoneAuthProvider.ForceResendingToken token){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobileNumber,       // Phone number to verify
                60,                         // Timeout duration
                TimeUnit.SECONDS,              // Unit of timeout
                TaskExecutors.MAIN_THREAD,     // Activity (for callback binding)
                mCallbacks,                    // OnVerificationStateChangedCallbacks
                token);                        // resend OTP token

    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        //To check if the data inside "credential" object is correct or not.
        //We take help from "FirebaseAuth" Class and pass that "credential" into it as parameter
        //It Checks this data and adds them into Authorized User List.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this.requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if All the Checking verification and Authentication task is successful, this code will execute
                        if (task.isSuccessful()) {

                            FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                            if (user!=null){
                                Log.i(TAG, "onComplete: User SignUp : Successful");
                            }
                            //update OTP editText
                            otp_number_editText.setText(credential.getSmsCode());

                            //We Save the User credential in phone and in firebase Database
                            linkTwoAccount();

                            //stop countDown
                            countDownTimer.onFinish();
                            countDownTimer.cancel();

                            loadDataForFewSeconds(3);

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                progressBar_fragmentVerifyNumber.setVisibility(View.INVISIBLE);
                                //Showing snackBar message, briefly explaining what went wrong.
                                showErrorSnackBar("Incorrect OTP");
                            }

                        }
                    }
                });
    }

    private void loadDataForFewSeconds(int seconds){
        runnable = new Runnable() {
            @Override
            public void run() {
                //The following code will execute after the 2 seconds.
                try {
                    startActivity(new Intent(getContext(), HomeActivity.class));
                    progressBar_fragmentVerifyNumber.setVisibility(View.INVISIBLE);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mWaitHandler.postDelayed(runnable, seconds * 1000);
    }

    private void verifyCode(String codeByUser) {
        //"PhoneAuthCredential" class, object credential, wraps phone number and verification information for authentication purposes.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, codeByUser);
        //This method takes "credential" as parameter and checks if the data inside "credential" object is correct or not.
        signInWithPhoneAuthCredential(credential);

        //otp_number_editText.setText("******");

    }

    private void hideKeyboard(){
        //Hide Keyboard
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void showNoInternetAlertDialog(){

        new AlertDialog.Builder(requireContext())
                .setMessage("Please Check Internet Connection.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        endTask();
                        onDestroy();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void endTask() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

        requireActivity().moveTaskToBack(true);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
        mWaitHandler.removeCallbacksAndMessages(null);
    }

    private boolean isInternetConnectionAvailable() {
        //here "cm" object is pointing to the Connective service of the phone
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //checks if "cm" is null or not.
        //if "cm" is null, this method will terminate and continue with outer methods
        //else continue as it is.
        assert cm != null;

        //here "netinfo" store the information about the active Connected Network.
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        //checks if "netinfo" is null or not and also Checks the state of connectivity
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {

            //determines and checks for type of connectivity from where the network is stable
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            //returns true if connection is available and false if connection is not available
            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        }
        else return false;
    }

    private void showErrorSnackBar(String message) {

        final Snackbar snackbar = Snackbar.make(mainLayout_coordinatorLayout, message, Snackbar.LENGTH_LONG);
        // To change background color to red
        snackbar.setBackgroundTint(getResources().getColor(R.color.colorRed));
        // To change color for action button
        snackbar.setActionTextColor(getResources().getColor(R.color.whiteColor));
        snackbar.setAction("DISMISS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void showNormalSnackBar(String message) {

        final Snackbar snackbar = Snackbar.make(mainLayout_coordinatorLayout, message, Snackbar.LENGTH_LONG);
        // To change background color to red
        snackbar.setBackgroundTint(getResources().getColor(R.color.colorPrimary));
        // To change color for action button
        snackbar.setActionTextColor(getResources().getColor(R.color.whiteColor));
        snackbar.setAction("DISMISS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void updateUserCredentialsOnDevice(String mobileNumber){

        SharedPreferences mPrefs = this.requireActivity().getSharedPreferences("UserDetails_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        Gson gson_retrieve = new Gson();
        String json_retrieve = mPrefs.getString("UserDetails_json", "");
        final UserDetails userDetails = gson_retrieve.fromJson(json_retrieve, UserDetails.class);

        userDetails.setMobile(mobileNumber);

        Gson gson_update = new Gson();
        String json_update = gson_update.toJson(userDetails);
        prefsEditor.putString("UserDetails_json", json_update);
        prefsEditor.apply();

        Log.i("UserDetails",json_update);

        try {
            //store credential to firebase Database
            final DatabaseReference availReff = FirebaseDatabase.getInstance().getReference().child("UserDetails");
            availReff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    FirebaseAuth userId = FirebaseAuth.getInstance();
                    userDetails.setUserID(userId.getUid());
                    availReff.child(Objects.requireNonNull(userId.getUid())).setValue(userDetails);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException().printStackTrace();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}