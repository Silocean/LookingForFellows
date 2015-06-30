package com.hblg.lookingfellow.slidingmenu.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Common;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.CheckSDCard;
import com.hblg.lookingfellow.tools.FormFile;
import com.hblg.lookingfellow.tools.ImageTool;
import com.hblg.lookingfellow.tools.SocketHttpRequester;

public class PersonInfoActivity extends Activity implements OnClickListener{
	Button gobackButton;
	ImageView headImage;
	Button headImageButton;
	Button personhomepagebgButton;
	Button nameButton;
	TextView nameTextView;
	Button sexButton;
	TextView sexTextView;
	Button hometownButton;
	TextView homeTownTextView;
	Button signsButton;
	TextView signsTextView;
	Button mobileButton;
	TextView mobileTextView;
	Button passwordButton;
	
	LayoutInflater inflater;
	PopupWindow popupWindow;
	View popupView;
	
	Button takeNewPictureButton;
	Button takeOldPictureButton;
	Button cancelButton;
	
	String rootPath = Environment.getExternalStorageDirectory() + "/lookingfellow/";
	
	Message msg;
	ProgressDialog dialog;
	UIHandler handler = new UIHandler();
	
	private class UIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "上传成功", 0).show();
				break;
			case 2 :
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "上传失败", 0).show();
				break;
			default:
				break;
			}
		}
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("PersonInfoActivity", this);
		setContentView(R.layout.activity_personinfo);
		headImage = (ImageView)this.findViewById(R.id.personinfo_headimage_icon);
		gobackButton = (Button)this.findViewById(R.id.personinfo_goback_button);
		gobackButton.setOnClickListener(this);
		headImageButton = (Button)this.findViewById(R.id.personinfo_headimage_button);
		headImageButton.setOnClickListener(this);
		personhomepagebgButton = (Button)this.findViewById(R.id.personinfo_personhomepagebg_button);
		personhomepagebgButton.setOnClickListener(this);
		nameButton = (Button)this.findViewById(R.id.personinfo_name_button);
		nameButton.setOnClickListener(this);
		sexButton = (Button)this.findViewById(R.id.personinfo_sex_button);
		sexButton.setOnClickListener(this);
		hometownButton = (Button)this.findViewById(R.id.personinfo_hometown_button);
		hometownButton.setOnClickListener(this);
		signsButton = (Button)this.findViewById(R.id.personinfo_signs_button);
		signsButton.setOnClickListener(this);
		mobileButton = (Button)this.findViewById(R.id.personinfo_mobile_button);
		mobileButton.setOnClickListener(this);
		passwordButton = (Button)this.findViewById(R.id.personinfo_password_button);
		passwordButton.setOnClickListener(this);
		initHeadimagePopupWindow();
		takeNewPictureButton=(Button)popupView.findViewById(R.id.headimage_popupwindow_takenewpicture);
		takeOldPictureButton = (Button)popupView.findViewById(R.id.headimage_popupwindow_takeoldpicture);
		cancelButton = (Button)popupView.findViewById(R.id.headimage_popupwindow_cancel);
		cancelButton.setOnClickListener(this);
		nameTextView = (TextView)this.findViewById(R.id.personinfo_name);
		sexTextView = (TextView)this.findViewById(R.id.personinfo_sex);
		homeTownTextView = (TextView)this.findViewById(R.id.personinfo_hometown);
		signsTextView = (TextView)this.findViewById(R.id.personinfo_personsigns);
		mobileTextView = (TextView)this.findViewById(R.id.personinfo_mobile);
		initStuInfo();
	}
	@Override
	protected void onResume() {
		initStuInfo();
		super.onResume();
	}
	
	private void initStuInfo() {
		File file = new File(rootPath + "head/" + "head_" + User.qq + ".jpg");
		Bitmap bitmap;
		if(!file.exists()) {
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head_default);
		} else {
			bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		}
		Bitmap output = ImageTool.toRoundCorner(bitmap, 360.0f);
		headImage.setImageBitmap(output);
		SQLiteService service = new SQLiteService(getApplicationContext());
		String qq = User.qq;
		Student stu = service.getStuInfo(qq);
		nameTextView.setText(stu.getName());
		if(stu.getSex().equals("")) {
			sexTextView.setText("未设置");
		} else {
			sexTextView.setText(stu.getSex());
		}
		homeTownTextView.setText(stu.getProvince() + " " + stu.getCity());
		if(stu.getSigns().equals("")) {
			signsTextView.setText("未设置");
		} else {
			signsTextView.setText(stu.getSigns());
		}
		if(stu.getPhone().equals("")) {
			mobileTextView.setText("未设置");
		} else {
			mobileTextView.setText(stu.getPhone());
		}
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.personinfo_goback_button:
			this.finish();
			break;
		case R.id.personinfo_headimage_button:
			this.dismissPopwindow(v, "head");
			break;
		case R.id.personinfo_personhomepagebg_button:
			this.dismissPopwindow(v, "headbg");
			break;
		case R.id.personinfo_name_button:
			intent = new Intent(getApplicationContext(), ModifyNameActivity.class);
			intent.putExtra("name", nameTextView.getText().toString().trim());
			startActivity(intent);
			break;
		case R.id.personinfo_sex_button:
			intent = new Intent(getApplicationContext(), ModifySexActivity.class);
			if(nameTextView.getText().toString().trim().equals("未设置")) {
				intent.putExtra("sex", "");
			} else {
				intent.putExtra("sex", sexTextView.getText().toString().trim());
			}
			startActivity(intent);
			break;
		case R.id.personinfo_hometown_button:
			intent = new Intent(getApplicationContext(), ChooseProvinceActivity.class);
			intent.putExtra("tag", "modify_activity");
			startActivity(intent);
			break;
		case R.id.personinfo_signs_button:
			intent = new Intent(getApplicationContext(), PersonSignsActivity.class);
			if(signsTextView.getText().toString().trim().equals("未设置")) {
				intent.putExtra("signs", "");
			} else {
				intent.putExtra("signs", signsTextView.getText().toString().trim());
			}
			startActivity(intent);
			break;
		case R.id.personinfo_mobile_button:
			intent = new Intent(getApplicationContext(), ModifyMobileActivity.class);
			if(mobileTextView.getText().toString().trim().equals("未设置")) {
				intent.putExtra("mobile", "");
			} else {
				intent.putExtra("mobile", mobileTextView.getText().toString().trim());
			}
			startActivity(intent);
			break;
		case R.id.personinfo_password_button:
			intent = new Intent(getApplicationContext(), ModifyPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.headimage_popupwindow_cancel:
			this.dismissPopwindow(v, "");
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1: {
			File file = new File(rootPath + "head/" + "tempHead.jpg");
			startPhotoZoom(Uri.fromFile(file), 100, 100, "head");
			break;
		}
		case 2: {
			if(data != null) {
				startPhotoZoom(data.getData(), 100, 100, "head"); 
			}
			break;
		}
		case 3: {
			if(data != null) {
				Bitmap bitmap = data.getExtras().getParcelable("data");
				saveImage(bitmap, "head");
			}
			break;
		}
		case 4: {
			File file = new File(rootPath + "headbg/" + "tempHeadBg.jpg");
			startPhotoZoom(Uri.fromFile(file), 200, 150, "headbg");
			break;
		}
		case 5: {
			if(data != null) {
				startPhotoZoom(data.getData(), 200, 150, "headbg"); 
			}
			break;
		}
		case 6: {
			if(data != null) {
				Bitmap bitmap = data.getExtras().getParcelable("data");
				saveImage(bitmap, "headbg");
			}
			break;
		}
		default:
			break;
		}
	}
	private void saveImage(Bitmap bitmap, String tag) {
		FileOutputStream fos = null;
		if(CheckSDCard.hasSdcard()) {
			try {
				String path = null;
				String headName = null;
				if(tag.equals("head")) {
					path = rootPath + "head/";
					headName = "head_" + User.qq + ".jpg";
				} else if(tag.equals("headbg")) {
					path = rootPath + "headbg/";
					headName = "headbg_" + User.qq + ".jpg";
				}
				File file = new File(path);
				if(!file.exists()) {
					file.mkdirs();
				}
				fos = new FileOutputStream(path + headName);
				boolean flag = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); // 保存头像到本地SD卡
				if(flag)  { //上传头像
					dialog = ProgressDialog.show(PersonInfoActivity.this, "", "正在上传...", true);
					String url = Common.PATH + "GetImageServlet";
					File imageFile = new File(path, headName);
					if(imageFile.exists()) {
						FormFile formFile = new FormFile(imageFile, "image", "image/*");
						Map<String, String> params = new HashMap<String, String>();
						params.put("tag", tag);
						params.put("imageName", headName);
						boolean result = SocketHttpRequester.post(url, params, formFile);
						if(result) {
							msg = handler.obtainMessage(1); // 1表示上传成功
							msg.sendToTarget();
						} else {
							msg = handler.obtainMessage(2); // 2表示上传失败
							msg.sendToTarget();
						}
					}
				}
				System.out.println(path);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(fos != null) {
						fos.flush();
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			Toast.makeText(getApplicationContext(), "SD卡不可用", 0).show();
		}
	}
	private void startPhotoZoom(Uri uri, int width, int height, String tag) {
		Intent intent = new Intent("com.android.camera.action.CROP"); 
		intent.setDataAndType(uri, "image/*"); 
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪 
		intent.putExtra("crop", "true"); 
		// aspectX aspectY 是宽高的比例 
		intent.putExtra("aspectX", 1); 
		intent.putExtra("aspectY", 1); 
		// outputX outputY 是裁剪图片宽高 
		intent.putExtra("outputX", width); 
		intent.putExtra("outputY", height); 
		intent.putExtra("return-data", true);
		if(tag.equals("head")) {
			startActivityForResult(intent, 3);
		} else if(tag.equals("headbg")) {
			startActivityForResult(intent, 6);
		}
	}
	private void dismissPopwindow(View v, final String tag) {
		if(popupWindow.isShowing()) {
			// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏  
			popupWindow.dismiss();
		} else {
			popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			if(tag.equals("head")) { // 如果是更改头像图片
				takeNewPictureButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dismissPopwindow(v, tag);
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //调用系统照相机
						//下面这句指定调用相机拍照后的照片存储的路径 
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(rootPath + "head/", "tempHead.jpg")));
						startActivityForResult(intent, 1);
					}
				});
				takeOldPictureButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dismissPopwindow(v, tag);
						Intent intent = new Intent(Intent.ACTION_PICK);
						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"); // 调用系统图库
						startActivityForResult(intent, 2);
					}
				});
			} else if(tag.equals("headbg")) { // 如果是更改个人主页背景图片
				takeNewPictureButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dismissPopwindow(v, tag);
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //调用系统照相机
						//下面这句指定调用相机拍照后的照片存储的路径 
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(rootPath + "headbg/", "tempHeadBg.jpg")));
						startActivityForResult(intent, 4);
					}
				});
				takeOldPictureButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dismissPopwindow(v, tag);
						Intent intent = new Intent(Intent.ACTION_PICK);
						intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"); // 调用系统图库
						startActivityForResult(intent, 5);
					}
				});
			}
			
		}
	}
	private void initHeadimagePopupWindow() {
		inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = inflater.inflate(R.layout.popupwindow_headimage, null);
		popupWindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, false);
		// 设置此参数获得焦点，否则无法点击
		popupWindow.setFocusable(true);  
		// 需要设置一下此参数，点击外边可消失 
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失  
		popupWindow.setOutsideTouchable(true);
		// 设置窗口动画效果
		popupWindow.setAnimationStyle(R.style.AnimationPreview);
	}
	
}
