package com.rms_kitchen;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

public class MainActivity extends Activity {

    private XWalkView mXWalkView;
     XWalkPreferences xWalkPreferences;
    private WebView mWebView;
    private UsbAdmin mUsbAdmin;
    private PrinterUtils pt;
    private String siteUrl = UrlAddress.url + "kitchen/";
    private String appLang = "chinese";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        pt = new PrinterUtils(MainActivity.this);
        mUsbAdmin = new UsbAdmin(MainActivity.this);
        startApp();

    }

    public void startApp() {
        mXWalkView = (XWalkView) findViewById(R.id.webView);
        //XWalkSettings settings = mXWalkView.getSettings();
     /*   settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(MODE_MULTI_PROCESS);
        mXWalkView.addJavascriptInterface(new JsInterface(this, mUsbAdmin), "kp");
        mXWalkView.setUIClient(new XWalkUIClient(mXWalkView));
        mXWalkView.setResourceClient(new XWalkResourceClient(mXWalkView));
        if (getResources().getConfiguration().locale.getCountry().equals("CN")) {
            appLang = "chinese";
        } else if (getResources().getConfiguration().locale.getCountry()
                .equals("KR")) {
            appLang = "korean";
        } else {
            appLang = "english";
        }
        Log.d("TESTURL:", siteUrl);*/
        mXWalkView.load(siteUrl + "?appLang=" + appLang, null);

        //  mWebView = (WebView) findViewById(R.id.webView);
      /*  WebSettings setting = mWebView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setAppCacheEnabled(true);
        setting.setCacheMode(MODE_MULTI_PROCESS);
        mWebView.addJavascriptInterface(new JsInterface(this, mUsbAdmin), "kp");
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
        if (getResources().getConfiguration().locale.getCountry().equals("CN")) {
            appLang = "chinese";
        } else if (getResources().getConfiguration().locale.getCountry()
                .equals("KR")) {
            appLang = "korean";
        } else {
            appLang = "english";
        }
        Log.d("TESTURL:", siteUrl);
        mWebView.loadUrl(siteUrl + "?appLang=" + appLang);*/
    }

    @Override
    protected void onDestroy() {
        mUsbAdmin.Closeusb();
        super.onDestroy();
    }


    public MainActivity() {
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    private static MainActivity mainActivity;

}