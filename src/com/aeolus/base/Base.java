package com.aeolus.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Base {
	private static Base base;
	public static String TAG = "ZhuoHuaScience";
	private Base() {
	}

	public static Base getInstance() {
		if (base == null) {
			base = new Base();
		}
		return base;
	}

	public Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} 
	}
	public Bitmap getHttpBitmap(String url)
	{
		 URL myFileUrl = null;
	     Bitmap bitmap = null;
	     
	     try {
	    	 
	    	 myFileUrl = new URL(url); 
			
		} catch (MalformedURLException  e) { 
			e.printStackTrace();
		}
	     try {
	    	 HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
	    	 conn.setConnectTimeout(0);
	    	 conn.setDoInput(true);
	    	 conn.connect();
	    	 InputStream iStream = conn.getInputStream();
	    	 bitmap = BitmapFactory.decodeStream(iStream);
	    	 iStream.close();
	    	 
		} catch (IOException  e) {
		    e.printStackTrace();
		}
	    return bitmap;
	}

}
