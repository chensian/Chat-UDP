package cn.gdut.edu.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Util {

	// 定义发送数据报的目的地
	public static final int DEST_PORT = 30000;
	public static final String DEST_IP = "localhost";
	public static final int DATA_LEN = 4096;

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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// 封装 客户端 接收    返回 Object
	public static Object clientReceive(DatagramSocket client)
			throws IOException {

		// 定义接收网络数据的字节数组
		byte[] inBuff = new byte[DATA_LEN];
		// 定义一个用于发送的DatagramPacket对象
		DatagramPacket inPacket = new DatagramPacket(inBuff, inBuff.length);
		client.receive(inPacket);

		return ByteToObject(inPacket);
	}

	
	// 封装 客户端 发送  Object
	public static void clientSend(DatagramSocket client, Object object)
			throws IOException {

		DatagramPacket outPacket = new DatagramPacket(new byte[0], 0,
				InetAddress.getByName(DEST_IP), DEST_PORT);
		outPacket.setData(ObjectToByte(object));
		client.send(outPacket);

	}
	
	
//	?????????????????????????????????????????????????????????????????????????????????????

	// 封装 服务端 接收
	public static DatagramPacket serverReceive(DatagramSocket client)
			throws IOException {

		// 定义接收网络数据的字节数组
		byte[] inBuff = new byte[DATA_LEN];
		// 定义一个用于发送的DatagramPacket对象
		DatagramPacket inPacket = new DatagramPacket(inBuff, inBuff.length);
		client.receive(inPacket);

		return inPacket;
	}

	// 封装 服务端 发送 Object
	public static void serverSend(DatagramSocket serverSocket,
			DatagramPacket inPacket, Object object) throws IOException {

		DatagramPacket outPacket = new DatagramPacket(new byte[0], 0,
				InetAddress.getByName(DEST_IP), DEST_PORT);
		outPacket.setSocketAddress(inPacket.getSocketAddress());
		outPacket.setData(ObjectToByte(object));
		serverSocket.send(outPacket);
	}

}
