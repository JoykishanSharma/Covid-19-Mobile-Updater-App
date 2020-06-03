package com.joyappsdevteam.covid_19tracer.authentication_module.user_signin_module;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joyappsdevteam.covid_19tracer.R;

public class Fragment_ForgotPassword extends Fragment {

    private CardView resetPassword;
    private EditText registeredEmailAddress;
    private CoordinatorLayout mainLayout_coordinatorLayout;
    private static final String TAG = "Fragment_ForgotPassword";
    private Handler mWaitHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        resetPassword = rootView.findViewById(R.id.reset_password_cardview);
        mainLayout_coordinatorLayout = rootView.findViewById(R.id.mainLayout_forgotPassword);
        registeredEmailAddress = rootView.findViewById(R.id.emailAddress_forgot_password);

        if (!isInternetConnectionAvailable()) {
            showNoInternetAlertDialog();
        }

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetConnectionAvailable()) {
                    String registeredEmailAddress_string = registeredEmailAddress.getText().toString().trim();

                    if (registeredEmailAddress_string.isEmpty()) {
                        showSnackBar("Invalid Email Address");
                    } else {
                        checkForRegisteredEmailAddress(registeredEmailAddress_string);
                    }
                } else {
                    showNoInternetAlertDialog();
                }
            }
        });

        return rootView;
    }

    private void checkForRegisteredEmailAddress(final String emailAddress) {

        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UserDetails");
            ref.orderByChild("emailAddress").equalTo(emailAddress).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        showSnackBar("This Email Address is Not Registered!");
                    } else {
                        sendPasswordResetLink(emailAddress);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException().printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendPasswordResetLink(String emailAddress) {
        try {
            FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showSnackBar("Reset Password link is sent to registered email");
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void showNoInternetAlertDialog(){

        new AlertDialog.Builder(requireContext())
                .setMessage("Please Check Internet Connection.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        endTask();
                        //onDestroy();
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
}
