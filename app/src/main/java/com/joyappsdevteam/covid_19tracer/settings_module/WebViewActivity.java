package com.joyappsdevteam.covid_19tracer.settings_module;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import com.joyappsdevteam.covid_19tracer.R;

import java.util.Objects;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String url;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.policy_terms_webView);

        String contentToView = getIntent().getStringExtra("content_to_show");

        assert contentToView != null;
        if (contentToView.equals("privacy_policy")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Privacy Policy");
            url = "https://drive.google.com/file/d/1d6gE7nDblE25AV-0yjB5OW6W0OF-VY0q/view?usp=sharing";
        }else if (contentToView.equals("terms_and_conditions")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("Terms And Conditions");
            url = "https://drive.google.com/file/d/14sQNmZLyVBBCB0MMr37Ms1PBOj5Nup-7/view?usp=sharing";
        }else {
            url = null;
        }

        startWebView(url);

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void startWebView(String url) {

        webView.setWebViewClient(new WebViewClient(){
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view,String url){
                view.loadUrl(url);
                return  true;
            }

            public void onLoadResource (WebView view,String url){
                if(progressDialog == null){
                    progressDialog = new ProgressDialog(WebViewActivity.this);
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
        webView.getSettings().setBuiltInZoomControls(true);

        //Load url in webview
        webView.loadUrl(url);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
