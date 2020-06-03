package com.joyappsdevteam.covid_19tracer.authentication_module;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.joyappsdevteam.covid_19tracer.R;

public class UserSignInSignUpActivity extends AppCompatActivity {

    TextView signIn, signUp;
    private ViewPager viewPager;
    private PagerViewAdapter pagerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in_sign_up);

        signIn = findViewById(R.id.sign_in_tabBar);
        signUp = findViewById(R.id.sign_up_tabBar);
        viewPager = findViewById(R.id.fragment_container);

        pagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager(),0);
        viewPager.setPageTransformer(false, new DepthPageTransformer());
        viewPager.setAdapter(pagerViewAdapter);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPageSelected(int position) {

                onChangeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onChangeTab(int position) {
        if (position == 0){
            signIn.setTextSize(20);
            signIn.setTextColor(getColor(R.color.colorPrimary));

            signUp.setTextSize(18);
            signUp.setTextColor(getColor(R.color.GrayTextColor));
        }
        else if (position == 1){
            signUp.setTextSize(20);
            signUp.setTextColor(getColor(R.color.colorPrimary));

            signIn.setTextSize(18);
            signIn.setTextColor(getColor(R.color.GrayTextColor));
        }
    }
}
