package cn.gdut.edu.udp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramSocket;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import cn.gdut.edu.entity.Message;
import cn.gdut.edu.entity.StatusCode;
import cn.gdut.edu.entity.User;
import cn.gdut.edu.util.Util;

/**
 * 根据指定的服务器地址、用户名和密码登录聊天服务器
 * 
 */
public class Login extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPanel pnlLogin;
	JButton btnLogin, btnRegister, btnExit;
	JLabel lblServer, lblUserName, lblPassword, lblLogo;
	JTextField txtUserName, txtServer;
	JPasswordField pwdPassword;
	// 用于将窗口定位
	Dimension scrnsize;
	Toolkit toolkit = Toolkit.getDefaultToolkit();

	DatagramSocket client;
	// 构造方法
	public Login() {
		super("登录聊天室");
		pnlLogin = new JPanel();
		this.getContentPane().add(pnlLogin);

		lblServer = new JLabel("服务器(S):");
		lblUserName = new JLabel("用户名(U):");
		lblPassword = new JLabel("密 码 (P):");

		txtServer = new JTextField(20);
		txtServer.setText("127.0.0.1");

		txtUserName = new JTextField(20);
		pwdPassword = new JPasswordField(20);

		btnLogin = new JButton("登录(L)");
		btnLogin.setToolTipText("登录到服务器");
		btnLogin.setMnemonic('L');

		btnRegister = new JButton("注册(R)");
		btnRegister.setToolTipText("注册新用户");
		btnRegister.setMnemonic('R');

		btnExit = new JButton("退出(X)");
		btnExit.setToolTipText("退出系统");
		btnExit.setMnemonic('X');

		/*
		 * 该布局采用手动布局 *　setBounds设置组件位置 * setFont设置字体、字型、字号 *
		 * 　setForeground设置文字的颜色 * setBackground设置背景色 * setOpaque将背景设置为透明
		 */
		pnlLogin.setLayout(null); // 组件用手动布局
		pnlLogin.setBackground(new Color(52, 130, 203));

		lblServer.setBounds(50, 100, 100, 30);
		txtServer.setBounds(150, 100, 120, 25);

		lblUserName.setBounds(50, 130, 100, 30);
		txtUserName.setBounds(150, 130, 120, 25);

		lblPassword.setBounds(50, 160, 100, 30);
		pwdPassword.setBounds(150, 160, 120, 25);
		btnLogin.setBounds(50, 200, 80, 25);
		btnRegister.setBounds(130, 200, 80, 25);
		btnExit.setBounds(210, 200, 80, 25);

		Font fontstr = new Font("宋体", Font.PLAIN, 12);
		lblServer.setFont(fontstr);
		txtServer.setFont(fontstr);
		lblUserName.setFont(fontstr);
		txtUserName.setFont(fontstr);
		lblPassword.setFont(fontstr);
		pwdPassword.setFont(fontstr);
		btnLogin.setFont(fontstr);
		btnRegister.setFont(fontstr);
		btnExit.setFont(fontstr);

		lblUserName.setForeground(Color.BLACK);
		lblPassword.setForeground(Color.BLACK);
		btnLogin.setBackground(Color.ORANGE);
		btnRegister.setBackground(Color.ORANGE);
		btnExit.setBackground(Color.ORANGE);

		pnlLogin.add(lblServer);
		pnlLogin.add(txtServer);
		pnlLogin.add(lblUserName);
		pnlLogin.add(txtUserName);
		pnlLogin.add(lblPassword);
		pnlLogin.add(pwdPassword);
		pnlLogin.add(btnLogin);
		pnlLogin.add(btnRegister);
		pnlLogin.add(btnExit);

		// 设置背景图片
		Icon logo1 = new ImageIcon("D:/logo.png");
		lblLogo = new JLabel(logo1);
		lblLogo.setBounds(0, 0, 340, 90);
		pnlLogin.add(lblLogo);
		// 设置登录窗口
		setResizable(false);
		setSize(340, 260);
		setVisible(true);
		scrnsize = toolkit.getScreenSize();
		setLocation(scrnsize.width / 2 - this.getWidth() / 2, scrnsize.height
				/ 2 - this.getHeight() / 2);
		// Image img = toolkit.getImage("images\\appico.jpg");
		// setIconImage(img);

		// 三个按钮注册监听
		btnLogin.addActionListener(this);
		btnRegister.addActionListener(this);
		btnExit.addActionListener(this);

	} // 构造方法结束

	// 按钮监听响应
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		if (source.equals(btnLogin)) {
			// 判断用户名和密码是否为空
			if (txtUserName.getText().equals("")
					|| pwdPassword.getPassword().toString().equals("")) {
				JOptionPane.showMessageDialog(null, "用户名或密码不能为空");
			} else {
				login();
			}
		}
		if (source.equals(btnRegister)) {
			this.dispose();
			new Register();
		}
		if (source == btnExit) {
			System.exit(0);
		}
	} // actionPerformed()结束

	// ////////登录事件响应方法//////////

	public void login() {

		 String name = txtUserName.getText();
		 String password = new String(pwdPassword.getPassword());
//
//		String name = "3114006171";
//		String password = "123456";
		String ip = Util.DEST_IP;

		try {
			// 连接到服务器

			System.out.println("正在连接服务器... ");

			// 创建一个客户端DatagramSocket，使用随机端口
			client = new DatagramSocket();
			// 初始化发送用的DatagramSocket，它包含一个长度为0的字节数组

			User user = new User(name, password);
			System.out.println(new String(Util.ObjectToByte(user)));
			
			Message message = new Message(user, "LOG");

			Util.clientSend(client, message);
			// 读取Socket中的数据，读到的数据放在inPacket所封装的字节数组中
			// 读来自服务器socket的登录状态
			System.out.println("读来自服务器socket的登录状态...");
			//
			String result = (String) Util.clientReceive(client);
			int status = Integer.parseInt(result);
			System.out.println(status);
			if (status == StatusCode.LOGSUCCESSFUL) {
				System.out.println("进入聊天室");
				this.dispose();
				// 关闭流对象
				new ChatRoom((String) name, ip, client);
 
			} else if (status== StatusCode.WRONGUSEORPASSWORD) {
				JOptionPane.showMessageDialog(null, "用户名或密码错误");
			}else if(status== StatusCode.REPEATLOG){
				JOptionPane.showMessageDialog(null, "你已经登录");
			}
		} catch (ConnectException e1) {
			JOptionPane.showMessageDialog(null, "未能建立到指定服务器的连接!");
		} catch (IOException e2) {
			JOptionPane.showMessageDialog(null, "不能写入到指定服务器!");
		}
	}

	public static void main(String args[]) {
		new Login();
	}

}