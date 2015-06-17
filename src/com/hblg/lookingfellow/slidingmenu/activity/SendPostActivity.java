package com.hblg.lookingfellow.slidingmenu.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.selfdefinedwidget.SendpostEditText;

public class SendPostActivity extends Activity {
	Button goback;
	Button sendPost;
	SendpostEditText contentEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sendpost);
		goback = (Button)this.findViewById(R.id.sendpost_goback);
		sendPost = (Button)this.findViewById(R.id.sendpost);
		contentEditText = (SendpostEditText)this.findViewById(R.id.sendpost_content);
		goback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		sendPost.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//TODO
				Toast.makeText(getApplicationContext(), "·¢Ìû", 0);
			}
		});
	}
	
}
