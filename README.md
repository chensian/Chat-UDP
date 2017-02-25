### 基于UDP协议实现的Java聊天室

转载请注明出处，谢谢！（原文链接：https://chensian.github.io/2016/05/24/chat-udp/ ）

Github ：https://github.com/chensian/Chat-UDP

#### 项目环境

eclipse EE

JDK1.8 + Mysql

#### 摘要

本聊天室系统是采用C/S架构设计的JAVA语言编写的聊天系统。该聊天系统具有完整的会话功能，服务器具有侦听服务，转发聊天信息，响应用户下线，发送系统消息的功能。客户端具有登陆，断开连接，发送聊天信息，接收聊天信息的功能。该聊天室由服务端和客户端组成，主要用到Socket通信的网络应用。  

#### 模块分解

![](http://ww1.sinaimg.cn/large/005LZfaWgy1fd2kzfjtruj30cy06uaa3)

#### 功能需求分析

##### 聊天室客户端需求分析

（1）基本功能即可以即时通讯

（2）可以查询在线用户基本信息

（3）可以进行群聊

（4）可以进行私聊

（5）可以显示在线用户列表

（6）可以离线接收信息

##### 聊天室服务器端需求分析
 
（1）可以显示在线用户基本信

（2）可以显示用户发送的聊天信息的基本情况（信息内容）

#### 详细设计

##### 服务器端介绍

1)newsPush(String, DatagramPacket)

说明：登录时，离线消息推送。

2)sendAll(Message, DatagramPacket)

说明：群发消息

3)sendONE(Message, DatagramPacket)

说明：私聊私发消息

4)register(User, DatagramPacket)

说明：注册事务处理

5)login(User, DatagramPacket)

说明：登录事务处理

##### 客户端介绍

1)登录失败：用户名或者密码错误，返回200

2)登录成功：服务器返回211		

3)重复登录，服务器对此ID的原登录客户端返回201

4)登录时，对密码传送进行加密(采用md5计算摘要法)，发送换算好后的password传输至服务器。

5)类型与长度：

用户名：固定10位的数字（注册人的学号）组成。

密码：6~8位，仅接受由数字或字母组成的密码。

```
（用户名和密码格式设置错误了，服务器将会返回100）
（用户名重复，服务器将会返回101）
（注册成功，服务器返回111）
```

##### 聊天室

1)clearMessage()

 说明：清空聊天窗口记录
 
2)exit()

 说明：退出聊天室
 
3)sendMessage()

 说明：发送消息，可以选择群聊或私聊
 
4)changeUser()

 说明：切换聊天好友。
 
##### 工具类

###### java对象与字节数组相互转换

```
public static byte[] ObjectToByte(java.lang.Object obj) {
		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();
			bo.close();
			oo.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return bytes;
	}

	public static Object ByteToObject(DatagramPacket inPacket) {
		byte[] bytes = inPacket.getData();
		Object obj = null;
		try {
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);
			obj = oi.readObject();
			bi.close();
			oi.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}
```
##### 结果状态码

![](http://ww1.sinaimg.cn/large/005LZfaWgy1fd2ler10h3j30f50anjro)


##### 运行界面

###### 注册界面

![](http://ww1.sinaimg.cn/large/005LZfaWgy1fd2lg5mjf9j309g078aai)

###### 注册失败

![](http://ww1.sinaimg.cn/large/005LZfaWgy1fd2ljkpzwdj309d0790t9)

###### 聊天室界面

![](http://ww1.sinaimg.cn/large/005LZfaWgy1fd2ljtv062j30fe0bbdgn)

###### 群发消息

![](http://ww1.sinaimg.cn/large/005LZfaWgy1fd2lk0161kj30ec0od76l)

###### 私聊功能

![](http://ww1.sinaimg.cn/large/005LZfaWgy1fd2lk6ztp7j30fd0ogn0d)

###### 离线接受功能

![](http://ww1.sinaimg.cn/large/005LZfaWgy1fd2lkd1g04j30iu0bv75k)
