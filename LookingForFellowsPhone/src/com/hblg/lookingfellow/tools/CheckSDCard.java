package com.hblg.lookingfellow.tools;

import android.os.Environment;

public class CheckSDCard {
	/**
     * ºÏ≤È «∑Ò¥Ê‘⁄SDCard
     * @return
     */
    public static boolean hasSdcard(){
            String state = Environment.getExternalStorageState();
            if(state.equals(Environment.MEDIA_MOUNTED)){
                    return true;
            }else{
                    return false;
            }
    }
}
