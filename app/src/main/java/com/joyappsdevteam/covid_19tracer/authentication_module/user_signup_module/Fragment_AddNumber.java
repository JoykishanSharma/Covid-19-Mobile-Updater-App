package com.joyappsdevteam.covid_19tracer.authentication_module.user_signup_module;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joyappsdevteam.covid_19tracer.R;

public class Fragment_AddNumber extends Fragment {

    private CardView sendOTP;
    private EditText addNumber_editText;
    private CoordinatorLayout mainLayout_coordinatorLayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private  String userID;
    private static final String TAG = "Fragment_AddNumber";
    private Handler mWaitHandler = new Handler();
    private ProgressBar progressBar_fragmentAddNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_number, container, false);

        sendOTP = rootView.findViewById(R.id.sendOTP_Cardview);
        mainLayout_coordinatorLayout = rootView.findViewById(R.id.mainLayout_addNumber);
        addNumber_editText = rootView.findViewById(R.id.editText_add_number);
        progressBar_fragmentAddNumber = rootView.findViewById(R.id.progressBar_fragmentAddNumber);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + String.valueOf(userID));

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Hide Keyboard
                View view = requireActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert inputManager != null;
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                if (isInternetConnectionAvailable()) {

                    String mobileNumber_string = addNumber_editText.getText().toString().trim();

                    if (mobileNumber_string.isEmpty() || mobileNumber_string.length() <= 9) {
                        showSnackBar("Invalid mobile number");
                    }
                    else {
                        progressBar_fragmentAddNumber.setVisibility(View.VISIBLE);
                        checkForPhoneNumber(mobileNumber_string);
                    }
                } else {
                    showNoInternetAlertDialog();
                }
            }
        });

        return rootView;
    }

    private void checkForPhoneNumber(final String number) {

        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UserDetails");
            ref.orderByChild("mobileNumber").equalTo(number).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        progressBar_fragmentAddNumber.setVisibility(View.INVISIBLE);
                        showSnackBar("This Number is Already Registered");
                    }
                    else {
                        sendOTPAndRegister(number);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void sendOTPAndRegister(String mobileNumber_string){
        Fragment_VerifyNumber fragment_verifyNumber = new Fragment_VerifyNumber();

        //storing mobileNumber to bundle (which we will pass it to Fragment_VerifyNumber)
        Bundle bundle = new Bundle();
        bundle.putString("mobileNumber_passed", mobileNumber_string);

        //set Fragmentclass Arguments
        fragment_verifyNumber.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.mainLayout, fragment_verifyNumber);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        progressBar_fragmentAddNumber.setVisibility(View.INVISIBLE);
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
        } else return false;
    }

    private void showSnackBar(String message) {

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