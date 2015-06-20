package com.hblg.lookingfellow.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.slidingmenu.activity.ChatActivity;
import com.hblg.lookingfellow.tools.ShowNotification;

public class MsgBroadcastReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Message msg = (Message) intent.getSerializableExtra("msg");
		String sender = msg.getSender();
		if(ChatActivity.active) { // 如果聊天窗口在最前端，证明用户正在聊天，直接更新聊天界面即可
			if(ChatActivity.friendQq.equals(sender)) {// 如果正在跟发消息者聊天,直接更新聊天界面即可
				ChatActivity.updateChatView(context, msg);
			} else { // 如果没有跟发消息者聊天，则通知栏显示通知
				ShowNotification.showNotification(context, msg);
			}
		} else {
			ShowNotification.showNotification(context, msg);
		}
	}

}
