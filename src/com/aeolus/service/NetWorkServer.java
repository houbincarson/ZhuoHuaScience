package com.aeolus.service;
 

import com.aeolus.base.Declare;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class NetWorkServer extends Service{ 
	
	 private ConnectivityManager connectivityManager;
	 private NetworkInfo info;
	 Declare declare;
	 private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		 @Override
	        public void onReceive(Context context, Intent intent) {
	            String action = intent.getAction();
	            declare = (Declare) getApplicationContext();
	            
	            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
	            	
	                Log.d("mark", "����״̬�Ѿ��ı�");
	                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	                info = connectivityManager.getActiveNetworkInfo();	                
	                if(info != null && info.isAvailable()) {
	                    String name = info.getTypeName();
	                    Log.d("mark", "��ǰ�������ƣ�" + name);
	                    Toast.makeText(getApplicationContext(), "��ǰ�������ƣ�"+name, Toast.LENGTH_SHORT).show();
	                    declare.setConnected(true);
	                } else {
	                    Log.d("mark", "û�п�������");
	                    Toast.makeText(getApplicationContext(), "û�п�������", Toast.LENGTH_SHORT).show();
	                    declare.setConnected(false);
	                }
	            }
	            else {
	            	Toast.makeText(getApplicationContext(), "û�п�������", Toast.LENGTH_SHORT).show();
	            	 
				}
	        }
	    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    } 
}


