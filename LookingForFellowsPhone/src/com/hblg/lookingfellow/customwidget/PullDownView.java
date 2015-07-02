package com.hblg.lookingfellow.customwidget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.customwidget.ScrollOverListView.OnScrollOverListener;

public class PullDownView extends LinearLayout implements OnScrollOverListener {

	private static final int START_PULL_DEVIATION = 50; // �ƶ����
	private static final int WHAT_DID_MORE = 5; // Handler what �Ѿ���ȡ�����
	private static final int WHAT_DID_REFRESH = 3; // Handler what �Ѿ�ˢ����
	/** �ײ�����İ��� **/
	private RelativeLayout mFooterView;
	/** �ײ�����İ��� **/
	private TextView mFooterTextView;
	/** �ײ�����İ��� **/
	private ProgressBar mFooterLoadingView;
	/** �Ѿ����� ����ˢ�¹��ܵ��б� **/
	private ScrollOverListView mListView;
	/** ˢ�º͸�����¼��ӿ� **/
	private OnPullDownListener mOnPullDownListener;
	private float mMotionDownLastY; // ����ʱ���Y������
	private boolean mIsFetchMoreing; // �Ƿ��ȡ������
	private boolean mIsPullUpDone; // �Ƿ�������
	private boolean mEnableAutoFetchMore; // �Ƿ������Զ���ȡ����

