package com.joyappsdevteam.covid_19tracer.info_module;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.joyappsdevteam.covid_19tracer.R;

public class InfoWebViewActivity extends AppCompatActivity {

    private WebView webView;
    private ImageView BackButton;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_webview);

        title = findViewById(R.id.info_webview_title);
        BackButton = findViewById(R.id.back_arrow9);
        webView = findViewById(R.id.myth_busters_webView);

        String whichWebViewToShow = getIntent().getStringExtra("WhichWebViewToShow");
        assert whichWebViewToShow != null;
        String url = null;

        if (whichWebViewToShow.equals("myth_busters")){
            title.setText("Myth Busters");
            url = "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/advice-for-public/myth-busters";
        }
        else if (whichWebViewToShow.equals("related_videos")){
            title.setText("Related Videos");
            url = "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/advice-for-public/videos";
        }
        else if (whichWebViewToShow.equals("faqs")){
            title.setText("Frequently Asked Questions");
            url = "https://www.who.int/news-room/q-a-detail/q-a-coronaviruses";
        }
        else if (whichWebViewToShow.equals("healthy_parenting")){
            title.setText("Healthy Parenting");
            url = "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/advice-for-public/healthy-parenting";
        }
        else if (whichWebViewToShow.equals("public_advice")){
            title.setText("Public advice from WHO");
            url = "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/advice-for-public";
        }
        else if (whichWebViewToShow.equals("gov_india")){
            title.setText("Ministry of Health and Family Welfare");
            url = "https://www.mohfw.gov.in/";
        }
        else if (whichWebViewToShow.equals("coronavirus_history")){
            title.setText("History of Coronavirus");
            url = "https://en.wikipedia.org/wiki/Coronavirus";
        }
        else if (whichWebViewToShow.equals("info_by_google")){
            title.setText("Info By Google");
            url = "https://www.google.com/intl/en_in/covid19/";
        }
        else if (whichWebViewToShow.equals("service_before_self")){
            title.setText("Service Before Self");
            url = "https://www.covid19india.org/essentials";
        }
        else if (whichWebViewToShow.equals("eConsult")){
            title.setText("eConsult");
            url = "https://econsult-website-three.now.sh/consult";
        }

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startWebView(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void startWebView(String url) {

        webView.setWebViewClient(new WebViewClient(){
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return  true;
            }

            public void onLoadResource (WebView view,String url){
                if(progressDialog == null){
                    progressDialog = new ProgressDialog(InfoWebViewActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
                else {
                    progressDialog.dismiss();
                }
            }

            public void onPageFinished(WebView view,String url){
                try{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //Javascript enabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);// enable javascript
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportMultipleWindows(true);

        //Load url in webview
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.isFocused() && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}