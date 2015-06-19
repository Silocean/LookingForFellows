package com.hblg.lookingfellow.slidingmenu.activity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.CityListViewAdapter;
import com.hblg.lookingfellow.slidingmenu.fragment.PersonFragment;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.NetModifyStuInfoTool;
import com.hblg.lookingfellow.user.User;

public class ChooseCityActivity extends Activity {
	Button gobackButton;
	ListView listView;
	List<String> list;
	String tag;
	
	String hometown;
	
	ProgressDialog dialog;
	Message message;
	UIHandler handler = new UIHandler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosecity);
		gobackButton = (Button)this.findViewById(R.id.choosecity_goback_button);
		gobackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		Bundle bundle = getIntent().getExtras();
		String proName = bundle.getString("province");
		SQLiteService sqLiteService = new SQLiteService(getApplicationContext());
		list = sqLiteService.getAllCities(proName);
		listView = (ListView)this.findViewById(R.id.cityList);
		CityListViewAdapter adapter = new CityListViewAdapter(this, bundle, getApplicationContext(), list, R.layout.listitem_choosearea);
		listView.setAdapter(adapter);
	}
	
	public void changeHometown(String provinceName, String cityName, int tag) {
		SQLiteService service = new SQLiteService(getApplicationContext());
		String qq = User.qq;
		hometown = provinceName + " " + cityName;
		service.modifyHometown(hometown, qq);
		dialog = ProgressDialog.show(ChooseCityActivity.this, "", "请稍等...", true);
		new ModifyHometownThread(tag).start();
	}
	
	private class ModifyHometownThread extends Thread {
		int tag;
		public ModifyHometownThread(int tag) {
			this.tag = tag;
		}
		@Override
		public void run() {
			modify();
		}
		private void modify() {
			try {
				message = NetModifyStuInfoTool.connNet("hometown", hometown);
				message.arg2 = tag;
				handler.sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class UIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if(msg.arg1 == 1 && msg.arg2 == 1) {
				// 表示保存成功
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "保存成功", 0).show();
				/*PersonFragment personFragment = new PersonFragment();
				((SlidingActivity)getParent()).replaceFragment(R.id.center_frame, personFragment);
				((SlidingActivity)getParent()).showLeft();*/
			} else if(msg.arg1 == 1 && msg.arg2 == 2) {
				// 表示保存成功
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "保存成功", 0).show();
				Intent intent = new Intent(getApplicationContext(), PersonInfoActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(intent);
			} else {
				// 表示保存成功
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "保存失败", 0).show();
			}
		}
		
	}
	
}
