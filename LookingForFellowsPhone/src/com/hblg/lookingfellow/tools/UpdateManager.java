package com.hblg.lookingfellow.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Update;

/**
 * Ӧ�ó�����¹��߰�
 * @version 1.0
 */
public class UpdateManager {

	private static final int DOWN_NOSDCARD = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;

	private static UpdateManager updateManager;

	private Context mContext;
	/**
	 *  ֪ͨ�Ի���
	 */
	private Dialog noticeDialog;
	/**
	 * ���ضԻ���
	 */
	private Dialog downloadDialog;
	/**
	 *  ������
	 */
	private ProgressBar mProgress;
	/**
	 *  ��ѯ����
	 */
	private ProgressDialog mProDialog;

	private int progress;
	/**
	 *  �����߳�
	 */
	private Thread downLoadThread;
	/**
	 *  ��ֹ���
	 */
	private boolean interceptFlag = false;
	/**
	 *  ��ʾ��
	 */
	private String updateMsg = "";
	/**
	 *  ���صİ�װ��url
	 */
	private String apkUrl = "";
	/**
	 *  ���ذ�����·��
	 */
	private String savePath = "";
	/**
	 *  apk��������·��
	 */
	private String apkFilePath = "";

	private String curVersionName = "";
	private int curVersionCode;
	private Update mUpdate;

	private Handler downHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			case DOWN_NOSDCARD:
				downloadDialog.dismiss();
				Toast.makeText(mContext, "�޷����ذ�װ�ļ�������SD���Ƿ����", 3000).show();
				break;
			}
		};
	};

	public static UpdateManager getUpdateManager() {
		if (updateManager == null) {
			updateManager = new UpdateManager();
		}
		return updateManager;
	}

	/**
	 * ���App����
	 * 
	 * @param context
	 * @param isShowMsg
	 *  �Ƿ���ʾ��ʾ��Ϣ
	 */
	public void checkAppUpdate(Context context, final boolean isShowMsg) {
		this.mContext = context;
		getCurrentVersion();
		if (isShowMsg)
			mProDialog = ProgressDialog.show(mContext, null, "���ڼ�⣬���Ժ�...",
					true, true);
		final Handler newHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (isShowMsg && mProDialog != null)
					mProDialog.dismiss();
				if (msg.what == 1) {
					mUpdate = (Update) msg.obj;
					if (mUpdate != null) {
						if (curVersionCode < mUpdate.getVersionCode()) {
							apkUrl = mUpdate.getDownloadUrl();
							Log.v("URL", "apkUrl"+apkUrl);
							updateMsg="�ٺ٣��и��¿�����ǰ�汾��Ϊ"+curVersionCode+".0;"+"Ҫ���µİ汾��Ϊ"+mUpdate.getVersionCode()+".0";
							showNoticeDialog();
						} else if (isShowMsg) {
							AlertDialog.Builder builder = new Builder(mContext);
							builder.setTitle("ϵͳ��ʾ");
							builder.setMessage("����ǰ�Ѿ������°汾");
							builder.setPositiveButton("ȷ��", null);
							builder.create().show();
						}
					}
				} else if (isShowMsg) {
					AlertDialog.Builder builder = new Builder(mContext);
					builder.setTitle("ϵͳ��ʾ");
					builder.setMessage("�޷���ȡ�汾������Ϣ");
					builder.setPositiveButton("ȷ��", null);
					builder.create().show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					deleteApk();
					//������Ӻ�̨�л�ð汾��Ϣ
					/*String response=downLoaddataFromNet("url");
					Update update =Update.parse("response");*/
					
					//��ʱ����
					Update update=Update.parse("");
					Log.v("Update",update.toString());
					msg.what = 1;
					msg.obj=update;
				} catch (Exception e) {
					e.printStackTrace();
				}
				newHandler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * ��ȡ��ǰ�ͻ��˰汾��Ϣ
	 */
	private void getCurrentVersion() {
		try {
			PackageInfo info = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0);
			curVersionName = info.versionName;
			curVersionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * ��ʾ�汾����֪ͨ�Ի���
	 */
	private void showNoticeDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setCancelable(false);
		builder.setTitle("����汾����");
		builder.setMessage(updateMsg);
		builder.setPositiveButton("��������", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * ��ʾ���ضԻ���
	 */
	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("���������°汾");

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);

		builder.setView(v);
		builder.setNegativeButton("ȡ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();

		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				String apkName = "lookingfellow" + mUpdate.getVersionName()
						+ ".apk";
				// �ж��Ƿ������SD��
				String storageState = Environment.getExternalStorageState();
				if (storageState.equals(Environment.MEDIA_MOUNTED)) {
					savePath = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + "/yxcampus/Update/";
					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdirs();
					}
					apkFilePath = savePath + apkName;
				}

				// û�й���SD�����޷������ļ�
				if (apkFilePath == null || apkFilePath == "") {
					downHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}
				File ApkFile = new File(apkFilePath);

				// �Ƿ������ظ����ļ�
				if (ApkFile.exists()) {
					downloadDialog.dismiss();
					installApk();
					return;
				}

				FileOutputStream fos = new FileOutputStream(ApkFile);

				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				int count = 0;
				byte buf[] = new byte[1024];
				
				//-------------------------->
				interceptFlag=false;//�Һ����ӵģ�Ĭ�ϵ���false������ʾ�������أ�
				
				//�����ȡ�����غ���������ó�true,��˼�ǲ��ܼ�������������߳�

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// ���½���
					downHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// �������֪ͨ��װ
						downHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// ���ȡ����ֹͣ����
				//---------------------------->
				

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * ����apk
	 * 
	 * @param url
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * ��װapk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(apkFilePath);
		if (!apkfile.exists()) {
			return;
		}
		
		
		deleteMyData();
		
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
		
		
	}
	
	/**ͷ���ļ�*/
	private static final File HEAD_PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/lookingfellow/HeadPhoto");
	
	public void deleteMyData(){
		//�����������������Ҫɾ�������ݿ�������ɾ��
		/*MyPreferences.getShare(mContext).edit().putString(MyPreferences.TOKEN, "").commit();
		MyPreferences.getShare(mContext).edit().putString(MyPreferences.Name, "").commit();
		MyPreferences.getShare(mContext).edit().putString(MyPreferences.Password, "").commit();
		MyPreferences.getShare(mContext).edit().putString(MyPreferences.Email, "").commit();
		MyPreferences.getShare(mContext).edit().putString(MyPreferences.PHONE, "").commit();
		if(HEAD_PHOTO_DIR.exists()){
			File[] files = HEAD_PHOTO_DIR.listFiles();
			if(files.length !=0){
				try{
					for(int index=0;index<files.length;index++){
						files[index].delete();
					}
				}catch(OutOfMemoryError e){
					e.printStackTrace();
				}
			}
		}*/
	}
	
	
	public void deleteApk(){
		
		File UPDATE_DIR = new File(
				Environment.getExternalStorageDirectory()
				.getAbsoluteFile() + "/lookingfellow/Update");
	    if(UPDATE_DIR.exists()){
	    	File files[]=UPDATE_DIR.listFiles();
	    	if(files.length!=0){
	    		for(int index=0;index<files.length;index++){
	    			files[index].delete();
	    		}
	    	}
	    }
		
	}
}
