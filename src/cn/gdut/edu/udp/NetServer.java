package cn.gdut.edu.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.gdut.edu.dao.MyDb;
import cn.gdut.edu.entity.Message;
import cn.gdut.edu.entity.Record;
import cn.gdut.edu.entity.StatusCode;
import cn.gdut.edu.entity.User;
import cn.gdut.edu.util.Util;

/**
 * 简单的服务器demo 相应用户的各种信息，包括注册，登陆，发送，接收，请求所有用户，在线用户，注销登陆
 * 
 * @author Huanfeng
 *
 */
public class NetServer {

	private static Map<String, SocketAddress> userList = new HashMap<String, SocketAddress>();

	DatagramSocket serverSocket;
	MulticastSocket multicastSocket;
	InetAddress group;

	class NetServerImpl implements Runnable {

		@Override
		public void run() {
			try {

				while (true) {

					DatagramPacket inPacket = Util.serverReceive(serverSocket);

					Message message = (Message) Util.ByteToObject(inPacket);

					String string = message.getType();

					System.out.println(string);

					if (string.startsWith("SENDONE")) {
						System.out.println("SENDONE");
						sendONE(message, inPacket);

					} else if (string.startsWith("SENDALL")) {
						System.out.println("SENDALL");
						sendAll(message, inPacket);

					} else if (string.startsWith("LOG")) {
						System.out.println("LOG");
						User user = (User) message.getObject();
						login(user, inPacket);
						newsPush(user.getName(), inPacket);

					} else if (string.startsWith("REG")) {
						System.out.println("REG");
						User user = (User) message.getObject();
						register(user, inPacket);

					} else if (string.startsWith("QUEO")) {
						System.out.println(userList.size());
						getOnlineUser(inPacket);

					} else if (string.startsWith("QUEA")) {
						getAllUser(inPacket);

					} else if (string.startsWith("EXIT")) {
						System.out.println("EXIT");
						offLine(message);

					} else if (string.startsWith("RETC")) {
						System.out.println("RETC");
						// ResultSet(string, serverSocket);

					} else {
						System.out.println("OtherWrong");
						OtherWrong(inPacket);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				close(serverSocket);
			}
		}

	}

	/**
	 * 其他类型的错误
	 * 
	 * @param serverSocket
	 */
	synchronized private void OtherWrong(DatagramPacket inPacket) {
		try {

			Message message = new Message("ERROR");
			Util.serverSend(serverSocket, inPacket, message);
		} catch (IOException e) {
			close(serverSocket);
		}
	}

	/**
	 * 断开与客户端的连接，并且关闭相关的流，释放资源
	 * 
	 * @param serverSocket
	 */

	/**
	 * @param serverSocket
	 */
	synchronized private static void close(DatagramSocket serverSocket) {
		if (userList.containsValue(serverSocket)) {
			userList.remove(serverSocket);
		}
		if (!serverSocket.isClosed())
			serverSocket.close();
	}

	/**
	 * 获取所有用户
	 *
	 * @param serverSocket
	 * @throws IOException
	 */
	synchronized private void getAllUser(DatagramPacket inPacket)
			throws IOException {
		MyDb myDb = new MyDb();
		Message message = new Message("ALLUSER");
		Set<String> allUserList = myDb.getAlluser();
		message.setObject(allUserList);
		try {
			Util.serverSend(serverSocket, inPacket, message);
		} catch (IOException e) {
			close(serverSocket);
		} finally {
			myDb.closeDb();
		}
	}

	/**
	 * 处理用户离线请求
	 *
	 * @param serverSocket
	 * @param string
	 */

	synchronized static private void offLine(Message message) {

		System.out.println("Offline-->" + message.getSendName());
		userList.remove(message.getSendName());
	}

	/**
	 * 获取在线用户
	 *
	 * @param serverSocket
	 */
	synchronized private void getOnlineUser(DatagramPacket inPacket) {

		Message message = new Message("ONLINEUSER");
		Set<String> onlineUserList = new HashSet<String>();

		for (String name : userList.keySet()) {
			onlineUserList.add(name);
		}

		message.setObject(onlineUserList);
		try {
			Util.serverSend(serverSocket, inPacket, message);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 初始化处理线程
	 *
	 * @param serverSocket
	 * @throws SocketException
	 * @throws UnknownHostException 
	 */
	synchronized private void init() throws SocketException, UnknownHostException {
		serverSocket = new DatagramSocket(new InetSocketAddress(Util.DEST_PORT));
		group = InetAddress.getByName("228.9.6.8");
		Thread thread = new Thread(new NetServerImpl());
		thread.start();
	}

	/**
	 * 离线消息推送
	 * 
	 * @throws IOException
	 */
	synchronized private void newsPush(String name, DatagramPacket inPacket)
			throws IOException {
		// TODO Auto-generated method stub
		MyDb myDb = new MyDb();
		// 把消息设置成 已读
		List<Record> recordlist = myDb.selectRecordByreceiveName(name);

		System.out.println("recordlist.size()" + recordlist.size());
		// 推送
		for (Record record : recordlist) {

			Message message = new Message();
			message.setObject(record.getContent());
			message.setSendName(record.getSendName());
			message.setReceiveName(record.getReceivename());
			message.setType(record.getType());
			System.out.println(message);
			Util.serverSend(serverSocket, inPacket, message);
		}

		// 把消息设置成 已读
		myDb.updateRecordByreceiveName(name);

	}

	/**
	 * 群转发消息
	 *
	 * @param string
	 * @param serverSocket
	 * @throws IOException
	 */
	synchronized private void sendAll(Message message, DatagramPacket inPacket)
			throws IOException {
		// 群聊发送

		message.setType("RETALL");

		for (String key : userList.keySet()) {

			
			// 不要发给自己 messge name 设置成自己
			if (!message.getSendName().equals(key)) {
				SocketAddress address = userList.get(key);
				inPacket.setSocketAddress(address);
				Util.serverSend(serverSocket, inPacket, message);
			}
		}

		// 离线记录 保存
		MyDb myDb = new MyDb();
		Set<String> allUserList = myDb.getAlluser();
		allUserList.removeAll(userList.keySet());

		for (String receivename : allUserList) {
			Record record = new Record();
			record.setType(message.getType());
			record.setSendName(message.getSendName());
			record.setReceivename(receivename);
			record.setContent((String) message.getObject());
			record.setIsread(false);
			// 不要发给自己 messge name 设置成自己
			myDb.saveRecord(record);
		}
		myDb.closeDb();
	}

	/**
	 * 私聊
	 *
	 * @param string
	 * @param serverSocket
	 * @throws IOException
	 */
	synchronized private void sendONE(Message message, DatagramPacket inPacket)
			throws IOException {
		// 私聊发送
		message.setType("RETONE");

		// 私聊在线
		if (userList.keySet().contains(message.getReceiveName())) {
			String name = message.getReceiveName();
			for (String str : userList.keySet()) {
				System.out.println(str);
			}

			SocketAddress address = userList.get(name);
			inPacket.setSocketAddress(address);
			Util.serverSend(serverSocket, inPacket, message);
		} else {
			MyDb myDb = new MyDb();
			Record record = new Record();
			record.setType(message.getType());
			record.setSendName(message.getSendName());
			record.setReceivename(message.getReceiveName());
			record.setContent((String) message.getObject());
			record.setIsread(false);
			// 不要发给自己 messge name 设置成自己
			myDb.saveRecord(record);
			myDb.closeDb();
		}

	}

	/**
	 * 处理用户注册请求
	 * 
	 * @param msg
	 * @param serverSocket
	 * @throws IOException
	 */
	synchronized private void register(User user, DatagramPacket inPacket)
			throws IOException {
		String name = user.getName();
		String passWord = user.getPassword();

		if (!passWord.isEmpty() && !name.isEmpty() && name.length() != 10
				&& passWord.length() >= 6 && passWord.length() <= 16) {
			Util.serverSend(serverSocket, inPacket,
					StatusCode.WRONGUSEORPASSWORDTYPE + "");
		}

		MyDb myDb = new MyDb();
		try {

			String out = myDb.addNewUse(name, passWord) + "";
			Util.serverSend(serverSocket, inPacket, out);
			myDb.closeDb();
		} catch (IOException | NoSuchAlgorithmException e) {
			close(serverSocket);
		}
	}

	/**
	 * 处理用户登录请求
	 * 
	 * @param msg
	 * @param serverSocket
	 * @throws IOException
	 */
	synchronized private void login(User user, DatagramPacket inPacket)
			throws IOException {

		System.out.println(user.getName() + user.getPassword());
		if (userList.containsKey(user.getName())) {
			// 占线中
			Util.serverSend(serverSocket, inPacket, StatusCode.REPEATLOG + "");
		} else {

			MyDb myDb = new MyDb();
			String status = null;
			try {
				status = myDb.LoginCheck(user.getName(), user.getPassword())
						+ "";
				Util.serverSend(serverSocket, inPacket, status);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int result = Integer.parseInt(status);
			if (result == StatusCode.LOGSUCCESSFUL) {
				userList.put(user.getName(), inPacket.getSocketAddress());
			}
			myDb.closeDb();
		}
	}

	public static void main(String[] args) throws SocketException, UnknownHostException {
		System.out.println("服务器启动成功\n");
		new NetServer().init();
	}

}
