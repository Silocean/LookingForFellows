package com.hblg.lookingfellow.slidingmenu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.tools.ImageTool;

public class PersonInfoActivity extends Activity implements OnClickListener{
	Button gobackButton;
	ImageView headImage;
	Button headImageButton;
	Button personhomepagebgButton;
	Button nameButton;
	Button sexButton;
	Button hometownButton;
	Button signsButton;
	Button mobileButton;
	Button passwordButton;
	
	LayoutInflater inflater;
	PopupWindow popupWindow;
	View popupView;
	
	Button takeNewPictureButton;
	Button takeOldPictureButton;
	Button cancelButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personinfo);
		headImage = (ImageView)this.findViewById(R.id.personinfo_headimage_icon);
		//��ͷ����Բ�δ���
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.headimage);
		Bitmap output = ImageTool.toRoundCorner(bitmap, 360.0f);
		headImage.setImageBitmap(output);
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
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.personinfo_goback_button:
			this.finish();
			break;
		case R.id.personinfo_headimage_button:
			if(popupWindow.isShowing()) {
				// ���ش��ڣ���������˵��������Сʱ������Ҫ�˷�ʽ����  
				popupWindow.dismiss();
			} else {
				popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
			break;
		case R.id.personinfo_personhomepagebg_button:
			if(popupWindow.isShowing()) {
				// ���ش��ڣ���������˵��������Сʱ������Ҫ�˷�ʽ����  
				popupWindow.dismiss();
			} else {
				popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
			break;
		case R.id.personinfo_name_button:
			intent = new Intent(getApplicationContext(), ModifyNameActivity.class);
			startActivity(intent);
			break;
		case R.id.personinfo_sex_button:
			intent = new Intent(getApplicationContext(), ModifySexActivity.class);
			startActivity(intent);
			break;
		case R.id.personinfo_hometown_button:
			intent = new Intent(getApplicationContext(), ChooseProvinceActivity.class);
			startActivity(intent);
			break;
		case R.id.personinfo_signs_button:
			intent = new Intent(getApplicationContext(), PersonSignsActivity.class);
			startActivity(intent);
			break;
		case R.id.personinfo_mobile_button:
			intent = new Intent(getApplicationContext(), ModifyMobileActivity.class);
			startActivity(intent);
			break;
		case R.id.personinfo_password_button:
			intent = new Intent(getApplicationContext(), ModifyPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.headimage_popupwindow_takenewpicture:
			//TODO
			Toast.makeText(getApplicationContext(), "����", 0).show();
			break;
		case R.id.headimage_popupwindow_takeoldpicture:
			Toast.makeText(getApplicationContext(), "�����ѡ��", 0).show();
			break;
		case R.id.headimage_popupwindow_cancel:
			if(popupWindow.isShowing()) {
				// ���ش��ڣ���������˵��������Сʱ������Ҫ�˷�ʽ����  
				popupWindow.dismiss();
			} else {
				popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
			break;
		default:
			break;
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