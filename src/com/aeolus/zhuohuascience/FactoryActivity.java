package com.aeolus.zhuohuascience;

import com.aeolus.base.AeolusWebChromeClient;
import com.aeolus.base.AeolusWebViewClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class FactoryActivity extends Activity {
	
	/*Android��Java����JavaScript��HTML�����������������
	1�� Android��Java������HTML��js����
	2�� Android��Java������HTML��js���루��������
	3�� HTML��js����Android��Java������
	4�� HTML��js����Android��Java�����루��������
	*/
	WebView mWebView;
	Context mContext;
	Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_factory);
		mContext = this;
		mWebView= (WebView) this.findViewById(R.id.factoryindex); 
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDefaultTextEncodingName("UTF-8");//�����ַ�����  
		mWebView.setScrollBarStyle(0);//���������Ϊ0ָ��������ռ�ÿռ䣬ֱ�Ӹ�������ҳ��  
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
		String url = "http://sxd616616.blog.163.com/blog/static/169712520142277565570/";
        mWebView.loadUrl(url);
	}
}
