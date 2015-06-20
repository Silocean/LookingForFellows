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
				Toast.makeText(getApplicationContext(), "�ϴ�ͷ��ɹ�", 0).show();
				break;
			case 2 :
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "�ϴ�ͷ��ʧ��", 0).show();
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
		takeNewPictureButton.setOnClickListener(this);
		takeOldPictureButton = (Button)popupView.findViewById(R.id.headimage_popupwindow_takeoldpicture);
		takeOldPictureButton.setOnClickListener(this);
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
			sexTextView.setText("δ����");
		} else {
			sexTextView.setText(stu.getSex());
		}
		homeTownTextView.setText(stu.getHometown());
		if(stu.getSigns().equals("")) {
			signsTextView.setText("δ����");
		} else {
			signsTextView.setText(stu.getSigns());
		}
		if(stu.getPhone().equals("")) {
			mobileTextView.setText("δ����");
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
			this.dismissPopwindow(v);
			break;
		case R.id.personinfo_personhomepagebg_button:
			this.dismissPopwindow(v);
			break;
		case R.id.personinfo_name_button:
			intent = new Intent(getApplicationContext(), ModifyNameActivity.class);
			intent.putExtra("name", nameTextView.getText().toString().trim());
			startActivity(intent);
			break;
		case R.id.personinfo_sex_button:
			intent = new Intent(getApplicationContext(), ModifySexActivity.class);
			if(nameTextView.getText().toString().trim().equals("δ����")) {
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
			if(signsTextView.getText().toString().trim().equals("δ����")) {
				intent.putExtra("signs", "");
			} else {
				intent.putExtra("signs", signsTextView.getText().toString().trim());
			}
			startActivity(intent);
			break;
		case R.id.personinfo_mobile_button:
			intent = new Intent(getApplicationContext(), ModifyMobileActivity.class);
			if(mobileTextView.getText().toString().trim().equals("δ����")) {
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
		case R.id.headimage_popupwindow_takenewpicture:
			this.dismissPopwindow(v);
			Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //����ϵͳ�����
			//�������ָ������������պ����Ƭ�洢��·�� 
			intent2.putExtra(MediaStore.EXTRA_OUTPUT, 
					Uri.fromFile(new File(rootPath + "head/", "tempHead.jpg")));
			startActivityForResult(intent2, 1);
			break;
		case R.id.headimage_popupwindow_takeoldpicture:
			this.dismissPopwindow(v);
			Intent intent3 = new Intent(Intent.ACTION_PICK);
			intent3.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"); // ����ϵͳͼ��
			startActivityForResult(intent3, 2);
			break;
		case R.id.headimage_popupwindow_cancel:
			this.dismissPopwindow(v);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			File file = new File(rootPath + "head/" + "tempHead.jpg");
			startPhotoZoom(Uri.fromFile(file), 100, 100);
			break;
		case 2: 
			if(data != null) {
				startPhotoZoom(data.getData(), 100, 100); 
			}
			break;
		case 3:
			if(data != null) {
				Bitmap bitmap = data.getExtras().getParcelable("data");
				saveImage(bitmap);
			}
			break;
		default:
			break;
		}
	}
	private void saveImage(Bitmap bitmap) {
		FileOutputStream fos = null;
		if(CheckSDCard.hasSdcard()) {
			try {
				String path = rootPath + "head/";
				String headName = "head_" + User.qq + ".jpg";
				File file = new File(path);
				if(!file.exists()) {
					file.mkdirs();
				}
				fos = new FileOutputStream(path + headName);
				boolean flag = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); // ����ͷ�񵽱���SD��
				if(flag)  { //�ϴ�ͷ��
					dialog = ProgressDialog.show(PersonInfoActivity.this, "", "�����ϴ�...", true);
					String url = "http://192.168.1.152:8080/lookingfellowWeb0.2/GetImageServlet";
					File imageFile = new File(path, headName);
					if(imageFile.exists()) {
						FormFile formFile = new FormFile(imageFile, "imaga", "image/*");
						Map<String, String> params = new HashMap<String, String>();
						params.put("title", "title");
						params.put("content", "content");
						boolean result = SocketHttpRequester.post(url, params, formFile);
						if(result) {
							msg = handler.obtainMessage(1); // 1��ʾ�ϴ��ɹ�
							msg.sendToTarget();
						} else {
							msg = handler.obtainMessage(2); // 2��ʾ�ϴ�ʧ��
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
			Toast.makeText(getApplicationContext(), "SD��������", 0).show();
		}
	}
	private void startPhotoZoom(Uri uri, int width, int height) {
		Intent intent = new Intent("com.android.camera.action.CROP"); 
		intent.setDataAndType(uri, "image/*"); 
		//�������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü� 
		intent.putExtra("crop", "true"); 
		// aspectX aspectY �ǿ�ߵı��� 
		intent.putExtra("aspectX", 1); 
		intent.putExtra("aspectY", 1); 
		// outputX outputY �ǲü�ͼƬ��� 
		intent.putExtra("outputX", width); 
		intent.putExtra("outputY", height); 
		intent.putExtra("return-data", true); 
		startActivityForResult(intent, 3); 
	}
	private void dismissPopwindow(View v) {
		if(popupWindow.isShowing()) {
			// ���ش��ڣ���������˵��������Сʱ������Ҫ�˷�ʽ����  
			popupWindow.dismiss();
		} else {
			popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		}
	}
	private void initHeadimagePopupWindow() {
		inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = inflater.inflate(R.layout.headimage_popupwindow, null);
		popupWindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, false);
		// ���ô˲�����ý��㣬�����޷����
		popupWindow.setFocusable(true);  
		// ��Ҫ����һ�´˲����������߿���ʧ 
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //���õ��������ߴ�����ʧ  
		popupWindow.setOutsideTouchable(true);
		// ���ô��ڶ���Ч��
		popupWindow.setAnimationStyle(R.style.AnimationPreview);
	}
	
}
