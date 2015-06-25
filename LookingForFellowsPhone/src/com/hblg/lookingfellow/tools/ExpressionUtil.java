package com.hblg.lookingfellow.tools;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.hblg.lookingfellow.R;



public class ExpressionUtil {
	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 */
    public static void dealExpression(Context context,SpannableString spannableString, Pattern patten, int start) throws Exception {
    	Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            Log.d("Key", key);
            if (matcher.start() < start) {
                continue;
            }
            Field field = R.drawable.class.getDeclaredField(key);
			int resId = Integer.parseInt(field.get(null).toString());		
            if (resId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                ImageSpan imageSpan = new ImageSpan(bitmap);				            
                int end = matcher.start() + key.length();					
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);	
                if (end < spannableString.length()) {						
                    dealExpression(context,spannableString,  patten, end);
                }
                break;
            }
        }
    }
    
    public static SpannableString getExpressionString(Context context,String str,String zhengze){
    	System.out.println("进来的内容 = " + str);
    	SpannableString spannableString = new SpannableString(str);
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);		//通过传入的正则表达式来生成一个pattern
        try {
            dealExpression(context,spannableString, sinaPatten, 0);
        } catch (Exception e) {
            Log.e("dealExpression", e.getMessage());
        }
        return spannableString;
    }
}