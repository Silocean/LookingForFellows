package com.hblg.lookingfellow.entity;

public interface MessageType {
	int MSG_CHAT = 1; // ������Ϣ
	int MSG_REQUESTADDFRIEND = 2; // ����Ӻ�����Ϣ
	int MSG_AGREEADDFRIEND = 3; // ͬ����Ӻ�������
	int MSG_REQUESTRETURNCHATMSG = 4; // ���󷵻ط������ݴ��������Ϣ
	int MSG_REQUESTRETURNADDFRIENDMSG = 5; // ���󷵻ط������ݴ��������Ӻ�����Ϣ
	int MSG_UNFRINEDMSG = 6; // ������ѹ�ϵ
	int MSG_EXIT = 7; // �û�������Ϣ
}
