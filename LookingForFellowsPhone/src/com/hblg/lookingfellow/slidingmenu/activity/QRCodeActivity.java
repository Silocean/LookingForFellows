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
 * ��ά��
 */
public class QRCodeActivity extends Activity{
	private String content="";
	private Bitmap bitmap;
	private ImageView qrCodeImg;
	private Button scanBtn;
	private Button backBtn;
	 /**
     * ������ת����ʼɨ�������ش�ֵ��resultCode
     */
    private static final int TO_SCAN = 99;
    /**
     * ��Ҫ���ɶ�ά�������
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
		//����
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
     * ��ָ�����������ɳɶ�ά��
     *
     * @param content ��Ҫ���ɶ�ά�������
     * @return �������ɺõĶ�ά���¼�
     * @throws WriterException WriterException�쳣
     */
    public Bitmap createTwoDCode(String content) throws Exception{
        // ���ɶ�ά����,����ʱָ����С,��Ҫ������ͼƬ�Ժ��ٽ�������,������ģ������ʶ��ʧ��
        BitMatrix matrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, 400, 400);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // ��ά����תΪһά��������,Ҳ����һֱ��������
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
        // ͨ��������������bitmap,����ο�api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    /**
     * ����ش����¼�
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
                    //�����￪��һ���߳̽��мӺ���
                } else if (resultCode == RESULT_CANCELED) {
                	Log.v("result","resultCode-->"+resultCode);
                    Toast.makeText(getApplicationContext(), "ɨ��ȡ��",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "ɨ���쳣",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
