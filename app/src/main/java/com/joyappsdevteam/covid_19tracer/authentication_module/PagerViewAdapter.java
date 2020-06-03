package com.joyappsdevteam.covid_19tracer.authentication_module;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.joyappsdevteam.covid_19tracer.authentication_module.user_signin_module.Fragment_SignIn;
import com.joyappsdevteam.covid_19tracer.authentication_module.user_signup_module.Fragment_AddNumber;
import com.joyappsdevteam.covid_19tracer.authentication_module.user_signup_module.Fragment_SignUp;
import com.joyappsdevteam.covid_19tracer.authentication_module.user_signup_module.Fragment_VerifyNumber;

class PagerViewAdapter extends FragmentPagerAdapter {

    int ORIGINAL_CODE = 1;
    int TEST_CODE = 0;

    public PagerViewAdapter(@NonNull FragmentManager fm, int code) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        TEST_CODE = code;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        if (TEST_CODE == ORIGINAL_CODE){
            switch(position){
                case 0 :
                    fragment = new Fragment_AddNumber();
                    break;
                case 1 :
                    fragment = new Fragment_VerifyNumber();
                    break;
            }
        }
        else {
            switch(position){
                case 0 :
                    fragment = new Fragment_SignIn();
                    break;
                case 1 :
                    fragment = new Fragment_SignUp();
                    break;
            }
        }

        assert fragment != null;
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
