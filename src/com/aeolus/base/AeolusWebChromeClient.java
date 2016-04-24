package com.aeolus.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class AeolusWebChromeClient extends WebChromeClient {
	
	Context mContext;
	ProgressDialog mProgressDialog ;
	View mView; 
	public AeolusWebChromeClient(Context mContext)
	{
		this.mContext = mContext;  
		mProgressDialog = new ProgressDialog(mContext);
	}
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() { 
		@Override
		public void handleMessage(Message msg) {
			if (!Thread.currentThread().isInterrupted()){  
	            switch (msg.what) {  
	            case 0:  
	            	mProgressDialog.show();// ��ʾ���ȶԻ���  
	                break;  
	            case 1:  
	            	mProgressDialog.hide();// ���ؽ��ȶԻ��򣬲���ʹ��dismiss()��cancel(),�����ٴε���show()ʱ����ʾ�ĶԻ���СԲȦ���ᶯ��  
	                break;  
	            }  
	        }  
			super.handleMessage(msg);
		}
	};
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		if (newProgress == 100) {
			mHandler.sendEmptyMessage(1); 
		}
		super.onProgressChanged(view, newProgress);
	}
	
	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			JsResult result) {
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		System.out.println("��������ʾ��");
		result.confirm();
		return super.onJsAlert(view, url, message, result);
	}
	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
			JsResult result) {
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		System.out.println("������ȷ�Ͽ�");
		result.confirm();
		return super.onJsConfirm(view, url, message, result);
	}
	@Override
	public boolean onJsPrompt(WebView view, String url, String message,
			String defaultValue, JsPromptResult result) {
		
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		System.out.println("�����������");
		result.confirm();
		return super.onJsPrompt(view, url, message, defaultValue, result);
	}
	@Override
	public boolean onJsBeforeUnload(WebView view, String url, String message,
			JsResult result) {
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		System.out.println("�������뿪ȷ�Ͽ�");
		result.confirm();
		return super.onJsBeforeUnload(view, url, message, result);
	}
	
	
}
