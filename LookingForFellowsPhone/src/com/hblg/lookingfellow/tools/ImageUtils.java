package com.hblg.lookingfellow.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.hblg.lookingfellow.R;


public class ImageUtils {
	public static String LOG_TAG ="ImageUtils";
	
	public static final String ImageFilename = "go";
	public static final String FlowImageFilename = "flow";
	
	public static LinkedHashMap<String, SoftReference<Bitmap>> imageCache = 
			new LinkedHashMap<String,SoftReference<Bitmap>>();
	
	private final static int Dafult_image = R.drawable.headimage;
	
	private final static long CLEAR_TIME = 24*24*60*60*1000;
	
	/**
	 * the entrance
	 * @param imageView
	 * @param imageUrl
	 * @param context
	 * @param callBack
	 */
	public static void setImageView(ImageView imageView,String imageUrl,Context context,ImageCallBack callBack){
		String md5 = MD5.getMD5(imageUrl);
		String imagePath = getExternalCacheDir(context,"go")+File.separator+md5;
		if(Utils.isSDAvailable()){
			setImageViewUtils( imageView,imagePath,imageUrl,context,callBack);
			imageView.setTag(imagePath);
		}else {
			setImageViewUtils( imageView,null,imageUrl,context,callBack);
			imageView.setTag(imageUrl);
		}
	}
	
	
	/**
	 * 首页顶部图片的设置
	 * @param imageView
	 * @param imageUrl
	 * @param context
	 * @param callBack
	 */
	public static void setHomeSlidImageView(ImageView imageView,String ImageName,String imageUrl,Context context,ImageCallBack callBack){
		String imagePath = getExternalCacheDir(context,"slid")+File.separator+ImageName;
		if(Utils.isSDAvailable()){
			setImageViewUtils( imageView,imagePath,imageUrl,context,callBack);
			imageView.setTag(imagePath);
		}else {
			setImageViewUtils( imageView,null,imageUrl,context,callBack);
			imageView.setTag(imageUrl);
		}
	}
	
	/**
	 * set image to view
	 * @param imageView
	 * @param imagePath
	 * @param imageUrl
	 * @param context
	 * @param callBack
	 */
	private static void setImageViewUtils(ImageView imageView, String imagePath,
			String imageUrl, Context context, ImageCallBack callBack) {
		Bitmap bitMap = null;
		if(imagePath == null){
			bitMap = loadBitMapFromSoftOrNet(imageUrl,context,callBack);
		}else{
			bitMap = loadBitMapFromLocalOrNet(imagePath,imageUrl,context,callBack);
		}
		if(bitMap == null){
			imageView.setImageResource(Dafult_image);
		}else{
			imageView.setImageBitmap(bitMap);
		}
	}


