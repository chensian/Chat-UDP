package cn.gdut.edu.udp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cn.gdut.edu.entity.Message;
import cn.gdut.edu.util.Util;

public class ChatRoom extends Thread implements ActionListener {
	static JFrame frmChat;
	JPanel pnlChat;
	JButton btnCls,  btnSend, btnClear, btnSave;
	JLabel lblUserList, lblUserMessage, lblSendMessage, lblChatUser;
	JLabel lblUserTotal, lblCount, lblBack;
	JTextField txtMessage;
	java.awt.List lstUserList;
	TextArea taUserMessage;
	JComboBox<String> cmbUser;
	JCheckBox chPrivateChat;
	String strServerIp, strLoginName;
	Thread thread;
	JMenuBar mbChat;
	JMenu mnuSystem, mnuHelp;
	JMenuItem mnuiCls, mnuiSave, mnuiExit, mnuiContent, mnuiIndex, mnuiAbout;

	// 用于将窗口用于定位
	Dimension scrnsize;
	Toolkit toolkit = Toolkit.getDefaultToolkit();

	DatagramSocket client;

	int MID = 0;
	Set<String> OnlineUser = new HashSet<String>();
	Set<String> OfflineUser = new HashSet<String>();

	// 构造方法
	public ChatRoom(String name, String ip, DatagramSocket client) {
		strServerIp = ip;
		this.client = client;
		strLoginName = name;
		frmChat = new JFrame("聊天室" + "[用户:" + name + "]");
		pnlChat = new JPanel();
		frmChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChat.getContentPane().add(pnlChat);

		Font fntDisp1 = new Font("宋体", Font.PLAIN, 12);
		// Font fntDisp2=new Font("宋体",Font.PLAIN,11);

		mbChat = new JMenuBar();
		mnuSystem = new JMenu("系统(S)");
		mnuSystem.setMnemonic(KeyEvent.VK_S);
		mnuSystem.setFont(fntDisp1);
		mnuHelp = new JMenu("帮助(H)");
		mnuHelp.setMnemonic(KeyEvent.VK_H);
		mnuHelp.setFont(fntDisp1);
		mbChat.add(mnuSystem);
		mbChat.add(mnuHelp);
		mnuiCls = new JMenuItem("清除屏幕显示");
		mnuiCls.setFont(fntDisp1);
		mnuiSave = new JMenuItem("保存聊天记录");
		mnuiSave.setFont(fntDisp1);
		mnuiExit = new JMenuItem("退出系统");
		mnuiExit.setFont(fntDisp1);
		mnuSystem.add(mnuiCls);
		mnuSystem.add(mnuiSave);
		mnuSystem.add(mnuiExit);
		mnuiContent = new JMenuItem("目录");
		mnuiContent.setFont(fntDisp1);
		mnuiIndex = new JMenuItem("索引");
		mnuiIndex.setFont(fntDisp1);
		mnuiAbout = new JMenuItem("关于[Chat聊天系统]...");
		mnuiAbout.setFont(fntDisp1);
		mnuHelp.add(mnuiContent);
		mnuHelp.add(mnuiIndex);
		mnuHelp.add(mnuiAbout);

		frmChat.setJMenuBar(mbChat);

		String list[] = { "所有人" };
		btnCls = new JButton("清屏(C)");
		btnSend = new JButton("发送(N)");
		btnSave = new JButton("保存");

		lblUserList = new JLabel("【在线用户列表】");
		lblUserMessage = new JLabel("【聊天信息】");
		lblSendMessage = new JLabel("聊天内容:");
		lblChatUser = new JLabel("你对:");
		lblUserTotal = new JLabel("在线人数:");
		lblCount = new JLabel("0");
		lstUserList = new java.awt.List();
		txtMessage = new JTextField(170);
		cmbUser = new JComboBox<String>(list);
		chPrivateChat = new JCheckBox("私聊");
		taUserMessage = new TextArea("", 300, 200,
				TextArea.SCROLLBARS_VERTICAL_ONLY);// 只能向下滚动
		taUserMessage.setEditable(false); // 不可写入

		/*
		 * 该布局采用手动布局 *　setBounds设置组件位置 * setFont设置字体、字型、字号 *
		 * 　setForeground设置文字的颜色 * setBackground设置背景色 * setOpaque将背景设置为透明
		 */

		pnlChat.setLayout(null);
		pnlChat.setBackground(new Color(52, 130, 203));
		btnCls.setBounds(500, 330, 80, 25);
		btnSend.setBounds(500, 300, 80, 25);
		btnSave.setBounds(400, 330, 80, 25);

		lblUserList.setBounds(5, 0, 120, 40);
		lblUserTotal.setBounds(130, 0, 60, 40);
		lblCount.setBounds(190, 0, 60, 40);
		lblUserMessage.setBounds(225, 0, 180, 40);
		lblChatUser.setBounds(10, 290, 40, 40);
		lblSendMessage.setBounds(210, 290, 60, 40);

		// lblUserTotal.setBounds(10,340,100,40);
		// lblCount.setBounds(73,340,100,40);
		lstUserList.setBounds(5, 40, 210, 255);
		taUserMessage.setBounds(225, 40, 360, 255);
		txtMessage.setBounds(270, 300, 210, 25);
		cmbUser.setBounds(50, 300, 80, 25);
		chPrivateChat.setBounds(135, 302, 60, 20);
		btnCls.setFont(fntDisp1);
		btnSend.setFont(fntDisp1);
		btnSave.setFont(fntDisp1);
		lblUserList.setFont(fntDisp1);
		lblUserMessage.setFont(fntDisp1);
		lblChatUser.setFont(fntDisp1);
		lblSendMessage.setFont(fntDisp1);
		lblUserTotal.setFont(fntDisp1);
		lblCount.setFont(fntDisp1);
		cmbUser.setFont(fntDisp1);
		chPrivateChat.setFont(fntDisp1);
		taUserMessage.setFont(new Font("宋体", Font.PLAIN, 12));

		lblUserList.setForeground(Color.YELLOW);
		lblUserMessage.setForeground(Color.YELLOW);
		lblSendMessage.setForeground(Color.black);
		lblChatUser.setForeground(Color.black);
		lblSendMessage.setForeground(Color.black);
		lblUserTotal.setForeground(Color.YELLOW);
		lblCount.setForeground(Color.YELLOW);
		cmbUser.setForeground(Color.black);
		chPrivateChat.setForeground(Color.black);
		lstUserList.setBackground(Color.white);
		taUserMessage.setBackground(Color.white);
		btnCls.setBackground(Color.ORANGE);
		btnSave.setBackground(Color.blue);
		btnSend.setBackground(Color.PINK);

		pnlChat.add(btnCls);
		pnlChat.add(btnSave);
		pnlChat.add(btnSend);
		pnlChat.add(lblUserList);
		pnlChat.add(lblUserMessage);
		pnlChat.add(lblSendMessage);
		pnlChat.add(lblChatUser);
		pnlChat.add(lblUserTotal);
		pnlChat.add(lblCount);
		pnlChat.add(lstUserList);
		pnlChat.add(taUserMessage);
		pnlChat.add(txtMessage);
		pnlChat.add(cmbUser);
		pnlChat.add(chPrivateChat);

		frmChat.addWindowListener(new Windowclose());
		btnCls.addActionListener(this);
		btnSave.addActionListener(this);
		btnSend.addActionListener(this);
		lstUserList.addActionListener(this);
		txtMessage.addActionListener(this);

		// 启动聊天页面信息刷新线程
		Thread thread = new Thread(this);
		thread.start();

		frmChat.setSize(600, 440);
		frmChat.setVisible(true);
		frmChat.setResizable(false);

		// 将窗口定位在屏幕中央
		scrnsize = toolkit.getScreenSize();
		frmChat.setLocation(scrnsize.width / 2 - frmChat.getWidth() / 2,
				scrnsize.height / 2 - frmChat.getHeight() / 2);
		Image img = toolkit.getImage("images\\appico.jpg");
		frmChat.setIconImage(img);

	} // 构造方法结束

