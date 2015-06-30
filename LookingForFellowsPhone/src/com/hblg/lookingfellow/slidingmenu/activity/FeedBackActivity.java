package com.hblg.lookingfellow.slidingmenu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.model.ManageActivity;

//·´À¡
public class FeedBackActivity extends Activity{
	private Button backBtn;
    private MyListener myListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ManageActivity.addActiviy("FeedBackActivity", this);
		myListener=new MyListener();
		setContentView(R.layout.activity_feedback);
		backBtn=(Button)findViewById(R.id.feedback_goback_button);
		backBtn.setOnClickListener(myListener);
	}
	private class MyListener implements OnClickListener{
		public void onClick(View view){
			switch(view.getId()){
			case R.id.feedback_goback_button:
				finish();
				break;
			default:
				break;
			}
		}
	}
	

}
