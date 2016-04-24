package com.aeolus.base;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AeolusWebViewClient extends WebViewClient {
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		 
		super.onPageStarted(view, url, favicon);
	}
	@Override
	public void onPageFinished(WebView view, String url) {
	 
		super.onPageFinished(view, url);
	}
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return super.shouldOverrideUrlLoading(view, url);
	}
	@Override
	public void onLoadResource(WebView view, String url) {
		 
		super.onLoadResource(view, url);
	}
	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		 
		super.onReceivedError(view, errorCode, description, failingUrl);
	}
}
