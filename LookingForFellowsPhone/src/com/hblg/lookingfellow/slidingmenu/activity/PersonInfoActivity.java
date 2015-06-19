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
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Student;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.ImageTool;
import com.hblg.lookingfellow.user.User;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personinfo);
		headImage = (ImageView)this.findViewById(R.id.personinfo_headimage_icon);
		//对头像做圆形处理
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
		SQLiteService service = new SQLiteService(getApplicationContext());
		String qq = User.qq;
		Student stu = service.getStuInfo(qq);
		nameTextView.setText(stu.getName());
		if(stu.getSex().equals("")) {
			sexTextView.setText("未设置");
		} else {
			sexTextView.setText(stu.getSex());
		}
		homeTownTextView.setText(stu.getHometown());
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
			if(popupWindow.isShowing()) {
				// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏  
				popupWindow.dismiss();
			} else {
				popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
			break;
		case R.id.personinfo_personhomepagebg_button:
			if(popupWindow.isShowing()) {
				// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏  
				popupWindow.dismiss();
			} else {
				popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
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
		case R.id.headimage_popupwindow_takenewpicture:
			//TODO
			Toast.makeText(getApplicationContext(), "拍摄", 0).show();
			break;
		case R.id.headimage_popupwindow_takeoldpicture:
			Toast.makeText(getApplicationContext(), "从相册选择", 0).show();
			break;
		case R.id.headimage_popupwindow_cancel:
			if(popupWindow.isShowing()) {
				// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏  
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
