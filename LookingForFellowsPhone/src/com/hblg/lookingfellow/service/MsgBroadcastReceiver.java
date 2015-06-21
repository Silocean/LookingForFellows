package com.hblg.lookingfellow.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hblg.lookingfellow.entity.Message;
import com.hblg.lookingfellow.entity.MessageType;
import com.hblg.lookingfellow.slidingmenu.activity.ChatActivity;
import com.hblg.lookingfellow.tools.ShowNotification;

public class MsgBroadcastReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Message msg = (Message) intent.getSerializableExtra("msg");
		if(msg.getType() == MessageType.MSG_CHAT) {// ����յ�����������Ϣ
			String sender = msg.getSender();
			if(ChatActivity.active) { // ������촰������ǰ��
				if(ChatActivity.friendQq.equals(sender)) { // ������ڸ�����Ϣ������,ֱ�Ӹ���������漴��
					ChatActivity.updateChatView(context, msg);
				} else { // ���û�и�����Ϣ�����죬��֪ͨ����ʾ֪ͨ
					ShowNotification.showNotification(context, msg);
				}
			} else { // ������촰�ڲ�����ǰ��
				ShowNotification.showNotification(context, msg);
			}
		} else if(msg.getType() == MessageType.MSG_REQUESTADDFRIEND) { // ����յ���������Ӻ��ѵ���Ϣ
			
		}
	}

}
