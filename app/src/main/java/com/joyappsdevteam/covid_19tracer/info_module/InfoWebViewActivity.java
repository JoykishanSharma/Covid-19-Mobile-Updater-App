package com.joyappsdevteam.covid_19tracer.info_module;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.joyappsdevteam.covid_19tracer.R;

import java.util.Objects;

public class InfoWebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_webview);

        webView = findViewById(R.id.myth_busters_webView);

        String whichWebViewToShow = getIntent().getStringExtra("WhichWebViewToShow");
        assert whichWebViewToShow != null;
        String url = null;

        if (whichWebViewToShow.equals("myth_busters")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Myth Busters");
            url = "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/advice-for-public/myth-busters";
        }
        else if (whichWebViewToShow.equals("related_videos")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Related Videos");
            url = "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/advice-for-public/videos";
        }
        else if (whichWebViewToShow.equals("faqs")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Frequently Asked Questions");
            url = "https://www.who.int/news-room/q-a-detail/q-a-coronaviruses";
        }
        else if (whichWebViewToShow.equals("healthy_parenting")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Healthy Parenting");
            url = "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/advice-for-public/healthy-parenting";
        }
        else if (whichWebViewToShow.equals("public_advice")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Public advice from WHO");
            url = "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/advice-for-public";
        }
        else if (whichWebViewToShow.equals("gov_india")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Ministry of Health and Family Welfare");
            url = "https://www.mohfw.gov.in/";
        }
        else if (whichWebViewToShow.equals("coronavirus_history")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("History of Coronavirus");
            url = "https://en.wikipedia.org/wiki/Coronavirus";
        }
        else if (whichWebViewToShow.equals("info_by_google")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Info By Google");
            url = "https://www.google.com/intl/en_in/covid19/";
        }
        else if (whichWebViewToShow.equals("service_before_self")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Service Before Self");
            url = "https://www.covid19india.org/essentials";
        }
        else if (whichWebViewToShow.equals("eConsult")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("eConsult");
            url = "https://econsult-website-three.now.sh/consult";
        }
        else if (whichWebViewToShow.equals("latest_news")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Latest News");
            url = "https://news.google.com/topics/CAAqIggKIhxDQkFTRHdvSkwyMHZNREZqY0hsNUVnSmxiaWdBUAE?hl=en-IN&gl=IN&ceid=IN%3Aen";
        }
        else if (whichWebViewToShow.equals("fake_news")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Fake News");
            url = "https://www.altnews.in/?s=coronavirus";
        }
        else if (whichWebViewToShow.equals("online_scams_buster")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Online Scams");
            url = "https://safety.google/intl/en_in/securitytips-covid19/?utm_source=GoogleUK&utm_medium=Desktop&utm_campaign=SafetyCenter&utm_term=Scams&utm_content=Everyone";
        }

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

        webView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
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
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setGeolocationDatabasePath( getApplicationContext().getFilesDir().getPath() );

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}