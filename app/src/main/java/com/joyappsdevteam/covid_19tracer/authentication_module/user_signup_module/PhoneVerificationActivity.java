package com.joyappsdevteam.covid_19tracer.authentication_module.user_signup_module;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.joyappsdevteam.covid_19tracer.R;

public class PhoneVerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        Fragment_AddNumber fragment_addNumber = new Fragment_AddNumber();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.mainLayout,fragment_addNumber).commit();

    }
}
