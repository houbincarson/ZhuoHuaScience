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
	/* �����Ƿ� */
	private static final int UPDATE = 10;
	/* ������ */
	private static final int DOWNLOAD = 11;
	/* ���ؽ��� */
	private static final int DOWNLOAD_FINISH = 12;
	/* ���������XML��Ϣ */
	HashMap<String, Object> mHashMap;
	/* ���ر���·�� */
	private String mSavePath;
	/* ��¼���������� */
	private int progress;
	/* �Ƿ�ȡ������ */
	private boolean cancelUpdate = false;
	private Context mContext;
	/* ���½����� */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private String CurVersionName = null;
	private String SerVersionName = null;
	private static int CurVersionCode = 0;
	private static int SerVersionCode = 0;
	private ArrayList<HashMap<String, Object>> arrayList = null;

	// ȷ�����캯��
	public UpdateManager(Context context) {
		this.mContext = context;
	}

	// �ṩ���ʽӿ�,�ж��Ƿ���Ҫ����
	public void CheckUpdateInfo() {
		getCurVisionInfo();
		getSerVisionInfo();
		Log.v("CurVersionCode", String.valueOf(CurVersionCode));
		Log.v("SerVersionCode", String.valueOf(SerVersionCode));
	}

	// ��ȡ��ǰ�汾��Ϣ
	private void getCurVisionInfo() {
		try {
			// ��ȡ����汾�ţ���ӦAndroidManifest.xml��android:versionCode
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

	// ��ȡ�������汾��
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
						throw new JSONException("��������Ϊ�գ�����ϵ�����̣�����");
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
		builder.setTitle("������ʾ");
		builder.setMessage(mHashMap.get("UpdateTips").toString());
		// ����
		builder.setPositiveButton("��������", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				ShowDownloadDialog();
			}
		});
		// �Ժ����
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * ��ʾ������ضԻ���
	 */
	@SuppressLint({ "InflateParams", "UseValueOf" })
	private void ShowDownloadDialog() {
		// ����������ضԻ���
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("���ڸ���");
		// �����ضԻ������ӽ�����
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
		// ȡ������
		builder.setNegativeButton("ȡ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// �����ļ�
		DownloadApk();
	}

	/**
	 * ����apk�ļ�
	 */
	private void DownloadApk() {
		// �������߳��������
		new DownloadApkThread().start();
	}

	private class DownloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// �ж�SD���Ƿ���ڣ������Ƿ���ж�дȨ��
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// ��ô洢����·��
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL((String) mHashMap.get("Download_link"));
					// ��������
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// ��ȡ�ļ���С
					int length = conn.getContentLength();
					// ����������
					InputStream is = conn.getInputStream();
					File file = new File(mSavePath);
					// �ж��ļ�Ŀ¼�Ƿ����
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath,
							(String) mHashMap.get("Version_Name"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// ����
					byte buf[] = new byte[1024];
					// д�뵽�ļ���
					do {
						int numread = is.read(buf);
						count += numread;
						// ���������λ��
						progress = (int) (((float) count / length) * 100);
						// ���½���
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// �������
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// д���ļ�
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// ���ȡ����ֹͣ����.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// ȡ�����ضԻ�����ʾ
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * ��װAPK�ļ�
	 */
	private void InstallApk() {
		File apkfile = new File(mSavePath,
				(String) mHashMap.get("Version_Name"));
		if (!apkfile.exists()) {
			return;
		}
		// ͨ��Intent��װAPK�ļ�
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
	 * ֱ�����ö�������ֵ,����private/protected���η�,������setter����.
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
	 * ѭ������ת��,��ȡ�����DeclaredField.
	 */
	protected static Field getDeclaredField(final Object object,
			final String fieldName) {
		return getDeclaredField(object.getClass(), fieldName);
	}

	/**
	 * ѭ������ת��,��ȡ���DeclaredField.
	 */
	protected static Field getDeclaredField(final Class<?> clazz,
			final String fieldName) {
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// Field���ڵ�ǰ�ඨ��,��������ת��
			}
		}
		return null;
	}

	/**
	 * ǿ��ת��fileld�ɷ���.
	 */
	protected static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}
}
