package com.aeolus.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import com.aeolus.service.CWcfDataRequest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

@SuppressLint("HandlerLeak")
public class UpdateManager {
	/* 更新是否 */
	private static final int UPDATE = 10;
	/* 下载中 */
	private static final int DOWNLOAD = 11;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 12;
	/* 保存解析的XML信息 */
	HashMap<String, Object> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;
	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private String CurVersionName = null;
	private String SerVersionName = null;
	private static int CurVersionCode = 0;
	private static int SerVersionCode = 0;
	private ArrayList<HashMap<String, Object>> arrayList = null;

	// 确定构造函数
	public UpdateManager(Context context) {
		this.mContext = context;
	}

	// 提供访问接口,判断是否需要更新
	public void CheckUpdateInfo() {
		getCurVisionInfo();
		getSerVisionInfo();
		Log.v("CurVersionCode", String.valueOf(CurVersionCode));
		Log.v("SerVersionCode", String.valueOf(SerVersionCode));
	}

	// 获取当前版本信息
	private void getCurVisionInfo() {
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			CurVersionCode = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode;
			CurVersionName = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionName;
			Log.v("CurVersionCode", String.valueOf(CurVersionCode));
			Log.v("CurVersionName", String.valueOf(CurVersionName));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 获取服务器版本号
	public void getSerVisionInfo() {
		new Thread(new Runnable() {
			public void run() {
				CWcfDataRequest request = new CWcfDataRequest();
				try {
					String proceName = "CheckPackageVerson";
					String[] paramKeys = new String[] { "PackageName" };
					String[] paramVals = new String[] { mContext
							.getPackageName() };
					String resultString = request.DataRequest_By_SimpDEs(proceName, paramKeys, paramVals);
					arrayList = new ArrayList<HashMap<String, Object>>();
					arrayList = request.LoadSingleDataSource(resultString); 
					
					if (arrayList.size() <= 0) {
						throw new JSONException("返回数据为空，请联系服务商！！！");
					}
					mHashMap = arrayList.get(0);
					Log.v("hashmanp", mHashMap.toString());

					Message message = new Message();
					message.what = UPDATE;
					mHandler.sendMessage(message);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE:
				if (mHandler != null) {

					SerVersionCode = (Integer) mHashMap.get("Version_Number");
					SerVersionName = (String) mHashMap.get("Version_Name");

					Log.v("SerVersionCode", String.valueOf(SerVersionCode));
					Log.v("SerVersionName", String.valueOf(SerVersionName));

					if (SerVersionCode > CurVersionCode) {
						ShowNoticeDialog();
					} else {
						// Toast.makeText(mContext, R.string.soft_update_no,
						// Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case DOWNLOAD:
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				Log.v("progress", String.valueOf(progress));
				InstallApk();
				break;
			default:
				break;
			}
		};
	};

	public void ShowNoticeDialog() {

		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("更新提示");
		builder.setMessage(mHashMap.get("UpdateTips").toString());
		// 更新
		builder.setPositiveButton("立即更新", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				ShowDownloadDialog();
			}
		});
		// 稍后更新
		builder.setNegativeButton("以后再说", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 显示软件下载对话框
	 */
	@SuppressLint({ "InflateParams", "UseValueOf" })
	private void ShowDownloadDialog() {
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("正在更新");
		// 给下载对话框增加进度条
		/*mProgress = new ProgressBar(mContext,null, android.R.attr.progressBarStyleHorizontal);
		mProgress.setLayoutParams(new FrameLayout.LayoutParams(65, 15,Gravity.CENTER_VERTICAL));*/
		mProgress = new ProgressBar(mContext);
		BeanUtils.setFieldValue(mProgress, "mOnlyIndeterminate", new Boolean(
				false));
		mProgress.setIndeterminate(false);
		mProgress.setProgressDrawable(mContext.getResources().getDrawable(
				android.R.drawable.progress_horizontal));
		mProgress.setIndeterminateDrawable(mContext.getResources().getDrawable(
				android.R.drawable.progress_indeterminate_horizontal));
		mProgress.setLayoutParams(new FrameLayout.LayoutParams(65, 5,
				Gravity.CENTER_VERTICAL));
		builder.setView(mProgress);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		DownloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void DownloadApk() {
		// 启动新线程下载软件
		new DownloadApkThread().start();
	}

	private class DownloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL((String) mHashMap.get("Download_link"));
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();
					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath,
							(String) mHashMap.get("Version_Name"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void InstallApk() {
		File apkfile = new File(mSavePath,
				(String) mHashMap.get("Version_Name"));
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}

class BeanUtils {
	private BeanUtils() {

	}

	/**
	 * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
	 */
	public static void setFieldValue(final Object object,
			final String fieldName, final Object value) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null)
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");
		makeAccessible(field);
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			Log.e("zbkc", "", e);
		}
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 */
	protected static Field getDeclaredField(final Object object,
			final String fieldName) {
		return getDeclaredField(object.getClass(), fieldName);
	}

	/**
	 * 循环向上转型,获取类的DeclaredField.
	 */
	protected static Field getDeclaredField(final Class<?> clazz,
			final String fieldName) {
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 强制转换fileld可访问.
	 */
	protected static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}
}
