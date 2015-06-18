package com.hblg.lookingfellow.slidingmenu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.hblg.lookingfellow.R;

public class PersonSignsActivity extends Activity {
	Button gobackButton;
	Button saveButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personsigns);
		gobackButton = (Button)this.findViewById(R.id.personsigns_goback);
		saveButton = (Button)this.findViewById(R.id.personsigns_save);
		gobackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//TODO
				Toast.makeText(getApplicationContext(), "save", 0);
			}
		});
	}
	
}
