package com.hblg.lookingfellow.tools;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

public class UIMode {
	/**
	 * 切换弱光模式
	 */
	public static void changeUIMode(Activity activity, int flag) {
		if(isAutoBrightness(activity)) {
			stopAutoBrightness(activity);
		}
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		if (flag == 0) {// 弱光
			lp.screenBrightness = 0.3f;
		} else if (flag == 1) {// 正常
			lp.screenBrightness = 0.5f;
		} else if (flag == 2) {// 强光
			lp.screenBrightness = 0.8f;
		}
		activity.getWindow().setAttributes(lp);
	}
	
	/**
	 * 获取uiMODE
	 * 
	 * @return the Uimode
	 */
	public static int checkUIMode(Activity activity) {
		SharedPreferences pf = MySharePreferences.getShare(activity);
		int flag = pf.getInt(MySharePreferences.UI, 2);
		return flag;
	}

	/**
	 * 判断是否开启了自动亮度调节
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
			Toast.makeText(act, "无法获取亮度", Toast.LENGTH_SHORT).show();
		}
		return automicBrightness;
	}

	/**
	 *  停止自动亮度调节
	 * @param activity
	 */
	public static void stopAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}

	/**
	 *  开启亮度自动调节
	 * @param activity
	 */
	public static void startAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);

	}

}
