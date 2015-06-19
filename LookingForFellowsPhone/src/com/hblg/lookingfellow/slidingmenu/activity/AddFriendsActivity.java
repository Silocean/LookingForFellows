package com.hblg.lookingfellow.slidingmenu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

import com.hblg.lookingfellow.R;
import com.hblg.lookingfellow.adapter.SearchfriendsListViewAdapter;
import com.hblg.lookingfellow.tools.ImageTool;

public class AddFriendsActivity extends Activity {
	Button gobackButton;
	ListView listView;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private SearchfriendsListViewAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addfriends);
		listView = (ListView)this.findViewById(R.id.searchFriendList);
		this.initList();
		gobackButton = (Button)this.findViewById(R.id.addfriends_goback_button);
		//listView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		gobackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	public void initList() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.head_default);
		for(int i=0; i<27; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("headimage", bitmap);
			map.put("name", "linxiaonan");
			map.put("hometown", "∫”±±  ‰√÷›");
			list.add(map);
		}
		adapter = new SearchfriendsListViewAdapter(getApplicationContext(), list, R.layout.listitem_searchfriendslayout);
		
		listView.setAdapter(adapter);
	}
	
}
