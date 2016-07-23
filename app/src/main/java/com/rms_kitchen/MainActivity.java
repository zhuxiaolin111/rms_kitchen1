package com.rms_kitchen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
	
	private WebView mWebView;
	private static SQLiteDatabase db;
	private static PrinterUtils pt;
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
		db = openOrCreateDatabase("/sdcard/posmac.db", Context.MODE_PRIVATE,
				null);
		
		startApp();
	}
	

	public void startApp() {
		mWebView = (WebView) findViewById(R.id.webView);
		WebSettings setting = mWebView.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.clearCache(true);
		mWebView.addJavascriptInterface(new JsInterface(this, db, pt), "kp");
		mWebView.setWebChromeClient(new WebChromeClient(){
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setMessage(message)
						.setNeutralButton("Alert", new OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								arg0.dismiss();
							}
						}).show();
				result.cancel();
				return true;
			}

			@Override
			public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
				// TODO Auto-generated method stub
				return super.onJsConfirm(view, url, message, result);
			}
		});
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
		mWebView.loadUrl(siteUrl + "?appLang=" + appLang);
	}

	
}
