package com.hblg.lookingfellow.tools;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Common;
import com.hblg.lookingfellow.entity.User;
/**
 * ����ͼ����Ĺ�����
 * @author Silocean
 *
 */
public class ImageTool {
	
	private static Bitmap output;
	
	/**
	 * ��ͼ�����Բ��
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
		output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);

		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	/**
	 * �ӱ��ػ������ȡͷ�񣬲������ڱ���
	 * @param qq �û�qq
	 * @return
	 */
	public static Bitmap getHeadImageFromLocalOrNet(Context context, String qq) {
		String rootPath = Environment.getExternalStorageDirectory() + "/lookingfellow/";
		File file = new File(rootPath + "head/" + "head_" + qq + ".jpg");
		Bitmap bitmap = null;
		if(!file.exists()) { //������ز�����ͷ�񣬾ʹӷ�������ѯ�Ƿ��и��û�ͷ������,���ز���ʾ����û�У���ʾĬ��ͷ��
			System.out.println("file is not exists");
			String headUrl = Common.PATH + "head/head_" + qq + ".jpg";
			try {
				byte[] data = ImageLoader.getImage(headUrl);
				if(data == null) { // ��û�У���ʾĬ��ͷ��
					bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.head_default);
				} else { // ����,���ز���ʾ
					bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
					if(CheckSDCard.hasSdcard()) { // ����ͷ�񵽱���SD��
						String path = rootPath + "head/";
						String headName = "head_" + qq + ".jpg";
						File tempFile = new File(path);
						if(!tempFile.exists()) {
							tempFile.mkdirs();
						}
						FileOutputStream fos = new FileOutputStream(path + headName);
						boolean flag = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					} else {
						System.out.println("SD��������");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else { // ��������и��û�ͷ��ֱ�Ӵӱ���ȡ��ͷ����ʾ
			System.out.println("get headimage from local");
			bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		}
		return bitmap;
	}
	
	/**
	 * �ӱ��ػ������ȡͷ�񱳾�ͼƬ���������ڱ���
	 * @param qq �û�qq
	 * @return
	 */
	public static Bitmap getHeadImageBgFromLocalOrNet(Context context, String qq) {
		String rootPath = Environment.getExternalStorageDirectory() + "/lookingfellow/";
		File file = new File(rootPath + "headbg/" + "headbg_" + qq + ".jpg");
		Bitmap bitmap = null;
		if(!file.exists()) { //������ز�����ͷ�񣬾ʹӷ�������ѯ�Ƿ��и��û�ͷ������,���ز���ʾ����û�У���ʾĬ��ͷ��
			System.out.println("file is not exists");
			String headUrl = Common.PATH + "headbg/headbg_" + qq + ".jpg";
			try {
				byte[] data = ImageLoader.getImage(headUrl);
				if(data == null) { // ��û�У���ʾĬ��ͷ��
					bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.head_default);
				} else { // ����,���ز���ʾ
					bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
					if(CheckSDCard.hasSdcard()) { // ����ͷ�񵽱���SD��
						String path = rootPath + "headbg/";
						String headName = "headbg_" + qq + ".jpg";
						File tempFile = new File(path);
						if(!tempFile.exists()) {
							tempFile.mkdirs();
						}
						FileOutputStream fos = new FileOutputStream(path + headName);
						boolean flag = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					} else {
						System.out.println("SD��������");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else { // ��������и��û�ͷ��ֱ�Ӵӱ���ȡ��ͷ����ʾ
			System.out.println("get headImageBg from local");
			bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		}
		return bitmap;
	}
	
}
