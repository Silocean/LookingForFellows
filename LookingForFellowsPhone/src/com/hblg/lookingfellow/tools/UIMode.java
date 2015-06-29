package com.hblg.lookingfellow.tools;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

public class UIMode {
	/**
	 * �л�����ģʽ
	 */
	public static void changeUIMode(Activity activity, int flag) {
		if(isAutoBrightness(activity)) {
			stopAutoBrightness(activity);
		}
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		if (flag == 0) {// ����
			lp.screenBrightness = 0.3f;
		} else if (flag == 1) {// ����
			lp.screenBrightness = 0.5f;
		} else if (flag == 2) {// ǿ��
			lp.screenBrightness = 0.8f;
		}
		activity.getWindow().setAttributes(lp);
	}
	
	/**
	 * ��ȡuiMODE
	 * 
	 * @return the Uimode
	 */
	public static int checkUIMode(Activity activity) {
		SharedPreferences pf = MySharePreferences.getShare(activity);
		int flag = pf.getInt(MySharePreferences.UI, 2);
		return flag;
	}

	/**
	 * �ж��Ƿ������Զ����ȵ���
	 * 
	 * @param act
	 * @return
	 */
	public static boolean isAutoBrightness(Activity act) {
		boolean automicBrightness = false;
		ContentResolver aContentResolver = act.getContentResolver();
		try {
			automicBrightness = Settings.System.getInt(aContentResolver,
					Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		} catch (Exception e) {
			Toast.makeText(act, "�޷���ȡ����", Toast.LENGTH_SHORT).show();
		}
		return automicBrightness;
	}

	/**
	 *  ֹͣ�Զ����ȵ���
	 * @param activity
	 */
	public static void stopAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}

	/**
	 *  ���������Զ�����
	 * @param activity
	 */
	public static void startAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);

	}

}
