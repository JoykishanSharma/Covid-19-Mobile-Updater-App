package com.joyappsdevteam.covid_19tracer.authentication_module.user_signin_module;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.joyappsdevteam.covid_19tracer.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Fragment_ForgotPassword fragment_forgotPassword = new Fragment_ForgotPassword();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.mainLayout_forgot_password,fragment_forgotPassword).commit();
    }

}
