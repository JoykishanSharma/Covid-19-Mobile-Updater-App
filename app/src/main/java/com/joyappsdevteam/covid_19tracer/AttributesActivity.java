package com.joyappsdevteam.covid_19tracer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AttributesActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attributes);

        ImageView back_arrow6 = findViewById(R.id.back_arrow6);
        webView = findViewById(R.id.attributes_webView);

        String url = "https://drive.google.com/file/d/1yHo_1BIfpOch4NluogmD-XRa9_6Ls9sN/view?usp=sharing";

        back_arrow6.setOnClickListener(new View.OnClickListener() {
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

            public boolean shouldOverrideUrlLoading(WebView view,String url){
                view.loadUrl(url);
                return  true;
            }

            public void onLoadResource (WebView view,String url){
                if(progressDialog == null){
                    progressDialog = new ProgressDialog(AttributesActivity.this);
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

}
