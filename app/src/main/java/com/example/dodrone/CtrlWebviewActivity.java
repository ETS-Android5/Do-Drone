package com.example.dodrone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CtrlWebviewActivity extends AppCompatActivity {

    private WebView ctrlWeb;
    private String url= "http://www.naver.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctrl_webview);


        ctrlWeb = (WebView)findViewById(R.id.ctrlWeb);
        ctrlWeb.getSettings().setJavaScriptEnabled(true);
        ctrlWeb.loadUrl(url);

        ctrlWeb.setWebChromeClient(new WebChromeClient());
        ctrlWeb.setWebViewClient(new WebViewClientClass());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && ctrlWeb.canGoBack()) {
            ctrlWeb.goBack();
            return true;



        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(String.valueOf(request.getUrl()));
            return true;
        }

    }
}