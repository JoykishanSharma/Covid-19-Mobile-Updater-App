package com.joyappsdevteam.covid_19tracer.authentication_module.user_signin_module;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.joyappsdevteam.covid_19tracer.R;
import com.joyappsdevteam.covid_19tracer.authentication_module.UserDetails;
import com.joyappsdevteam.covid_19tracer.home_module.HomeActivity;

public class Fragment_SignIn extends Fragment {

    private EditText emailAddress_signin_editText, password_signin_editText;
    private CardView signIn;
    private TextView forgotPassword;
    private CoordinatorLayout mainLayout_coordinatorLayout;
    private boolean isEmailAddressAlreadyRegistered = false;
    private static final String TAG = "Fragment_SignIn";
    private FirebaseAuth mAuth;
    private Handler mWaitHandler = new Handler();
    private ProgressBar progressBar_fragmentSingIn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        emailAddress_signin_editText = rootView.findViewById(R.id.emailAddress_signin);
        password_signin_editText = rootView.findViewById(R.id.password_signin);
        signIn = rootView.findViewById(R.id.signin_cardview);
        forgotPassword = rootView.findViewById(R.id.forgot_password);
        mainLayout_coordinatorLayout = rootView.findViewById(R.id.mainLayout_signIn);
        progressBar_fragmentSingIn = rootView.findViewById(R.id.progressBar_fragmentSingIn);

        mAuth = FirebaseAuth.getInstance();

        hideKeyboard();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //hide keyboard
                hideKeyboard();

                if (isInternetConnectionAvailable()) {
                    String emailAddress_string = emailAddress_signin_editText.getText().toString().trim();
                    String password_string = password_signin_editText.getText().toString().trim();

                    if (emailAddress_string.isEmpty() || password_string.isEmpty()) {
                        showSnackBar("Empty field");
                    }
                    else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress_string).matches()) {
                        showSnackBar("Invalid Email Address");
                    }
                    else if (password_string.length() <= 5) {
                        showSnackBar("Password size should be [6 - 20]");
                    }
                    else {
                        progressBar_fragmentSingIn.setVisibility(View.VISIBLE);
                        signInExistingUser(emailAddress_string,password_string);

                    }
                } else {
                    showNoInternetAlertDialog();
                }

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //hide keyboard
                hideKeyboard();

                startActivity(new Intent(getContext(), ForgotPasswordActivity.class));
            }
        });

        return rootView;
    }

    private void signInExistingUser(String emailAddress_string, String password_string){
        mAuth.signInWithEmailAndPassword(emailAddress_string, password_string)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            startActivity(new Intent(getContext(), HomeActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            progressBar_fragmentSingIn.setVisibility(View.INVISIBLE);
                            showSnackBar("Not a Registered User");
                        }

                    }
                });
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

    private void hideKeyboard() {
        //Hide Keyboard
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

    private void saveUserCredentialsOnDevice(UserDetails userDetails) {
        SharedPreferences mPrefs = this.requireActivity().getSharedPreferences("UserCredentials_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userDetails);
        prefsEditor.putString("UserCredentials_json", json);
        prefsEditor.apply();

        Log.i("UserCredentials", json);
    }

    private boolean checkForEmailAddress(String emailAddress) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserCredentials");
        ref.orderByChild("emailAddress").equalTo(emailAddress).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    isEmailAddressAlreadyRegistered = true;
                    Log.i("Email Exists :","true");
                }
                else {
                    isEmailAddressAlreadyRegistered = false;
                    Log.i("Email Exists :","false");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return isEmailAddressAlreadyRegistered;
    }

    private void checkForPassword(String password) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UserCredentials");
        ref.orderByChild("password").equalTo(password).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String key = dataSnapshot.getKey();
                    assert key != null;
                    //Log.i("key",String.valueOf(key));

                    String val = dataSnapshot.getChildren().toString();
                    //Log.i("val",String.valueOf(val));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

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


}
