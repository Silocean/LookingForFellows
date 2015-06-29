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
 * 应用程序更新工具包
 * @version 1.0
 */
public class UpdateManager {

	private static final int DOWN_NOSDCARD = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;

	private static UpdateManager updateManager;

	private Context mContext;
	/**
	 *  通知对话框
	 */
	private Dialog noticeDialog;
	/**
	 * 下载对话框
	 */
	private Dialog downloadDialog;
	/**
	 *  进度条
	 */
	private ProgressBar mProgress;
	/**
	 *  查询动画
	 */
	private ProgressDialog mProDialog;

	private int progress;
	/**
	 *  下载线程
	 */
	private Thread downLoadThread;
	/**
	 *  终止标记
	 */
	private boolean interceptFlag = false;
	/**
	 *  提示语
	 */
	private String updateMsg = "";
	/**
	 *  返回的安装包url
	 */
	private String apkUrl = "";
	/**
	 *  下载包保存路径
	 */
	private String savePath = "";
	/**
	 *  apk保存完整路径
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
				Toast.makeText(mContext, "无法下载安装文件，请检查SD卡是否挂载", 3000).show();
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
	 * 检查App更新
	 * 
	 * @param context
	 * @param isShowMsg
	 *  是否显示提示消息
	 */
	public void checkAppUpdate(Context context, final boolean isShowMsg) {
		this.mContext = context;
		getCurrentVersion();
		if (isShowMsg)
			mProDialog = ProgressDialog.show(mContext, null, "正在检测，请稍后...",
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
							updateMsg="嘿嘿，有更新咯！当前版本号为"+curVersionCode+".0;"+"要更新的版本号为"+mUpdate.getVersionCode()+".0";
							showNoticeDialog();
						} else if (isShowMsg) {
							AlertDialog.Builder builder = new Builder(mContext);
							builder.setTitle("系统提示");
							builder.setMessage("您当前已经是最新版本");
							builder.setPositiveButton("确定", null);
							builder.create().show();
						}
					}
				} else if (isShowMsg) {
					AlertDialog.Builder builder = new Builder(mContext);
					builder.setTitle("系统提示");
					builder.setMessage("无法获取版本更新信息");
					builder.setPositiveButton("确定", null);
					builder.create().show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					deleteApk();
					//在这里从后台中获得版本信息
					/*String response=downLoaddataFromNet("url");
					Update update =Update.parse("response");*/
					
					//临时测试
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
	 * 获取当前客户端版本信息
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
	 * 显示版本更新通知对话框
	 */
	private void showNoticeDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setCancelable(false);
		builder.setTitle("软件版本更新");
		builder.setMessage(updateMsg);
		builder.setPositiveButton("立即更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示下载对话框
	 */
	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("正在下载新版本");

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
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
				// 判断是否挂载了SD卡
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

				// 没有挂载SD卡，无法下载文件
				if (apkFilePath == null || apkFilePath == "") {
					downHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}
				File ApkFile = new File(apkFilePath);

				// 是否已下载更新文件
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
				interceptFlag=false;//我后来加的，默认的是false，即表示可以下载，
				
				//当点击取消下载后，这个被设置成true,意思是不能继续开这个下载线程

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					downHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						downHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载
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
	 * 下载apk
	 * 
	 * @param url
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
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
	
	/**头像文件*/
	private static final File HEAD_PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/lookingfellow/HeadPhoto");
	
	public void deleteMyData(){
		//根据情况而定，有需要删除的数据可在这里删除
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
