create table student (stuQQ varchar(11) primary key, stuName varchar(20) not null, stuPro varchar(50) not null,
	 stuCity varchar(15) not null, stuPassword varchar(15) not null, stuSex varchar(2) null, stuSigns varchar(60) null,
	 stuPhone varchar(11) null );
select * from student;
insert into student values('946818013', 'Tracy', '河北', '保定', '123456', '男', '这个人很懒，什么都没有留下~', '18271635641');
select stuQQ from student;

select * from student where stuQQ = '65' and stuPassword = '123';
select * from student where stuQQ = '65';
update student set stuPhone = '18271635641' where stuQQ = '946818013';

drop table student;

create table post (postId int auto_increment primary key, title varchar(100) not null, content varchar(600), authorId varchar(11),
	time timestamp not null, replyNum int not null,
	foreign key(authorId) references student(stuQQ)
drop view post_pro;

create view post_all as select * from post;

create view post_beijing as select * from post where authorId in (select stuQQ from student where stuPro like '北京');
create view post_tianjin as select * from post where authorId in (select stuQQ from student where stuPro like '天津');
create view post_shanghai as select * from post where authorId in (select stuQQ from student where stuPro like '上海');
create view post_chongqing as select * from post where authorId in (select stuQQ from student where stuPro like '重庆');
create view post_hebei as select * from post where authorId in (select stuQQ from student where stuPro like '河北');
create view post_shanxi as select * from post where authorId in (select stuQQ from student where stuPro like '山西');
create view post_taiwan as select * from post where authorId in (select stuQQ from student where stuPro like '台湾');
create view post_liaoning as select * from post where authorId in (select stuQQ from student where stuPro like '辽宁');
create view post_jilin as select * from post where authorId in (select stuQQ from student where stuPro like '吉林');
create view post_heilongjiang as select * from post where authorId in (select stuQQ from student where stuPro like '黑龙江');
create view post_jiangsu as select * from post where authorId in (select stuQQ from student where stuPro like '江苏');
create view post_zhejiang as select * from post where authorId in (select stuQQ from student where stuPro like '浙江');
create view post_anhui as select * from post where authorId in (select stuQQ from student where stuPro like '安徽');
create view post_fujian as select * from post where authorId in (select stuQQ from student where stuPro like '福建');
create view post_jiangxi as select * from post where authorId in (select stuQQ from student where stuPro like '江西');
create view post_shandong as select * from post where authorId in (select stuQQ from student where stuPro like '山东');
create view post_henan as select * from post where authorId in (select stuQQ from student where stuPro like '河南');
create view post_hubei as select * from post where authorId in (select stuQQ from student where stuPro like '湖北');
create view post_hunan as select * from post where authorId in (select stuQQ from student where stuPro like '湖南');
create view post_guangdong as select * from post where authorId in (select stuQQ from student where stuPro like '广东');
create view post_gansu as select * from post where authorId in (select stuQQ from student where stuPro like '甘肃');
create view post_sichuan as select * from post where authorId in (select stuQQ from student where stuPro like '四川');
create view post_guizhou as select * from post where authorId in (select stuQQ from student where stuPro like '贵州');
create view post_hainan as select * from post where authorId in (select stuQQ from student where stuPro like '海南');
create view post_yunnan as select * from post where authorId in (select stuQQ from student where stuPro like '云南');
create view post_qinghai as select * from post where authorId in (select stuQQ from student where stuPro like '青海');
create view post_shanxi2 as select * from post where authorId in (select stuQQ from student where stuPro like '陕西');
create view post_guangxi as select * from post where authorId in (select stuQQ from student where stuPro like '广西');
create view post_xizang as select * from post where authorId in (select stuQQ from student where stuPro like '西藏');
create view post_ningxia as select * from post where authorId in (select stuQQ from student where stuPro like '宁夏');
create view post_xinjiang as select * from post where authorId in (select stuQQ from student where stuPro like '新疆');
create view post_neimenggu as select * from post where authorId in (select stuQQ from student where stuPro like '内蒙古');
create view post_aomen as select * from post where authorId in (select stuQQ from student where stuPro like '澳门');
create view post_xianggang as select * from post where authorId in (select stuQQ from student where stuPro like '香港');

select * from post order by postId desc;
select * from post where authorId in (select stuQQ from student where stuPro like '河北');
select * from post_all order by postId desc;

create table reply (replyId int auto_increment primary key, details varchar(300) not null,
	time timestamp not null, fromId varchar(11) not null, toId varchar(11) not null, postId int not null, 
	foreign key(fromId) references student(stuQQ),
	foreign key(toId) references student(stuQQ),
	foreign key(postId) references post(postId));
	
select * from reply;
--select top 5 * from reply where toPostId = '127' and replyId not in(select top 5 replyId from reply where toPostId = '127');
select COUNT(postId) from post;
delete from post;
delete from post where postId >= 142 and postId <= 149;
insert into post (title, content, authorId, time, replyNum, imageName) values('十一旅游季，求组团！！！', '有木有志同道合的老乡啊,吱个声啊', '946818013', '2013-9-27 16:22:34', 0, "");
insert into reply (details, time, fromId, toId, postId) values('我去，到时候叫我一声儿', '2013-9-27 16:45', '190486790', '946818013', 1);
update post set replyNum = replyNum + 1 where postId = 1;

--分页查询语句
--select top 20 * from post_pro where postId not in(select top 0 postId from post_pro order by postId desc) order by postId desc;
--select top 20 * from post order by postId desc;
--select top 20 * from post where postId not in(select top 23 postId from post order by postId desc) order by postId desc;

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

select * from student where stuHometown = '辽宁省 鞍山市';

create table message (msgId int auto_increment primary key, msgType int, msgSender varchar(11), msgReceiver varchar(11), msgDetails varchar(60), msgTime timestamp);
select * from message;
delete from message;
select * from message where msgReceiver = '765';
insert into message values (1, '65', '765', '你好啊', '2013-10-08 14:34:12');
insert into message values (1, '65', '765', '啦啦啦啦', '2013-10-08 14:35:12');
insert into message values (1, '3424', '765', '吼吼哦吼哦发发发额发额发额发额发额发额发额发', '2013-10-08 14:56:12');

select msgReceiver from message group by msgReceiver;

select * from message where (msgSender='65' and msgReceiver='3424') or (msgSender='3424' and msgReceiver='65') order by msgId desc;

create table requestAddMsg (msgId int auto_increment primary key, msgType int, msgSender varchar(11), msgReceiver varchar(11), msgDetails varchar(60), msgTime timestamp);
select * from requestAddMsg;
delete from requestAddMsg;

create table unfriendMsg (msgId int auto_increment primary key, msgType int, msgSender varchar(11), msgReceiver varchar(11), msgDetails varchar(60), msgTime timestamp);
select * from unfriendMsg;
delete from unfriendMsg;