package com.hblg.lookingfellow.tools;

import android.os.Environment;

public class Utils {
	public static boolean isSDAvailable() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

}
