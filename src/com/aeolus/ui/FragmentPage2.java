package com.aeolus.ui;
 
import com.aeolus.base.AeolusWebChromeClient;
import com.aeolus.base.AeolusWebViewClient;
import com.aeolus.base.Base;
import com.aeolus.base.JSinterface;
import com.aeolus.zhuohuascience.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("HandlerLeak")
public class FragmentPage2 extends Fragment {
	
	/*Android（Java）与JavaScript（HTML）交互有四种情况：
	1） Android（Java）调用HTML中js代码
	2） Android（Java）调用HTML中js代码（带参数）
	3） HTML中js调用Android（Java）代码
	4） HTML中js调用Android（Java）代码（带参数）
	*/
	Context mContext;
	WebView mWebView;
	View mView;
	Handler mHandler; 
	@Override
	public void onAttach(Activity activity) {
		Log.v(Base.TAG, "FragmentPage2");
		mContext = activity;
		super.onAttach(activity);
	}
	
	@SuppressLint({ "InflateParams", "SetJavaScriptEnabled" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = (View) inflater.inflate(R.layout.fragmentpage2, container,false);
		mWebView= (WebView) mView.findViewById(R.id.webView1); 
		
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDefaultTextEncodingName("UTF-8");//设置字符编码  
		mWebView.setScrollBarStyle(0);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上  
		mWebView.setWebViewClient(new AeolusWebViewClient());
		mWebView.setWebChromeClient(new AeolusWebChromeClient(mContext));
		 
		mWebView.setOnKeyListener(new View.OnKeyListener(){

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {  
                    mWebView.goBack();  
                    return true;  
                } 
				return false;
			}
			
		});
		mHandler = new Handler();
		JSinterface js = new JSinterface(mContext, mHandler, mWebView);
		mWebView.addJavascriptInterface(js, "myObject"); 
		// 加载assets目录下的文件
        //String url = "file:///android_asset/index.html";
		String url = "http://www.baidu.com";
        mWebView.loadUrl(url);
		return mView; 
	} 
}
