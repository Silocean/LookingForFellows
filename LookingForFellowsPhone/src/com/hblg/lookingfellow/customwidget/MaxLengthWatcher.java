package com.hblg.lookingfellow.customwidget;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

public class MaxLengthWatcher implements TextWatcher {
	int maxLen;
	int contentLen;
	private CharSequence temp;
	private int editStart;
	private int editEnd;
	EditText editText;
	Activity activity;

	public MaxLengthWatcher(int maxLen, EditText editText, Activity activity) {
		this.maxLen = maxLen;
		this.editText = editText;
		this.activity = activity;
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

		if (contentLen > maxLen) {
			Toast.makeText(this.activity, "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
					.show();
			s.delete(editStart - 1, editEnd);
			int tempSelection = editStart;
			editText.setText(s);
			editText.setSelection(tempSelection);
		}

	}
}
