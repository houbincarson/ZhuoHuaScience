package com.aeolus.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import com.aeolus.zhuohuascience.R;
import com.aeolus.base.UpdateManager;
import com.aeolus.service.CWcfDataRequest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	String result = null;
	TextView tv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		UpdateManager manager = new UpdateManager(MainActivity.this);
		manager.CheckUpdateInfo();
		new Thread(new Runnable() {

			@Override
			public void run() {
				CWcfDataRequest request = new CWcfDataRequest();
				try {
					
					String proceName = "TestConnection";
					String[] paramKeys = new String[] {};
					String[] paramVals = new String[] {};
					
					String resultString = request.DataRequest_By_SimpDEs(proceName, paramKeys, paramVals);
					ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
					arrayList = request.LoadSingleDataSource(resultString);
					result = arrayList.toString();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// 更新UI
						if (result != null) {
							tv.setText(result);
						} else {
							tv.setText("没有访问到！");
						}
					}

				});
			}
		}).start();
	}
}
