package com.hblg.lookingfellow.slidingmenu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.User;
/**
 * 二维码
 */
public class QRCodeActivity extends Activity{
	private String content="";
	private Bitmap bitmap;
	private ImageView qrCodeImg;
	private Button scanBtn;
	private Button backBtn;
	 /**
     * 用于跳转到开始扫描用来回传值的resultCode
     */
    private static final int TO_SCAN = 99;
    /**
     * 将要生成二维码的内容
     */
    private TextView codeText;
    
    private MyListener myListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcode);
		myListener=new MyListener();
		initView();
		content=User.qq;
		try {
			bitmap=createTwoDCode(content);
			if(null!=bitmap){
				qrCodeImg.setImageBitmap(bitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void initView(){
		qrCodeImg=(ImageView)findViewById(R.id.qrcode_img);
		backBtn=(Button)findViewById(R.id.qrcode_goback_button);
		scanBtn=(Button)findViewById(R.id.qrcode_scan);
		codeText=(TextView)findViewById(R.id.qrcode_txt);
		scanBtn.setOnClickListener(myListener);
		//返回
		backBtn.setOnClickListener(myListener);
	}
	private class MyListener implements OnClickListener{
		
		public void onClick(View view){
			switch(view.getId()){
			case R.id.qrcode_goback_button:
				finish();
				break;
			case R.id.qrcode_scan:
			    Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                startActivityForResult(intent, TO_SCAN);
				break;
			default:
				break;
			}
		}
	}
	
	
	 /**
     * 将指定的内容生成成二维码
     *
     * @param content 将要生成二维码的内容
     * @return 返回生成好的二维码事件
     * @throws WriterException WriterException异常
     */
    public Bitmap createTwoDCode(String content) throws Exception{
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, 400, 400);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    /**
     * 界面回传的事件
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TO_SCAN:
                if (resultCode == RESULT_OK) {
                    String scanStr = data.getStringExtra("RESULT");
                    //setCode(codeEdit.setText(scanStr).);
                    codeText.setText(scanStr);
                    //在这里开启一个线程进行加好友
                } else if (resultCode == RESULT_CANCELED) {
                	Log.v("result","resultCode-->"+resultCode);
                    Toast.makeText(getApplicationContext(), "扫描取消",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "扫描异常",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
