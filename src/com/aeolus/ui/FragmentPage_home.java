package com.aeolus.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException; 
 

import com.aeolus.base.Base;
import com.aeolus.service.CWcfDataRequest;
import com.aeolus.zhuohuascience.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentPage_home extends Fragment {
	 
	View mview;
	Context mContext;  
	ImageView mImageView;
	@Override
	public void onAttach(Activity activity) {
		Log.v(Base.TAG, "AAAAAAAAAA____onAttach");
		mContext = activity;
		super.onAttach(activity);
	} 
	
	@SuppressLint("HandlerLeak")
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v(Base.TAG, "AAAAAAAAAA____onCreateView");
		mview = (View) inflater.inflate(R.layout.fragmentpage_home, container,false); 
		
		mImageView= (ImageView) mview.findViewById(R.id.imageView); 
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String proceName = "sp_test";
				String[] paramKeys = new String[] {};
				String[] paramVals = new String[] {};
				
				 CWcfDataRequest mCWcfDataRequest = new CWcfDataRequest();
				 String resultString = mCWcfDataRequest.DataRequest_By_SimpDEs(proceName, paramKeys, paramVals);
				 Log.v(Base.TAG, resultString);
				 try {
					 ArrayList<HashMap<String, Object>> array = mCWcfDataRequest.LoadSingleDataSource(resultString);
					 Log.v(Base.TAG, array.get(0).get("url").toString()); 
					 final Bitmap bitmap = Base.getInstance().getHttpBitmap(array.get(0).get("url").toString());
					 
					 mImageView.post(new Runnable() {
			                public void run() {
			                    mImageView.setImageBitmap(bitmap);
			                }
			            }); 
					 
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		return mview;

	}

	 

	@Override
	public void onDestroyView() { 
		super.onDestroyView();
		mview = null;
	}

}
