package com.hblg.lookingfellow.customwidget;

import java.io.UnsupportedEncodingException;

import com.hblg.lookingfellow.slidingmenu.activity.PersonSignsActivity;

import android.app.Activity;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

public class SignsTextWatcher implements TextWatcher {
	int maxLen;
	int contentLen;
	int keep;
	private CharSequence temp;
	private int editStart;
	private int editEnd;
	EditText editText;
	Activity activity;
	PersonSignsActivity psActivity;
	public SignsTextWatcher(int maxLen, EditText editText, Activity activity) {
		this.maxLen = maxLen;
		this.editText = editText;
		this.activity = activity;
		psActivity = (PersonSignsActivity)this.activity;
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		temp = s;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

	@Override
	public void afterTextChanged(Editable s) {

		editStart = editText.getSelectionStart();
		editEnd = editText.getSelectionEnd();
		
		try {
			contentLen = temp.toString().trim().getBytes("GBK").length;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		keep = maxLen - contentLen;
		System.out.println(keep);
		Message msg = psActivity.handler.obtainMessage(1);
		msg.arg2 = keep;
		msg.sendToTarget();
		
		if (contentLen > maxLen) {
			Toast.makeText(this.activity, "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT).show();
			s.delete(editStart - 1, editEnd);
			int tempSelection = editStart;
			editText.setText(s);
			editText.setSelection(tempSelection);
		}
	}

}
