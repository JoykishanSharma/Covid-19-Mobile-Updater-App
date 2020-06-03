package com.joyappsdevteam.covid_19tracer;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.joyappsdevteam.covid_19tracer.authentication_module.user_signup_module.PhoneVerificationActivity;
import com.joyappsdevteam.covid_19tracer.home_module.HomeActivity;
import com.joyappsdevteam.covid_19tracer.authentication_module.UserSignInSignUpActivity;

public class WelcomeActivity extends AppCompatActivity {

    //A handle class mainly deal with the task which need to be executed on a single thread
    //For more Detail, Go to https://developer.android.com/reference/android/os/Handler
    private Handler mWaitHandler = new Handler();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "WelcomeActivity";
    private boolean isSignedIn = false;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();

        try {
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        isSignedIn = true;
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                        isSignedIn = false;
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkUserPermissions();

        if (!isInternetConnectionAvailable()) {
            try {
                mWaitHandler.removeCallbacks(runnable);
                mWaitHandler.removeCallbacksAndMessages(null);
                showNoInternetAlertDialog();
            } catch (Exception e) {
                Toast.makeText(this, "Something Wrong with AlertDialog", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }

    private void welcomeScreen2Seconds() {
        runnable = new Runnable() {
            @Override
            public void run() {

                //The following code will execute after the 2 seconds.
                try {

                    if (isSignedIn) {
                        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(WelcomeActivity.this, UserSignInSignUpActivity.class);
                        startActivity(intent);
                    }

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mWaitHandler.postDelayed(runnable, 1000);  // Give a 3 seconds delay.
    }

    private void checkUserPermissions() {
        //checking if Phone Call permission is granted by Android or not
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) +
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

                welcomeScreen2Seconds();
        }
        //if the Phone Call permission is not granted, the App will request for Phone Call permission
        else {
            requestRuntimePermission();
        }
    }

    private void requestRuntimePermission() {

        /* Android provides a utility method, "shouldShowRequestPermissionRationale()", that returns true
         * if the Android has previously denied the request, and returns false if a user has denied a permission
         * and selected the Don't ask again option in the permission request dialog. */

        //To know More about Runtime Permission request, Go to https://developer.android.com/training/permissions/requesting

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            //Showing AlertDialog for explaining "Why we need this Permission in the App?"
            new AlertDialog.Builder(WelcomeActivity.this)
                    .setTitle("Permission needed")
                    .setMessage("These permissions are needed for the app to work properly.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Requesting User to give Direct Phone Call Permission and Internet Access Permission(Optional)
                            ActivityCompat.requestPermissions(WelcomeActivity.this,
                                    new String[] {Manifest.permission.CALL_PHONE,
                                            Manifest.permission.ACCESS_FINE_LOCATION},
                                    1);
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
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    //This Method gets Call Automatically afer "requestRuntimePermission" method for checking purpose
    //This Method Checks if the User has Granted permission or Not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1)  {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //Permission Granted
                welcomeScreen2Seconds();

            } else {
                endTask();
            }
        }
    }

    private void showNoInternetAlertDialog() {

        new AlertDialog.Builder(this)
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

    private boolean isInternetConnectionAvailable() {
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
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            //returns true if connection is available and false if connection is not available
            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        } else return false;
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

    public void endTask() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

        finishAndRemoveTask();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
        mWaitHandler.removeCallbacksAndMessages(null);
    }
}
