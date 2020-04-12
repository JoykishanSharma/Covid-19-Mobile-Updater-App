package com.joyappsdevteam.covid_19tracer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelplineNumberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline_number);

        Map<String, List<String>> stateName = new HashMap<>();
        List<String> numbers = new ArrayList<>();
        numbers.add("Value 1");
        numbers.add("Value 2");
        stateName.put("Key1", numbers);

        // to get the arraylist
        System.out.println(stateName.get("key1"));
    }
}
