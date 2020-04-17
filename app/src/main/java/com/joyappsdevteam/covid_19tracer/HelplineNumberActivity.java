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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HelplineNumberActivity extends AppCompatActivity implements View.OnClickListener {

    TextView national_helpline_01,national_helpline_02,andhra_pradesh,arunachal_pradesh,assam,call_104_states,haryana,kerala,maharashtra,
            manipur,meghalaya,mizoram,nagaland,odisha,rajasthan,tamil_nadu,tripura,uttar_pradesh,
            west_bengal_no1,west_bengal_no2,andaman_and_nicobar_islands,chandigarh,call_104_union_territory
            ,delhi,jammu_and_kashmir_no1,jammu_and_kashmir_no2,ladakh;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline_number);

        callNowCardViewReferencing();
    }

    private void callNowCardViewReferencing(){
        back_button = findViewById(R.id.back_arrow7);
        back_button.setOnClickListener(this);
        national_helpline_01 = findViewById(R.id.national_helpline_01);
        national_helpline_01.setOnClickListener(this);
        national_helpline_02 = findViewById(R.id.national_helpline_02);
        national_helpline_02.setOnClickListener(this);
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

            case R.id.back_button:
                finish();
                break;

            case R.id.national_helpline_01:
                directCall("01123978046");
                break;

            case R.id.national_helpline_02:
                directCall("1075");
                break;

            case R.id.andhra_pradesh:
                directCall("08662410978");
                break;

            case R.id.arunachal_pradesh:
                directCall("09436055743");
                break;

            case R.id.assam:
                directCall("06913347770");
                break;

            case R.id.call_104_states:

            case R.id.call_104_union_territory:
                directCall("104");
                break;

            case R.id.haryana:
                directCall("08558893911");
                break;

            case R.id.kerala:
                directCall("04712552056");
                break;

            case R.id.maharashtra:
                directCall("02026127394");
                break;

            case R.id.manipur:
                directCall("03852411668");
                break;

            case R.id.meghalaya:
                directCall("108");
                break;

            case R.id.mizoram:
                directCall("102");
                break;

            case R.id.nagaland:
                directCall("07005539653");
                break;

            case R.id.odisha:
                directCall("09439994859");
                break;

            case R.id.rajasthan:
                directCall("01412225624");
                break;

            case R.id.tamil_nadu:
                directCall("04429510500");
                break;

            case R.id.tripura:
                directCall("03812315879");
                break;

            case R.id.uttar_pradesh:
                directCall("18001805145");
                break;

            case R.id.west_bengal_no1:
                directCall("1800313444222");
                break;

            case R.id.west_bengal_no2:
                directCall("03323412600");
                break;

            case R.id.andaman_and_nicobar_islands:
                directCall("03192232102");
                break;

            case R.id.chandigarh:
                directCall("09779558282");
                break;

            case R.id.delhi:
                directCall("01122307145");
                break;

            case R.id.jammu_and_kashmir_no1:
                directCall("01912520982");
                break;

            case R.id.jammu_and_kashmir_no2:
                directCall("01942440283");
                break;

            case R.id.ladakh:
                directCall("01982256462");
                break;

            default:
                break;
        }
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