	/**
	 * if sdcard isn't available 
	 * @param imageView
	 * @param imageUrl
	 * @param context
	 * @param callBack
	 * @return
	 */
	private static Bitmap loadBitMapFromSoftOrNet(final String imageUrl, Context context, final ImageCallBack callBack) {
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Bitmap> ref = imageCache.get(imageUrl);
			Bitmap bitmap = ref.get();
			if(bitmap!=null){
				return bitmap;
			}
		}
		final Handler handler  = new Handler(){
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					Bitmap bitmap = (Bitmap) msg.obj;
					callBack.loadImage(bitmap,imageUrl);
				}
			}
		};
		Runnable runnable = new Runnable() {
			public void run(){
				try {
					URL url = new URL(imageUrl);
					Log.v(LOG_TAG, imageUrl);
					HttpURLConnection conn  = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(4000);
					conn.setReadTimeout(5000);
					conn.connect();
					InputStream in = conn.getInputStream();
					BitmapFactory.Options options = new Options();
					options.inSampleSize =1;
					options.inTempStorage = new byte[16 * 1024];
					Bitmap bitmap = BitmapFactory.decodeStream(in,new Rect(0, 0, 0, 0),options);
					imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
					Message msg = handler.obtainMessage();
					msg.obj = bitmap;
					
					in.close();//2013、8、9 fay
					conn.disconnect();
					
					handler.sendMessage(msg);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}catch(OutOfMemoryError e){
					e.printStackTrace();
				}
			}
		};
		ThreadPoolManager.getInstance().addTask(runnable);
		return null;
	}

	/**
	 * if sdcard is available
	 * @param imageView
	 * @param imagePath
	 * @param imageUrl
	 * @param context
	 * @param callBack
	 * @return
	 */
	private static Bitmap loadBitMapFromLocalOrNet(final String imagePath, final String imageUrl, Context context,
			final ImageCallBack callBack) {
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Bitmap> ref = imageCache.get(imageUrl);
			Bitmap bitmap = ref.get();
			if(bitmap!=null){
				return bitmap;
			}
		}
		
		Bitmap bitMap;
		bitMap = getImageFromSDcard(imagePath);
		
		if(bitMap !=null){
			return bitMap;
		}else {
			final Handler handler  = new Handler(){
				public void handleMessage(Message msg) {
					if (msg.obj != null) {
						Bitmap bitmap = (Bitmap) msg.obj;
						callBack.loadImage(bitmap,imagePath);
					}
				}
			};
			Runnable runnable = new Runnable() {
				public void run(){
					try {
						Log.v(LOG_TAG, "runnable");
						URL url = new URL(imageUrl);
						Log.v(LOG_TAG, imageUrl);
						HttpURLConnection conn  = (HttpURLConnection) url.openConnection();
						conn.setConnectTimeout(4000);
						conn.setReadTimeout(5000);
						conn.connect();
						InputStream in = conn.getInputStream();
						BitmapFactory.Options options = new Options();
						options.inSampleSize =1;
						Bitmap bitmap = BitmapFactory.decodeStream(in,new Rect(0, 0, 0, 0),options);
						imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
						Log.v(LOG_TAG, "runnable end");
						Message msg = handler.obtainMessage();
						msg.obj = bitmap;
						handler.sendMessage(msg);
						if(bitmap!=null){
							putImageToSDcar(bitmap,imagePath);
						}
						
						in.close();//fay
						conn.disconnect();
						
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}catch(OutOfMemoryError e){
						e.printStackTrace();
					}
				
				}
			};
			ThreadPoolManager.getInstance().addTask(runnable);
		}
		return null;
	}

	/**
	 * save bitmap to sd
	 * @param bitMap
	 * @param imagePath
	 */
	public static void putImageToSDcar(Bitmap bitMap, String imagePath) {
		if(bitMap == null && imagePath ==null || imagePath.equals("")){
			return ;
		}
		File file = new File(imagePath);
		
		if(file.exists()){
			return ;
		}else {
			try {
				File parentFile = file.getParentFile();
				if(!parentFile.exists()){
					parentFile.mkdirs();
				}
				file.createNewFile();
				FileOutputStream os = new FileOutputStream(file);
				bitMap.compress(Bitmap.CompressFormat.PNG, 100, os);
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}catch(OutOfMemoryError e){
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * get bitmap from sd
	 * @param imagePath
	 * @return
	 */
	public static Bitmap getImageFromSDcard(String imagePath) {
		File file = new File(imagePath);
		if(file.exists()){
			Bitmap bitmap=null;
			try{
			    bitmap = BitmapFactory.decodeFile(imagePath);
			    file.setLastModified(System.currentTimeMillis());
			}catch(OutOfMemoryError e){
				e.printStackTrace();
			}
			return bitmap;
		}
		return null;
	}

	/**
	 * @return the cache dir from sd
	 */
	@TargetApi(Build.VERSION_CODES.FROYO)
	@SuppressLint("NewApi")
	public static String getExternalCacheDir(Context context,String filename){
		if(hasExternalCacheDir()){
			return context.getExternalCacheDir().getPath()+File.separator+filename;
		}
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/"+filename;
		return Environment.getExternalStorageDirectory().getPath() + cacheDir;
	}
	

	public static boolean hasExternalCacheDir() {
		return Build.VERSION.SDK_INT>Build.VERSION_CODES.FROYO;
	}
	/**
	 * public interface ,when set image to view
	 */
	public interface ImageCallBack{
		public void loadImage(Bitmap bitMap,String imageTag);
	}
	
	
	/**
	 * clear sdcache
	 * @param cacheFile
	 */
	public static void clearCache(File cacheFile){
		File [] fils = cacheFile.listFiles();
		if(fils ==null){
			return ;
		}
		for(File f:fils){
			if(f.isFile()){
				if(System.currentTimeMillis() -f.lastModified() >CLEAR_TIME){
					f.delete();
					Log.v(LOG_TAG, "clear");
				}
			}else {
				clearCache(f);
			}
		}
	}
	
	/**
	 * clear sdcache right noew
	 * @param cacheFile
	 */
	public static void clearCacheNow(File cacheFile){
		File [] fils = cacheFile.listFiles();
		if(fils ==null){
			return ;
		}
		for(File f:fils){
			if(f.isFile()){
					f.delete();
			}else {
				clearCache(f);
			}
		}
		
	}
	
	
	
}