	@SuppressWarnings("unchecked")
	public void run() {

		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					// / 1、获取 在线 用户
					Message QUEO = new Message("QUEO");
					Message QUEA = new Message("QUEA");
					try {
						Util.clientSend(client, QUEO);
						Util.clientSend(client, QUEA);
						Thread.sleep(4000);
					} catch (IOException e) {
						e.printStackTrace();
						break;
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
					lstUserList.removeAll();
					lstUserList.add("...ON...");
					// 刷新在线用户 人数
					Iterator<String> iton = OnlineUser.iterator();
					while (iton.hasNext()) {
						String user = iton.next();
					//	System.out.println("user : " + user);
						lstUserList.add(user);
					}
					lstUserList.add("...OFF...");
					//
					Iterator<String> itoff = OfflineUser.iterator();
					while (itoff.hasNext()) {
						String user = itoff.next();
					//	System.out.println("user : " + user);
						lstUserList.add(user);
					}
				}
			}
		});

		thread.start();

		try {
			while (true) {

				// 对 服务器 返回 的 字符串 处理
				Message result = (Message) Util.clientReceive(client);
				String type = result.getType();

				if (type.equals("ONLINEUSER")) {
					OnlineUser = (Set<String>) result.getObject();

				} else if (type.equals("ALLUSER")) {
					OfflineUser = (Set<String>) result.getObject();
					OfflineUser.removeAll(OnlineUser);

				} else if (type.equals("ERROR")) {
					System.out.println("ERROR");
				} else if (type.equals("RETALL")) {

					taUserMessage.append("【" + result.getSendName()
							+ "】对 所有人 说:" + result.getObject().toString()
							+ "\n");
				} else if (type.equals("RETONE")) {

					taUserMessage
							.append("【" + result.getSendName() + "】悄悄对你 说:"
									+ result.getObject().toString() + "\n");
				}

				// 显示 在线人数
				lblCount.setText(OnlineUser.size() + "");

			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "不能连接服务器！" + e.getMessage());
			e.printStackTrace();
			System.out.println("已经断开链接");
		}

	} // run()结束

	// /////////监听按钮响应//////////////
	public void actionPerformed(ActionEvent ae) {
		Object source = (Object) ae.getSource();
		if (source.equals(btnCls)) {
			clearMessage();
		}
		if (source.equals(btnSend)) {
			sendMessage();
		}
		if (source.equals(btnSave)) {
			saveMsg();
		}
		if (source.equals(lstUserList)) // 双击列表框
		{
			changeUser();
		}
	} // actionPerformed()结束

	// /////////监听窗口关闭响应//////////////
	class Windowclose extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			exit();
		}
	}
	
	public void saveMsg() {
		String msg = taUserMessage.getText();
		taUserMessage.append("记录已经保存在Note.txt");
		try {
			FileOutputStream Note = new FileOutputStream("Note.txt");
			Note.write(msg.getBytes());
			Note.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	// "清屏"按钮
	public void clearMessage() {
		taUserMessage.setText("");
	}

	// "退出"按钮
	public void exit() {
		// 发送退出信息
		try {
			// 向服务器发送信息
			Message message = new Message(strLoginName, "EXIT");
			message.setSendName(strLoginName);
			Util.clientSend(client, message);

			frmChat.dispose();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	} // exit()结束

	// "发送"按钮
	public void sendMessage() {

		try {

			Message message = new Message();
			String content = txtMessage.getText();
			String chatUser = String.valueOf(cmbUser.getSelectedItem());
			boolean whisper = chPrivateChat.isSelected() ? true : false;

			if (whisper) {

				message.setType("SENDONE");
				message.setSendName(strLoginName);
				message.setReceiveName(chatUser);
				if (chatUser.equals(strLoginName)) {
					taUserMessage.append("【你】对 自己 说:" + content + "\n");
				} else {
					taUserMessage.append("【你】对 " + chatUser + "说:" + content
							+ "\n");
				}
			} else {
				message.setType("SENDALL");
				// 不要发给自己 messge name 设置成自己
				message.setSendName(strLoginName);
				// 添加至 对话框
				taUserMessage.append("【你】对  所有人说:" + content + "\n");
			}

			message.setObject(content);
			Util.clientSend(client, message);

			txtMessage.setText(""); // 清空文本框

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			System.out.println(e.getStackTrace());
		}
	} // sendMessage()结束

	// 将所选用户添加到cmbUser中
	public void changeUser() {
		boolean key = true;
		String selected = lstUserList.getSelectedItem();

		for (int i = 0; i < cmbUser.getItemCount(); i++) {
			if (selected.equals(cmbUser.getItemAt(i))) {
				key = false;
				break;
			}
		}
		if (key == true) {
			cmbUser.insertItemAt(selected, 0);
		}
		cmbUser.setSelectedItem(selected);

	} // changeUser()结束

	public static void main(String args[]) {
		// new ChatRoom("测试用户", "127.0.0.1", client);
	}

}