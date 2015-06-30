package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Common;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.tools.FormFile;
import com.hblg.lookingfellow.tools.SocketHttpRequester;
import com.hblg.lookingfellow.tools.StreamTool;
import com.hblg.lookingfellow.tools.TimeConvertTool;
import com.hblg.lookingfellow.tools.UUIDGenerator;

public class SendPostActivity extends Activity implements OnClickListener {
	private LinearLayout editBg;
	
	private static final int DIALOG_PIC = 1;
	private static final int DIALOG_EXIT = 2;
	
	private Button goback;
	private Button sendPost;
	
	private EditText postEditText;
	private Button bacBut;
	private Button atBut;
	private Button addPicBut;
	
	private ProgressDialog dialog;
	private Message msg = new Message();
	private UIHandler handler = new UIHandler(this);
	
	private String rootPath = Environment.getExternalStorageDirectory() + "/lookingfellow/";
	
	private Map<String, String> imageMap = new HashMap<String, String>();
	
	private class UIHandler extends Handler {
		SendPostActivity activity;

		public UIHandler(SendPostActivity activity) {
			this.activity = activity;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "发帖成功", 0).show();
				setResult(1);
				this.activity.finish();
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "发帖失败", 0).show();
				setResult(2);
				break;
			case 3:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "网络连接出现问题", 0).show();
				setResult(3);
				break;
			case 4:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "上传图片失败", 0).show();
				setResult(4);
			default:
				break;
			}
		}
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_PIC: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("操作").setItems(new String[] { "拍照", "相册" },
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统照相机
							// 下面这句指定调用相机拍照后的照片存储的路径
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(rootPath + "post/", "postTemp.jpg")));
							intent.putExtra("return-data", true); // 一定要加上这句话，否则没有返回数据
							startActivityForResult(intent, 1);
						} else {
							Intent intent = new Intent(Intent.ACTION_PICK);
							intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"); // 调用系统图库
							intent.putExtra("return-data", true); // 一定要加上这句话，否则没有返回数据
							startActivityForResult(intent, 1);
						}
					}
				});
			return builder.create();
		}
		case DIALOG_EXIT: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("提醒").setMessage("是否放弃正在编辑的内容")
	               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   finish();
	                   }
	               })
	               .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   
	                   }
	               });
	        return builder.create();
		}
		default:
			break;
		}
		return null;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (data != null) {
				Bitmap bitmap = data.getExtras().getParcelable("data");
				String[] proj = {MediaStore.Images.Media.DATA};
				Uri uri = data.getData();
	            //好像是android多媒体数据库的封装接口，具体的看Android文档
	            Cursor cursor = managedQuery(uri, proj, null, null, null); 
	            //按我个人理解 这个是获得用户选择的图片的索引值
	            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	            //将光标移至开头 ，这个很重要，不小心很容易引起越界
	            cursor.moveToFirst();
	            //最后根据索引值获取图片路径
	            String path = cursor.getString(column_index);
	            //System.out.println(path);
				insertImage(path, bitmap);
			}
			break;
		default:
			break;
		}
	}
	public void insertImage(String imagePath,Bitmap image) {
		int imageCount = getImageNum();
		if(imageCount <= 4) { // 一次最多上传5张
			Bitmap bm = BitmapFactory.decodeFile(imagePath);
			int height = bm.getHeight();
			String imageName = "[" + UUIDGenerator.getUUID() + "]_" + height + ".jpg";
			Editable eb = postEditText.getEditableText();
			//获得光标所在位置
			int qqPosition = postEditText.getSelectionStart();
			SpannableString ss = new SpannableString(imageName);
			//定义插入图片
			imageMap.put(imageName.substring(1, imageName.indexOf('_')-1)+imageName.substring(imageName.indexOf('_'), imageName.length()), imagePath);
			//Toast.makeText(getApplicationContext(), bm.getWidth() + ":" + bm.getHeight(), 0).show();
			Drawable drawable = new BitmapDrawable(bm);
			ss.setSpan(new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE), 0 , ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			drawable.setBounds(2 , 0 , drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			//插入图片
			eb.insert(qqPosition, ss);
		} else { // 如果图片超过5张
			Toast.makeText(getApplicationContext(), "一次最多只能添加5张照片", 1).show();
		}
	}
	/**
	 * 或内容中的图片数量
	 * @return
	 */
	public int getImageNum() {
		int count = 0;
		String content = postEditText.getText().toString();
		String zhengze = "\\[[0-9a-z]{32}\\]_\\d+.jpg";
		Pattern pattern = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()) {		
			count ++;
		}
		return count;
	}
	/**
	 * 获取内容中的所有图片名字
	 * @return
	 */
	public List<String> getImageName() {
		List<String> nameList = new ArrayList<String>();
		String content = postEditText.getText().toString();
		String zhengze = "\\[[0-9a-z]{32}\\]_\\d+.jpg";
		Pattern pattern = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()) {
			String imageName = matcher.group();
			imageName = imageName.substring(1, imageName.indexOf('_')-1) + imageName.substring(imageName.indexOf('_'), imageName.length());
			nameList.add(imageName);
		}
		return nameList;
	}
	
	/**
	 * 获取已添加图片路径
	 */
	public Map<String, String> getImagePath() {
		// 获取所有已添加图片名字
		List<String> names = this.getImageName();
		for (Map.Entry<String, String> entry : imageMap.entrySet()) {
			for(int i=0; i<names.size(); i++) {
				if(!names.contains(entry.getKey())) {
					imageMap.remove(entry.getKey());
				}
			}
		}
		return imageMap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("SendPostActivity", this);
		setContentView(R.layout.activity_sendpost);
		goback = (Button) this.findViewById(R.id.sendpost_goback);
		sendPost = (Button) this.findViewById(R.id.sendpost);
		postEditText = (EditText) this.findViewById(R.id.postTxt);
		addPicBut = (Button) this.findViewById(R.id.addpic);
		bacBut = (Button) this.findViewById(R.id.background);
		atBut = (Button) this.findViewById(R.id.atfriend);
		goback.setOnClickListener(this);
		sendPost.setOnClickListener(this);
		addPicBut.setOnClickListener(this);
		bacBut.setOnClickListener(this);
		atBut.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sendpost_goback: { // 返回
			String content = postEditText.getText().toString();
			if(!content.equals("")) {
				showDialog(DIALOG_EXIT);
			} else {
				finish();
			}
			break;
		}
		case R.id.sendpost: { // 发帖
			try {
				if (check()) {
					dialog = ProgressDialog.show(SendPostActivity.this, "", "请稍等...", true);
					new SendPostThread().start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case R.id.addpic: { // 添加图片
			showDialog(DIALOG_PIC);
			break;
		}
		case R.id.background: { // 更改背景
			//Toast.makeText(getApplicationContext(), this.getImageNum()+"", 1).show();
			System.out.println(imageMap);
			break;
		}
		case R.id.atfriend: { // @好友
			
			break;
		}
		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
		String content = postEditText.getText().toString();
		if(!content.equals("")) {
			showDialog(DIALOG_EXIT);
		} else {
			finish();
		}
	}
	protected boolean check() {
		String content = postEditText.getText().toString();
		if(content.equals("")) {
			Toast.makeText(getApplicationContext(), "内容不能为空", 0).show(); 
			return false;
		}
		return true;
	}

	private class SendPostThread extends Thread {
		@Override
		public void run() {
			try {
				sendPost();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void sendPost() throws Exception {
		String content = postEditText.getText().toString();
		String time = TimeConvertTool.convertToString(new Date(System.currentTimeMillis()));
		Map<String, String> params = new HashMap<String, String>();
		params.put("qq", User.qq);
		params.put("title", "");
		params.put("details", content);
		params.put("time", time);
		StringBuilder imageName = new StringBuilder();
		List<String> names = this.getImageName();
		for(int i=0; i<names.size(); i++) {
			imageName.append(names.get(i)).append(";");
		}
		if(!imageName.toString().equals("")) {
			imageName.deleteCharAt(imageName.length()-1);
		}
		params.put("imageName", imageName.toString());
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey() + "=");
			sb.append(URLEncoder.encode(entry.getValue(), "utf-8") + "&");
		}
		sb.deleteCharAt(sb.length()-1);
		System.out.println("sb:" + sb);
		byte[] data = sb.toString().getBytes();
		String path = Common.PATH + "PostsServlet";
		if(uploadImages()) { // 如果上传图片成功
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(5000);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(data.length));
			conn.setDoOutput(true);
			OutputStream out = conn.getOutputStream();
			out.write(data);
			if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream in = conn.getInputStream();
				String str = new String(StreamTool.read(in));
				if(str.equals("success")) {
					msg = handler.obtainMessage(1); // 1表示发帖成功
					msg.sendToTarget();
					return;
				} else {
					msg = handler.obtainMessage(2); // 2表示发帖失败
					msg.sendToTarget();
					return;
				}
			}
			msg = handler.obtainMessage(3); // 3表示网络连接出现问题
			msg.sendToTarget();
		} else {
			msg = handler.obtainMessage(4); // 表示图片上传失败
			msg.sendToTarget();
		}
	}
	/**
	 * 上传图片
	 * @return
	 * @throws Exception
	 */
	private boolean uploadImages() throws Exception {
		imageMap = this.getImagePath();
		if(imageMap.size()==0) {// 如果没添加图片也返回true
			return true;
		} else {
			String url = Common.PATH + "GetImageServlet";
			boolean flag = true;
			for (Map.Entry<String, String> entry : imageMap.entrySet()) {
				String imagePath = entry.getValue();
				String imageName = entry.getKey();
				File file = new File(imagePath);
				if(file.exists()) {
					FormFile formFile = new FormFile(file, "image", "image/*");
					Map<String, String> params = new HashMap<String, String>();
					params.put("tag", "post");
					params.put("imageName", imageName);
					boolean result = SocketHttpRequester.post(url, params, formFile);
					if(!result) {
						flag = false; // 如果其中任意图片上传失败，flag设为false
					}
				} else {
					Toast.makeText(getApplicationContext(), "找不到图片", 0).show();
					flag = false;
				}
			}
			return flag;
		}
	}

}