	public PullDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderViewAndFooterViewAndListView(context);
	}

	public PullDownView(Context context) {
		super(context);
		initHeaderViewAndFooterViewAndListView(context);
	}

	/*
	 * ================================== Public method �ⲿʹ�ã�����������⼸���Ϳ�����
	 */

	/**
	 * ˢ�ºͻ�ȡ�����¼��ӿ�
	 */
	public interface OnPullDownListener {
		/** ˢ���¼��ӿ� ����Ҫע����ǻ�ȡ������ Ҫ�ر� ˢ�µĽ�����RefreshComplete() **/
		void onRefresh();

		/** ˢ���¼��ӿ� ����Ҫע����ǻ�ȡ������ Ҫ�ر� ����Ľ����� notifyDidMore() **/
		void onMore();
	}

	/**
	 * ֪ͨ�Ѿ���ȡ������ˣ�Ҫ����Adapter.notifyDataSetChanged����
	 * ����ִ�����������֮�󣬵������notyfyDidMore() �Ż����ؼ���Ȧ�Ȳ���
	 */
	public void notifyDidMore() {
		mUIHandler.sendEmptyMessage(WHAT_DID_MORE);
	}

	/** ˢ����� �ر�ͷ�������� **/
	public void RefreshComplete() {
		mUIHandler.sendEmptyMessage(WHAT_DID_REFRESH);
	}

	/**
	 * ���ü�����
	 * 
	 * @param listener
	 */
	public void setOnPullDownListener(OnPullDownListener listener) {
		mOnPullDownListener = listener;
	}

	/**
	 * ��ȡ��Ƕ��listview
	 * 
	 * @return ScrollOverListView
	 */
	public ListView getListView() {
		return mListView;
	}

	/**
	 * �Ƿ����Զ���ȡ���� �Զ���ȡ���࣬��������footer�����ڵ���ײ���ʱ���Զ�ˢ��
	 * 
	 * @param index
	 *            �����ڼ�������
	 */
	public void enableAutoFetchMore(boolean enable, int index) {
		if (enable) {
			mListView.setBottomPosition(index);
			// mFooterLoadingView.setVisibility(View.VISIBLE);
		} else {
			mFooterTextView.setText("������ظ���");// ���Զ�����
			mFooterLoadingView.setVisibility(View.GONE);
		}
		mEnableAutoFetchMore = enable;
	}

	/*
	 * ================================== Private method ����ʵ������ˢ�µȲ���
	 * 
	 * ==================================
	 */

	/**
	 * ��ʼ������
	 */
	@SuppressWarnings("deprecation")
	private void initHeaderViewAndFooterViewAndListView(Context context) {
		setOrientation(LinearLayout.VERTICAL);

		/**
		 * �Զ���Ų��ļ�
		 */
		mFooterView = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.pulldown_footer, null);
		mFooterTextView = (TextView) mFooterView
				.findViewById(R.id.pulldown_footer_text);
		mFooterLoadingView = (ProgressBar) mFooterView
				.findViewById(R.id.pulldown_footer_loading);
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mIsFetchMoreing) {

					mIsFetchMoreing = true;
					mFooterTextView.setText("���ظ�����...");
					mFooterLoadingView.setVisibility(View.VISIBLE);
					mOnPullDownListener.onMore();
				}
			}
		});

		/*
		 * ScrollOverListView ͬ���ǿ��ǵ�����ʹ�ã����Է������� ͬʱ��Ϊ����Ҫ���ļ����¼�
		 */
		mListView = new ScrollOverListView(context);
		mListView.setOnScrollOverListener(this);
		mListView.setCacheColorHint(0);
		addView(mListView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		// �յ�listener
		mOnPullDownListener = new OnPullDownListener() {
			@Override
			public void onRefresh() {
			}

			@Override
			public void onMore() {
			}
		};

		mListView.addFooterView(mFooterView);
		// mListView.setAdapter(mListView.getAdapter());

	}

	private Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_REFRESH: {
				mListView.onRefreshComplete();
				return;
			}

			case WHAT_DID_MORE: {
				mIsFetchMoreing = false;
				mFooterTextView.setText("����");
				mFooterLoadingView.setVisibility(View.GONE);
			}
			}
		}

	};

	/**
	 * ��Ŀ�Ƿ�����������Ļ
	 */
	private boolean isFillScreenItem() {
		final int firstVisiblePosition = mListView.getFirstVisiblePosition();
		final int lastVisiblePostion = mListView.getLastVisiblePosition()
				- mListView.getFooterViewsCount();
		final int visibleItemCount = lastVisiblePostion - firstVisiblePosition
				+ 1;
		final int totalItemCount = mListView.getCount()
				- mListView.getFooterViewsCount();

		if (visibleItemCount < totalItemCount)
			return true;
		return false;
	}

	/*
	 * ================================== ʵ�� OnScrollOverListener�ӿ�
	 */

	@Override
	public boolean onListViewTopAndPullDown(int delta) {

		return true;
	}

	@Override
	public boolean onListViewBottomAndPullUp(int delta) {
		if (!mEnableAutoFetchMore || mIsFetchMoreing)
			return false;
		// ����������Ļ�Ŵ���
		if (isFillScreenItem()) {
			mIsFetchMoreing = true;
			mFooterTextView.setText("���ظ�����...");
			mFooterLoadingView.setVisibility(View.VISIBLE);
			mOnPullDownListener.onMore();
			return true;
		}
		return false;
	}

	@Override
	public boolean onMotionDown(MotionEvent ev) {
		mIsPullUpDone = false;
		mMotionDownLastY = ev.getRawY();

		return false;
	}

	@Override
	public boolean onMotionMove(MotionEvent ev, int delta) {
		// ��ͷ���ļ�������ʧ��ʱ�򣬲��������
		if (mIsPullUpDone)
			return true;

		// �����ʼ���µ��������벻�������ֵ���򲻻���
		final int absMotionY = (int) Math.abs(ev.getRawY() - mMotionDownLastY);
		if (absMotionY < START_PULL_DEVIATION)
			return true;

		return false;
	}

	@Override
	public boolean onMotionUp(MotionEvent ev) {
		if (ScrollOverListView.canRefleash) {
			ScrollOverListView.canRefleash = false;
			mOnPullDownListener.onRefresh();
		}
		return false;
	}

	/** ����ͷ�� ������������ **/
	public void setHideHeader() {
		mListView.showRefresh = false;
	}

	/** ��ʾͷ�� ʹ���������� **/
	public void setShowHeader() {
		mListView.showRefresh = true;
	}

	/** ���صײ� ������������ **/
	public void setHideFooter() {
		mFooterView.setVisibility(View.GONE);
		mFooterTextView.setVisibility(View.GONE);
		mFooterLoadingView.setVisibility(View.GONE);
		enableAutoFetchMore(false, 1);
	}

	/**
	 * ȥ���ײ�
	 */
	public void removeFooterView() {
		mListView.removeFooterView(mFooterView);
	}

	/**
	 * ��ӵײ�
	 */
	public void addFooterView() {
		mListView.addFooterView(mFooterView);
	}

	/** ��ʾ�ײ� ʹ���������� **/
	public void setShowFooter() {
		mFooterView.setVisibility(View.VISIBLE);
		mFooterTextView.setVisibility(View.VISIBLE);
		// mFooterLoadingView.setVisibility(View.VISIBLE);
		enableAutoFetchMore(true, 1);
	}

}
