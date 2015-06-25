package com.hblg.lookingfellow.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @author Fay
 * 
 */
public class NetWorkHelper {
	private static String LOG_TAG = "NetWorkHelper";

	public static boolean isNetWorkConnection(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null == connManager) {
			Log.w(LOG_TAG, "couldn't get connectivity manager");
		} else {
			NetworkInfo netWorkInfo = connManager.getActiveNetworkInfo();
			if (null != netWorkInfo) {
				if (netWorkInfo.isAvailable()) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isNetWorkWIfi(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null == connManager) {
			Log.w(LOG_TAG, "couldn't get connectivity manager");
		} else {
			NetworkInfo info = connManager.getActiveNetworkInfo();
			if (null != info) {
				if (ConnectivityManager.TYPE_WIFI == info.getType()) {
					Log.w(LOG_TAG, "network is wifi");
					return true;
				}
			}
		}
		return false;
	}

}
