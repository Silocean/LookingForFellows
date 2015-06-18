package com.hblg.lookingfellow.slidingmenu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.hblg.lookingfellow.R;

public class ModifySexActivity extends Activity implements OnClickListener, OnCheckedChangeListener{
	Button gobackButton;
	Button modifySexButton;
	RadioGroup radioGroup;
	RadioButton maleRadioButton;
	RadioButton femaleRadioButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifysex);
		gobackButton = (Button)this.findViewById(R.id.modifysex_goback_button);
		gobackButton.setOnClickListener(this);
		radioGroup = (RadioGroup)this.findViewById(R.id.radiogroup);
		radioGroup.setOnCheckedChangeListener(this);
		maleRadioButton = (RadioButton)this.findViewById(R.id.modifysex_radiobutton_male);
		femaleRadioButton = (RadioButton)this.findViewById(R.id.modifysex_radiobutton_female);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.modifysex_goback_button:
			this.finish();
			break;
		default:
			break;
		}
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.modifysex_radiobutton_male:
			//TODO
			Toast.makeText(getApplicationContext(), "male", 0).show();
			break;
		case R.id.modifysex_radiobutton_female:
			//TODO
			Toast.makeText(getApplicationContext(), "famale", 0).show();
			break;
		default:
			break;
		}
	}

}
