package com.joyappsdevteam.covid_19tracer.authentication_module.user_signup_module;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.gson.Gson;
import com.joyappsdevteam.covid_19tracer.R;
import com.joyappsdevteam.covid_19tracer.authentication_module.UserDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Fragment_SignUp extends Fragment {

    private CardView signUp;
    private AppCompatSpinner spinner;
    private CoordinatorLayout mainLayout_coordinatorLayout;
    private EditText username_signup_editText, emailAddress_signup_editText, password_signup_editText;
    private FirebaseAuth mAuth;
    private static final String TAG = "Fragment_SignUp";
    private Handler mWaitHandler = new Handler();
    private ProgressBar progressBar_fragmentSignUp;
    private String selectedItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        signUp = rootView.findViewById(R.id.signup_Cardview);
        spinner = rootView.findViewById(R.id.spinner);
        mainLayout_coordinatorLayout = rootView.findViewById(R.id.mainLayout_signUp);
        username_signup_editText = rootView.findViewById(R.id.username_signup);
        emailAddress_signup_editText = rootView.findViewById(R.id.emailAddress_signup);
        password_signup_editText = rootView.findViewById(R.id.password_signup);
        progressBar_fragmentSignUp = rootView.findViewById(R.id.progressBar_fragmentSignUp);

        mAuth = FirebaseAuth.getInstance();

        //Hide Keyboard
        hideKeyboard();

        //get spinner ready with data
        loadDataToSpinner();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Hide Keyboard
                hideKeyboard();

                if (isInternetConnectionAvailable()) {

                    String username_string = username_signup_editText.getText().toString().trim();
                    String emailAddress_string = emailAddress_signup_editText.getText().toString().trim();
                    String password_string = password_signup_editText.getText().toString().trim();

                    if (username_string.isEmpty() || emailAddress_string.isEmpty() || password_string.isEmpty()) {
                        showSnackBar("Empty field");
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress_string).matches()) {
                        showSnackBar("Invalid Email Address");
                    } else if (selectedItem.equals("Select your state")){
                        showSnackBar("Select your location");

                    } else if (password_string.length() <= 5) {
                        showSnackBar("Password size should be [6 - 20]");
                    } else {
                        try {
                            progressBar_fragmentSignUp.setVisibility(View.VISIBLE);
                            checkForEmailAddress(username_string, emailAddress_string, selectedItem, password_string);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    showNoInternetAlertDialog();
                }
            }
        });

        return rootView;
    }

    private void loadDataToSpinner() {
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.requireActivity(),R.layout.spinner_item,categories);

        //Dropdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        //setting default value to spinner
        spinner.setSelection(0);

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
    }

    private void createNewAccount(final String username_string, final String emailAddress_string,
                                  final String location_string, final String password_string) {
        try {
            UserDetails userDetails = new UserDetails(
                    username_string,
                    emailAddress_string,
                    location_string,
                    password_string,
                    null
            );

            saveUserCredentialsOnDevice(userDetails);

            Intent intent = new Intent(getContext(), PhoneVerificationActivity.class);
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("email", String.valueOf(emailAddress_string)).apply();
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("password", String.valueOf(password_string)).apply();
            startActivity(intent);
            progressBar_fragmentSignUp.setVisibility(View.INVISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showNoInternetAlertDialog() {

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

    private void hideKeyboard() {
        //Hide Keyboard
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void saveUserCredentialsOnDevice(UserDetails userDetails) {
        SharedPreferences mPrefs = this.requireActivity().getSharedPreferences("UserDetails_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userDetails);
        prefsEditor.putString("UserDetails_json", json);
        prefsEditor.apply();

        Log.i("UserDetails", json);
    }

    private void checkForEmailAddress(final String username_string, final String emailAddress_string,
                                      final String selectedItem_string, final String password_string) {

        try {
            mAuth.fetchSignInMethodsForEmail(emailAddress_string)
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                            boolean isNewUser = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).isEmpty();

                            if (isNewUser) {
                                createNewAccount(username_string, emailAddress_string, selectedItem_string, password_string);
                            } else {
                                progressBar_fragmentSignUp.setVisibility(View.INVISIBLE);
                                showSnackBar("Email Address Already Registered");
                            }

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
