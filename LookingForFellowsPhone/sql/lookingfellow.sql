
create table student (stuQQ varchar(11) primary key, stuName varchar(20) not null, stuPro varchar(50) not null,
	 stuCity varchar(15) not null, stuPassword varchar(15) not null, stuSex varchar(2) null, stuSigns varchar(60) null,
	 stuPhone varchar(11) null );
select * from student;
insert into student values('3424', 'syunfei', 'beijij', '123456');
select stuQQ from student;
delete from student;
select * from student where stuQQ = '65' and stuPassword = '123';
select * from student where stuQQ = '65';
update student set stuPhone = '18271633177' where stuQQ = '65';
select * from student where stuHometown like '����ʡ%' and stuQQ != 65;
drop table student;

drop view post_pro;

create view post_all as select * from post;
create view post_beijing as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_tianjin as select * from post where authorId in (select stuQQ from student where stuPro like '���');
create view post_shanghai as select * from post where authorId in (select stuQQ from student where stuPro like '�Ϻ�');
create view post_chongqing as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_hebei as select * from post where authorId in (select stuQQ from student where stuPro like '�ӱ�');
create view post_shanxi as select * from post where authorId in (select stuQQ from student where stuPro like 'ɽ��');
create view post_taiwan as select * from post where authorId in (select stuQQ from student where stuPro like '̨��');
create view post_liaoning as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_jilin as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_heilongjiang as select * from post where authorId in (select stuQQ from student where stuPro like '������');
create view post_jiangsu as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_zhejiang as select * from post where authorId in (select stuQQ from student where stuPro like '�㽭');
create view post_anhui as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_fujian as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_jiangxi as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_shandong as select * from post where authorId in (select stuQQ from student where stuPro like 'ɽ��');
create view post_henan as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_hubei as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_hunan as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_guangdong as select * from post where authorId in (select stuQQ from student where stuPro like '�㶫');
create view post_gansu as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_sichuan as select * from post where authorId in (select stuQQ from student where stuPro like '�Ĵ�');
create view post_guizhou as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_hainan as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_yunnan as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_qinghai as select * from post where authorId in (select stuQQ from student where stuPro like '�ຣ');
create view post_shanxi as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_guangxi as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_xizang as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_ningxia as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_xinjiang as select * from post where authorId in (select stuQQ from student where stuPro like '�½�');
create view post_neimenggu as select * from post where authorId in (select stuQQ from student where stuPro like '���ɹ�');
create view post_aomen as select * from post where authorId in (select stuQQ from student where stuPro like '����');
create view post_xianggang as select * from post where authorId in (select stuQQ from student where stuPro like '���');

select * from post order by postId desc;
select * from post where authorId in (select stuQQ from student where stuPro like '�ӱ�');
select * from post_all order by postId desc;

create table reply (replyId int identity(1,1) not null, details varchar(300) not null,
	time smalldatetime not null, fromId varchar(11) not null, toId varchar(11) not null, postId int not null, 
	foreign key(fromId) references student(stuQQ),
	foreign key(toId) references student(stuQQ),
	foreign key(postId) references post(postId));
	
select * from reply;
select top 5 * from reply where toPostId = '127' and replyId not in(select top 5 replyId from reply where toPostId = '127');
select COUNT(postId) from post;
delete from post;
delete from post where postId >= 142 and postId <= 149;
insert into post values('ʮһ���μ��������ţ�����', '��ľ��־ͬ���ϵ����簡,֨������', '2013-9-27 16:22:34', '65', 0);
	insert into reply values('��ȥ����ʱ�����һ����', '2013-9-27 16:45', '765', 127);
	update post set replyNum = replyNum + 1 where postId = 127;
	insert into reply values('��Ҳȥ����ʱ��Ҳ����һ����', '2013-9-27 16:46', '3424', 127);
	update post set replyNum = replyNum + 1 where postId = 127;
	insert into reply values('��֪��', '2013-9-27 16:46', '3424', 127);
	update post set replyNum = replyNum + 1 where postId = 127;
	insert into reply values('ͬ��֪��', '2013-9-27 16:46', '3424', 127);
	update post set replyNum = replyNum + 1 where postId = 127;
	insert into reply values('¥������', '2013-9-27 16:46', '3424', 127);
	update post set replyNum = replyNum + 1 where postId = 127;
	insert into reply values('����¥����ظ�', '2013-9-27 16:46', '3424', 127);
	update post set replyNum = replyNum + 1 where postId = 127;
	insert into reply values('������ظ�', '2013-9-27 16:46', '3424', 127);
	update post set replyNum = replyNum + 1 where postId = 127;

drop table reply;

delete from post where postId = 16;
delete from reply where replyId > 8 and replyId < 11;
insert into post values('�ż��ˣ���˭��У��������', '��������������', '2013-9-27 14:22:34', '65', 0);
	insert into reply values('��֪����', '2013-9-27 18:45', '765', 7);
	update post set replyNum = replyNum + 1 where postId = 7;
	insert into reply values('����û�а�', '2013-9-27 18:56', '3424', 7);
	update post set replyNum = replyNum + 1 where postId = 7;

insert into post values('�����������������', '�������磬���Ź㳡���ǵ�׼ʱ����', '2013-9-27 14:22:34', '3424', 0);
	insert into reply values('��������������', '2013-9-27 18:45', '765', 17);
	update post set replyNum = replyNum + 1 where postId = 17;
	insert into reply values('�����õ�', '2013-9-27 18:56', '65', 17);
	update post set replyNum = replyNum + 1 where postId = 17;
//��ҳ��ѯ���
select top 20 * from post_pro where postId not in(select top 0 postId from post_pro order by postId desc) order by postId desc;
select top 20 * from post order by postId desc;
select top 20 * from post where postId not in(select top 23 postId from post order by postId desc) order by postId desc;

create table friend (user_qq varchar(11), friend_qq varchar(11), constraint relationkey primary key ( user_qq, friend_qq));
select * from friend;
delete from friend;
drop table friend;
insert into friend values ('65', '765');
insert into friend values ('765', '65'); 
insert into friend values ('65', '3424');
insert into friend values ('3424', '65'); 
insert into friend values ('765', '1234567'); 
insert into friend values ('1234567', '765'); 

select friend_qq from friend where user_qq = '65';
select * from student where stuQQ = '3424';

select * from student where stuHometown = '����ʡ ��ɽ��';

create table message (msgId int identity(1,1), msgType int, msgSender varchar(11), msgReceiver varchar(11), msgDetails varchar(60), msgTime smalldatetime);
select * from message;
delete from message;
select * from message where msgReceiver = '765';
insert into message values (1, '65', '765', '��ð�', '2013-10-08 14:34:12');
insert into message values (1, '65', '765', '��������', '2013-10-08 14:35:12');
insert into message values (1, '3424', '765', '���Ŷ��Ŷ�������������', '2013-10-08 14:56:12');

select msgReceiver from message group by msgReceiver;

select * from message where (msgSender='65' and msgReceiver='3424') or (msgSender='3424' and msgReceiver='65') order by msgId desc;

create table requestAddMsg (msgId int identity(1,1), msgType int, msgSender varchar(11), msgReceiver varchar(11), msgDetails varchar(60), msgTime smalldatetime);
select * from requestAddMsg;
delete from requestAddMsg;

create table unfriendMsg (msgId int identity(1,1), msgType int, msgSender varchar(11), msgReceiver varchar(11), msgDetails varchar(60), msgTime smalldatetime);
select * from unfriendMsg;
delete from unfriendMsg;