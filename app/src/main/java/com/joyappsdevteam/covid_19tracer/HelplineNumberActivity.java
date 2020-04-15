package com.joyappsdevteam.covid_19tracer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelplineNumberActivity extends AppCompatActivity implements View.OnClickListener {

    Map<String, List<String>> stateName;
    TextView andhra_pradesh,arunachal_pradesh,assam,call_104_states,haryana,kerala,maharashtra,
            manipur,meghalaya,mizoram,nagaland,odisha,rajasthan,tamil_nadu,tripura,uttar_pradesh,
            west_bengal_no1,west_bengal_no2,andaman_and_nicobar_islands,chandigarh,call_104_union_territory
            ,delhi,jammu_and_kashmir_no1,jammu_and_kashmir_no2,ladakh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline_number);

        stateName = new HashMap<>();

        addAllStateNamesAndNumbersToTheList();

        callNowCardViewReferencing();

        Log.i("StateWise data", String.valueOf(stateName));
        Log.i("bihar", String.valueOf(stateName.get("Bihar / Chhattisgarh / Goa / Gujarat / Himachal Pradesh / " +
                "Jharkhand / Karnataka / Madhya Pradesh / Punjab / Sikkim / Telangana / Uttarakhand")));
    }

    private void callNowCardViewReferencing(){
        andhra_pradesh = findViewById(R.id.andhra_pradesh);
        andhra_pradesh.setOnClickListener(this);
        arunachal_pradesh = findViewById(R.id.arunachal_pradesh);
        arunachal_pradesh.setOnClickListener(this);
        assam = findViewById(R.id.assam);
        assam.setOnClickListener(this);
        call_104_states = findViewById(R.id.call_104_states);
        call_104_states.setOnClickListener(this);
        haryana = findViewById(R.id.haryana);
        haryana.setOnClickListener(this);
        kerala = findViewById(R.id.kerala);
        kerala.setOnClickListener(this);
        maharashtra = findViewById(R.id.maharashtra);
        maharashtra.setOnClickListener(this);
        manipur = findViewById(R.id.manipur);
        manipur.setOnClickListener(this);
        meghalaya = findViewById(R.id.meghalaya);
        meghalaya.setOnClickListener(this);
        mizoram = findViewById(R.id.mizoram);
        mizoram.setOnClickListener(this);
        nagaland = findViewById(R.id.nagaland);
        nagaland.setOnClickListener(this);
        odisha = findViewById(R.id.odisha);
        odisha.setOnClickListener(this);
        rajasthan = findViewById(R.id.rajasthan);
        rajasthan.setOnClickListener(this);
        tamil_nadu = findViewById(R.id.tamil_nadu);
        tamil_nadu.setOnClickListener(this);
        tripura = findViewById(R.id.tripura);
        tripura.setOnClickListener(this);
        uttar_pradesh = findViewById(R.id.uttar_pradesh);
        uttar_pradesh.setOnClickListener(this);
        west_bengal_no1 = findViewById(R.id.west_bengal_no1);
        west_bengal_no1.setOnClickListener(this);
        west_bengal_no2 = findViewById(R.id.west_bengal_no2);
        west_bengal_no2.setOnClickListener(this);
        andaman_and_nicobar_islands = findViewById(R.id.andaman_and_nicobar_islands);
        andaman_and_nicobar_islands.setOnClickListener(this);
        chandigarh = findViewById(R.id.chandigarh);
        chandigarh.setOnClickListener(this);
        call_104_union_territory = findViewById(R.id.call_104_union_territory);
        call_104_union_territory.setOnClickListener(this);
        delhi = findViewById(R.id.delhi);
        delhi.setOnClickListener(this);
        jammu_and_kashmir_no1 = findViewById(R.id.jammu_and_kashmir_no1);
        jammu_and_kashmir_no1.setOnClickListener(this);
        jammu_and_kashmir_no2 = findViewById(R.id.jammu_and_kashmir_no2);
        jammu_and_kashmir_no2.setOnClickListener(this);
        ladakh = findViewById(R.id.ladakh);
        ladakh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.andhra_pradesh:

                break;

            case R.id.arunachal_pradesh:

                break;

            case R.id.assam:

                break;

            case R.id.call_104_states:

                break;

            case R.id.haryana:

                break;

            case R.id.kerala:

                break;

            case R.id.maharashtra:

                break;

            case R.id.manipur:

                break;

            case R.id.meghalaya:

                break;

            case R.id.mizoram:

                break;

            case R.id.nagaland:

                break;

            case R.id.odisha:

                break;

            case R.id.rajasthan:

                break;

            case R.id.tamil_nadu:

                break;

            case R.id.tripura:

                break;

            case R.id.uttar_pradesh:

                break;

            case R.id.west_bengal_no1:

                break;

            case R.id.west_bengal_no2:

                break;

            case R.id.andaman_and_nicobar_islands:

                break;

            case R.id.chandigarh:

                break;

            case R.id.call_104_union_territory:

                break;

            case R.id.delhi:

                break;

            case R.id.jammu_and_kashmir_no1:

                break;

            case R.id.jammu_and_kashmir_no2:

                break;

            case R.id.ladakh:

                break;

            default:
                break;
        }
    }

    private void addAllStateNamesAndNumbersToTheList() {

        addStateAndNumbersToTheList("Andhra Pradesh","08662410978");
        addStateAndNumbersToTheList("Arunachal Pradesh","09436055743");
        addStateAndNumbersToTheList("Assam","06913347770");
        addStateAndNumbersToTheList("Bihar / Chhattisgarh / Goa / Gujarat / Himachal Pradesh / " +
                "Jharkhand / Karnataka / Madhya Pradesh / Punjab / Sikkim / Telangana / Uttarakhand","104");
        addStateAndNumbersToTheList("Haryana","08558893911");
        addStateAndNumbersToTheList("Kerala","04712552056");
        addStateAndNumbersToTheList("Maharashtra","02026127394");
        addStateAndNumbersToTheList("Manipur","03852411668");
        addStateAndNumbersToTheList("Meghalaya","108");
        addStateAndNumbersToTheList("Mizoram","102");
        addStateAndNumbersToTheList("Nagaland","07005539653");
        addStateAndNumbersToTheList("Odisha","09439994859");
        addStateAndNumbersToTheList("Rajasthan","01412225624");
        addStateAndNumbersToTheList("Tamil Nadu","04429510500");
        addStateAndNumbersToTheList("Tripura","03812315879");
        addStateAndNumbersToTheList("Uttar Pradesh","18001805145");
        addStateAndNumbersToTheList("West Bengal","1800313444222","03323412600");

        //Union Territory
        addStateAndNumbersToTheList("Andaman and Nicobar Islands","03192232102");
        addStateAndNumbersToTheList("Chandigarh","09779558282");
        addStateAndNumbersToTheList("Dadra and Nagar Haveli / Daman and Diu / Lakshadweep / Puducherry","104");
        addStateAndNumbersToTheList("Delhi","01122307145");
        addStateAndNumbersToTheList("Jammu and Kashmir","01912520982", "01942440283");
        addStateAndNumbersToTheList("Ladakh","01982256462");

    }

    private void addStateAndNumbersToTheList(String state, String number1, String number2){
        List<String> numbers = new ArrayList<>();
        numbers.add(number1);
        numbers.add(number2);
        stateName.put(state, numbers);
    }

    private void addStateAndNumbersToTheList(String state, String number1){
        List<String> numbers = new ArrayList<>();
        numbers.add(number1);
        stateName.put(state, numbers);
    }

    private void directCall(final String number){
        new AlertDialog.Builder(HelplineNumberActivity.this)
                .setTitle("Direct Call")
                .setMessage("Are you sure you want to call this number?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with call operation
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + number));
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(callIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
