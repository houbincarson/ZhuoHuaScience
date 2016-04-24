package com.aeolus.base;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aeolus.ui.FragmentPage2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log; 
import android.webkit.WebView;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class JSinterface {
	private Context mContext;
	private Handler mHandler;
	private WebView mWebView;
	private JSONArray mJsonArray = new JSONArray();
	private Random mRandom = new Random();

	public JSinterface(Context context, Handler handler, WebView webView) {
		Log.v(Base.TAG, "JSinterface");
		this.mContext = context;
		this.mHandler = handler;
		this.mWebView = webView;
	}
 
	public void init() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				Log.v(Base.TAG, "javascript:setContactInfo('" + getJsonStr()
						+ "')");
				mWebView.loadUrl("javascript:setContactInfo('" + getJsonStr()
						+ "')");
			}
		});
	}

	private String getJsonStr() {

		try {
			for (int i = 0; i < 10; i++) {
				JSONObject object = new JSONObject();
				object.put("name", i);
				object.put("value", mRandom.nextInt(30));
				object.put("color", getRandColorCode());
				mJsonArray.put(object);
			}
			return mJsonArray.toString();

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object getRandColorCode() {

		String r, g, b;
		Random random = new Random();
		r = Integer.toHexString(mRandom.nextInt(256)).toUpperCase();
		g = Integer.toHexString(mRandom.nextInt(256)).toUpperCase();
		b = Integer.toHexString(random.nextInt(256)).toUpperCase();

		r = r.length() == 1 ? "0" + r : r;
		g = g.length() == 1 ? "0" + g : g;
		b = b.length() == 1 ? "0" + b : b;

		return "#" + r + g + b;
	}
 
	public int getW() {
		return px2dip(mContext.getResources().getDisplayMetrics().widthPixels);
	}
 
	public int getH() {
		return px2dip(mContext.getResources().getDisplayMetrics().heightPixels);
	}
 
	public int px2dip(float pxValue) {
		float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
 
	public void setValue(String name, String value) {
		Toast.makeText(mContext, name + " " + value + "%", Toast.LENGTH_SHORT)
				.show();

		Intent layOut = new Intent();
		layOut.setClass(mContext, FragmentPage2.class);
		Bundle homeData = new Bundle();
		layOut.putExtras(homeData);
		mContext.startActivity(layOut);
	}

}
