package com.hblg.lookingfellow.customwidget;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;

public class SendpostEditText extends EditText {

	public SendpostEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public SendpostEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}
	
	public void insetExpression(int start, String tag) {
		Editable editable = this.getText();
		editable.insert(start, tag);
	}
}
