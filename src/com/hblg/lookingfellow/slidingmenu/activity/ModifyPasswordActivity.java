package com.hblg.lookingfellow.slidingmenu.activity;

import com.hblg.lookingfellow.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ModifyPasswordActivity extends Activity implements OnClickListener{
	Button gobackButton;
	Button modifySaveButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifypassword);
		gobackButton = (Button)this.findViewById(R.id.modifypassword_goback_button);
		gobackButton.setOnClickListener(this);
		modifySaveButton = (Button)this.findViewById(R.id.modifypassword_save);
		modifySaveButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.modifypassword_goback_button:
			this.finish();
			break;
		case R.id.modifypassword_save:
			//TODO
			Toast.makeText(getApplicationContext(), "modifypassword", 0).show();
			break;
		default:
			break;
		}
	}

}
