package com.hblg.lookingfellow.adapter;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.entity.Common;
import com.hblg.lookingfellow.entity.User;
import com.hblg.lookingfellow.slidingmenu.activity.FriendInfoActivity;
import com.hblg.lookingfellow.slidingmenu.activity.PostDetailActivity;
import com.hblg.lookingfellow.sqlite.SQLiteService;
import com.hblg.lookingfellow.tools.ImageTool;
import com.hblg.lookingfellow.tools.TimeConvertTool;

public class PostDetailListViewAdapter extends BaseAdapter{
	@SuppressWarnings("unused")
	private ListView listview;
	Context context;
	List<Map<String, Object>>list;
	PostDetailActivity postDetailActivity;
	
	Map<String,Object> map = null;
	Holder holder=null;
	LayoutInflater inflater;
	LinearLayout linearLayout;
	Bitmap bitmap;
	public PostDetailListViewAdapter(Context context, ListView listview, PostDetailActivity postDetailActivity){
		this.context=context;
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listview=listview;
		this.postDetailActivity = postDetailActivity;
		
	}
	public void setData(ArrayList<Map<String, Object>>list){
		this.list=list;
		notifyDataSetChanged();
	}
	public void clearData(){
		list.clear();
		notifyDataSetChanged();
	}
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		holder=new Holder();
		map = list.get(position);
		if(null==convertView){
			convertView=inflater.inflate(R.layout.listitem_postsdetail, null);
			
			holder.headImg=(ImageView)convertView.findViewById(R.id.posts_list_headImg);
			holder.nameTxt=(TextView)convertView.findViewById(R.id.posts_list_nameTxt);
			holder.timeTxt=(TextView)convertView.findViewById(R.id.posts_list_time);
			holder.replyToWhoTxt=(TextView)convertView.findViewById(R.id.posts_list_replyToWho);
			holder.contentTxt=(TextView)convertView.findViewById(R.id.posts_list_content);
			
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		
		// ͷ��
		final String fromId = (String)map.get("fromId");
		bitmap = ImageTool.getHeadImageFromLocalOrNet(context, fromId);
		//bitmap = ImageTool.toRoundCorner(bitmap, 15);
		holder.headImg.setImageBitmap(bitmap);
		if(!fromId.equals(User.qq)) {
			holder.headImg.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(context, FriendInfoActivity.class);
					// ��ֹ Calling startActivity() from outside of an Activity���ⷢ��
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("qq", fromId);
					SQLiteService service = new SQLiteService(context);
					boolean flag = service.checkIsFriend(fromId);
					if(flag) {
						intent.putExtra("tag", "unfriendRequest");
					} else {
						intent.putExtra("tag", "addRequest");
					}
					context.startActivity(intent);
				}
			});
		}
		// toId
		final String toId = (String)map.get("toId");
		
		// ����
		final String fromName = (String)map.get("fromName");
		holder.nameTxt.setText(fromName);
		
		final String toName = (String)map.get("toName");
		
		// ʱ��
		String time = (String)map.get("time");
		time = TimeConvertTool.calDateTime(time);
		holder.timeTxt.setText(time);
		
		// �ظ�˭
		holder.replyToWhoTxt.setText("�ظ���" + toName);
		
		// ����
		String details = (String)map.get("details");
		SpannableString ss = new SpannableString(details);
		String zhengze1 = "\\[[0-9a-z]{32}\\]_\\d+.jpg";
		String zhengze2 = "f0[0-9]{2}|f10[0-7]";
		try {
			this.dealExpression(context, ss, zhengze1, 0);
			this.dealExpression(context, ss, zhengze2, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		holder.contentTxt.setText(ss);
		
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(toId!=null) {
					postDetailActivity.toId = fromId;
					postDetailActivity.toName = fromName;
					postDetailActivity.commentEdt.setHint("�ظ���" + fromName);
				}
			}
		});
		
		return convertView;
	}
	
	/**
	 * ��spanableString���������жϣ��������Ҫ��������ӦͼƬ����
	 */
    public void dealExpression(Context context, SpannableString spannableString, String zhengze, int start) throws Exception {
    	if(zhengze.equals("\\[[0-9a-z]{32}\\]_\\d+.jpg")) {// ���������ͼƬ
    		Pattern pattern = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
    		Matcher matcher = pattern.matcher(spannableString);
    		while (matcher.find()) {
    			String key = matcher.group();
    			Log.d("Key", key);
    			if (matcher.start() < start) {
    				continue;
    			}
            	String imageName = key;
            	imageName = imageName.substring(1, imageName.indexOf('_')-1) + imageName.substring(imageName.indexOf('_'), imageName.length());
            	InputStream is = new URL(Common.PATH + "post/" + imageName).openStream();
            	System.out.println("====="+key);
            	Bitmap bitmap = BitmapFactory.decodeStream(is);
            	@SuppressWarnings("deprecation")
				ImageSpan imageSpan = new ImageSpan(bitmap);				            
            	int end = matcher.start() + key.length();
            	spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);	
            	if (end < spannableString.length()) {						
            		dealExpression(context, spannableString, zhengze, end);
            	}
            	break;
    		}
    	} else if(zhengze.equals("f0[0-9]{2}|f10[0-7]")) { // ����Ǳ���ͼƬ
    		Pattern pattern = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
    		Matcher matcher = pattern.matcher(spannableString);
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
                    @SuppressWarnings("deprecation")
					ImageSpan imageSpan = new ImageSpan(bitmap);				            
                    int end = matcher.start() + key.length();					
                    spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);	
                    if (end < spannableString.length()) {						
                        dealExpression(context, spannableString, zhengze, end);
                    }
                    break;
                }
            }
    	}
    }
	
	//�Ż�
	private class Holder{
		ImageView headImg;//����ͷ��
		TextView nameTxt;//�����ǳ�
		TextView timeTxt;//ʱ��
		TextView replyToWhoTxt;//����¥��1¥��2¥��
		TextView contentTxt;//��������
	}
	

}
