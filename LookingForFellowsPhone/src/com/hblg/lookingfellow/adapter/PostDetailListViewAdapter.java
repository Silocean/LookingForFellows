package com.hblg.lookingfellow.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hblg.lookingfellow.R;

public class PostDetailListViewAdapter extends BaseAdapter{
	private ListView listview;
	Context context;
	List<Map<String, Object>>list;
	Map<String,Object> daoMap=null;
	Holder holder=null;
	LayoutInflater inflater;
	LinearLayout linearLayout;
	public PostDetailListViewAdapter(Context context,ListView listview){
		this.context=context;
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listview=listview;
		
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		holder=new Holder();
		daoMap=list.get(position);
		if(null==convertView){
			convertView=inflater.inflate(R.layout.listitem_postsdetail, null);
			linearLayout=(LinearLayout)convertView.findViewById(R.id.posts_list_layout);
			
			holder.headImg=(ImageView)convertView.findViewById(R.id.posts_list_headImg);
			holder.contentImg=(ImageView)convertView.findViewById(R.id.posts_list_contentImg);
			holder.nameTxt=(TextView)convertView.findViewById(R.id.posts_list_nameTxt);
			holder.timeTxt=(TextView)convertView.findViewById(R.id.posts_list_time);
			holder.floorTxt=(TextView)convertView.findViewById(R.id.posts_list_floor);
			holder.contentTxt=(TextView)convertView.findViewById(R.id.posts_list_content);
			holder.oneNameTxt=(TextView)convertView.findViewById(R.id.one_name);
			holder.oneTxt=(TextView)convertView.findViewById(R.id.one_txt);
			holder.twoNameTxt=(TextView)convertView.findViewById(R.id.two_name);
			holder.twoTxt=(TextView)convertView.findViewById(R.id.two_txt);
			holder.expandTxt=(TextView)convertView.findViewById(R.id.expand_comments);
			holder.commentBtn=(Button)convertView.findViewById(R.id.posts_list_commentBtn);

			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		
		//¥
		if(0==position){
			holder.floorTxt.setText("¥��");
		}else{
			holder.floorTxt.setText(position+1+"¥");
		}
		
		//�鿴ʣ������
		holder.expandTxt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//��ʱ����
				Toast.makeText(context,"�鿴"+(position+1)+"¥ʣ������", 2000).show();
			}
		});
		
		
		//����
		holder.commentBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//��ʱ����
				Toast.makeText(context,""+(position+1), 2000).show();
			}
		});
		
		return convertView;
	}
	
	//�Ż�
	private class Holder{
		ImageView headImg;//����ͷ��
		ImageView contentImg;//������Ƭ
		TextView nameTxt;//�����ǳ�
		TextView  timeTxt;//ʱ��
		TextView floorTxt;//����¥��1¥��2¥��
		TextView contentTxt;//��������
		TextView oneNameTxt;//��һ���������ǳ�
		TextView oneTxt;//��һ����������������
		TextView twoNameTxt;//�ڶ����������ǳ�
		TextView twoTxt;//�ڶ�������������
		Button commentBtn;//����
		TextView expandTxt;//չ������
		
	}
	

}
