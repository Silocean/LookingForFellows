package com.hblg.lookingfellow.selfdefinedwidget;

import android.widget.GridView;

/**
 * 自定义GridView，是GridView不用下拉风格，只在布局中加入这个类的路径，其他均一样
 */
public class MyGridView extends GridView {
	public MyGridView(android.content.Context context,
			android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}

}
