package com.hblg.lookingfellow.slidingmenu.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.model.ManageActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.NetModifyStuInfoTool;

public class ModifySexActivity extends Activity implements OnClickListener{
	Button gobackButton;
	Button modifySexButton;
	
	private RelativeLayout boyRel;
	private RelativeLayout girlRel;
	private ImageView boyImg;
	private ImageView girlImg;
	
	String sex;
	
	ProgressDialog dialog;
	Message message;
	UIHandler handler = new UIHandler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("ModifySexActivity", this);
		setContentView(R.layout.activity_modifysex);
		gobackButton = (Button)this.findViewById(R.id.modifysex_goback_button);
		gobackButton.setOnClickListener(this);
		initView();
	}
	private void initView(){
		boyRel=(RelativeLayout)findViewById(R.id.modify_boy);
		girlRel=(RelativeLayout)findViewById(R.id.modify_girl);
		boyImg=(ImageView)findViewById(R.id.modify_boy_right);
		girlImg=(ImageView)findViewById(R.id.modify_girl_right);
		boyRel.setOnClickListener(this);
		girlRel.setOnClickListener(this);
		String sex = getIntent().getStringExtra("sex");
		if(sex.equals("男")) {
			boyImg.setImageDrawable(getResources().getDrawable(R.drawable.modifysex_radiobutton_checked));
			girlImg.setImageDrawable(getResources().getDrawable(R.drawable.modifysex_radiobutton_unchecked));
		} else if(sex.equals("女")) {
			girlImg.setImageDrawable(getResources().getDrawable(R.drawable.modifysex_radiobutton_checked));
			boyImg.setImageDrawable(getResources().getDrawable(R.drawable.modifysex_radiobutton_unchecked));
		}
	}
	private class UIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1 == 1) {
				// 表示保存成功
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "保存成功", 0).show();
				Intent intent = new Intent(getApplicationContext(), PersonInfoActivity.class);
				startActivity(intent);
			} else {
				// 表示保存成功
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "保存失败", 0).show();
			}
		}
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.modifysex_goback_button:
			this.finish();
			break;
		case R.id.modify_boy:
			boyImg.setImageDrawable(getResources().getDrawable(R.drawable.modifysex_radiobutton_checked));
			girlImg.setImageDrawable(getResources().getDrawable(R.drawable.modifysex_radiobutton_unchecked));
			this.modifySex("男");
			break;
		case R.id.modify_girl:
			girlImg.setImageDrawable(getResources().getDrawable(R.drawable.modifysex_radiobutton_checked));
			boyImg.setImageDrawable(getResources().getDrawable(R.drawable.modifysex_radiobutton_unchecked));
			this.modifySex("女");
			break;
		default:
			break;
		}
	}
	private void modifySex(String sex) {
		SQLiteService service = new SQLiteService(getApplicationContext());
		String qq = User.qq;
		this.sex = sex;
		service.modifySex(sex, qq);
		dialog = ProgressDialog.show(ModifySexActivity.this, "", "正在修改...", true);
		new ModifySexThread().start();
	}
	/*private void changeSex() {
		SQLiteService service = new SQLiteService(getApplicationContext());
		String qq = User.qq;
		if(maleRadioButton.isChecked()) {
			sex = "男";
		} else {
			sex = "女";
		}
		service.modifySex(sex, qq);
		dialog = ProgressDialog.show(ModifySexActivity.this, "", "正在修改...", true);
		new ModifySexThread().start();
	}*/
	
	private class ModifySexThread extends Thread {
		@Override
		public void run() {
			modify();
		}
		private void modify() {
			try {
				message = NetModifyStuInfoTool.connNet("sex", sex);
				handler.sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
