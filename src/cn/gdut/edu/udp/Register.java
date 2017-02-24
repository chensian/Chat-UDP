package cn.gdut.edu.udp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.net.DatagramSocket;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import cn.gdut.edu.entity.Message;
import cn.gdut.edu.entity.StatusCode;
import cn.gdut.edu.entity.User;
import cn.gdut.edu.util.Util;

/**
 * 
 * @author chen
 *
 */
public class Register extends JFrame implements ActionListener {
	// 默认端口
	public static final int PORT = 1000;

	private static final long serialVersionUID = 1L;
	JPanel pnlRegister;
	JLabel lblUserName, lblLogo;
	JLabel lblPassword, lblConfirmPass, logoPosition;
	JTextField txtUserName;
	JPasswordField pwdUserPassword, pwdConfirmPass;
	JRadioButton rbtnMale, rbtnFemale;
	ButtonGroup btngGender;
	JButton btnOk, btnCancel, btnClear;
	String strServerIp;
	// 用于将窗口用于定位
	Dimension scrnsize;
	Toolkit toolkit = Toolkit.getDefaultToolkit();

	// 构造方法
	public Register() {

		super("登录聊天室");
		pnlRegister = new JPanel();
		this.getContentPane().add(pnlRegister);

		lblUserName = new JLabel("用户名(U):");
		lblPassword = new JLabel("密 码 (P):");
		lblConfirmPass = new JLabel("确认密 码 (R):");

		txtUserName = new JTextField(20);
		pwdUserPassword = new JPasswordField(20);
		pwdConfirmPass = new JPasswordField(20);

		btnCancel = new JButton("返回登录(L)");
		btnOk = new JButton("注册(R)");
		btnClear = new JButton("清空(C)");

		/*
		 * 该布局采用手动布局 *　setBounds设置组件位置 * setFont设置字体、字型、字号 *
		 * 　setForeground设置文字的颜色 * setBackground设置背景色 * setOpaque将背景设置为透明
		 */
		pnlRegister.setLayout(null); // 组件用手动布局
		pnlRegister.setBackground(new Color(52, 130, 203));

		lblUserName.setBounds(50, 100, 100, 30);
		txtUserName.setBounds(150, 100, 120, 25);

		lblPassword.setBounds(50, 130, 100, 30);
		pwdUserPassword.setBounds(150, 130, 120, 25);

		lblConfirmPass.setBounds(50, 160, 100, 30);
		pwdConfirmPass.setBounds(150, 160, 120, 25);

		btnOk.setBounds(50, 200, 80, 25);
		btnCancel.setBounds(130, 200, 80, 25);
		btnClear.setBounds(210, 200, 80, 25);

		Font fontstr = new Font("宋体", Font.PLAIN, 12);
		lblUserName.setFont(fontstr);
		txtUserName.setFont(fontstr);

		lblPassword.setFont(fontstr);
		pwdUserPassword.setFont(fontstr);

		lblConfirmPass.setFont(fontstr);
		pwdConfirmPass.setFont(fontstr);

		btnCancel.setFont(fontstr);
		btnOk.setFont(fontstr);
		btnClear.setFont(fontstr);

		lblPassword.setForeground(Color.BLACK);
		lblPassword.setForeground(Color.BLACK);
		btnCancel.setBackground(Color.ORANGE);
		btnOk.setBackground(Color.ORANGE);
		btnClear.setBackground(Color.ORANGE);

		pnlRegister.add(lblUserName);
		pnlRegister.add(txtUserName);
		pnlRegister.add(lblPassword);
		pnlRegister.add(pwdUserPassword);
		pnlRegister.add(lblConfirmPass);
		pnlRegister.add(pwdConfirmPass);
		pnlRegister.add(btnCancel);
		pnlRegister.add(btnOk);
		pnlRegister.add(btnClear);

		// 设置背景图片
		Icon logo1 = new ImageIcon("D:/logo.png");
		lblLogo = new JLabel(logo1);
		lblLogo.setBounds(0, 0, 340, 90);
		pnlRegister.add(lblLogo);
		// 设置登录窗口
		setResizable(false);
		setSize(340, 260);
		setVisible(true);
		scrnsize = toolkit.getScreenSize();
		setLocation(scrnsize.width / 2 - this.getWidth() / 2, scrnsize.height
				/ 2 - this.getHeight() / 2);
		Image img = toolkit.getImage("images\\appico.jpg");
		setIconImage(img);

		// 三个按钮注册监听
		btnOk.addActionListener(this);
		btnCancel.addActionListener(this);
		btnClear.addActionListener(this);
	} // 构造方法结束

	// 按钮监听响应
	public void actionPerformed(ActionEvent ae) {
		Object source = new Object();
		source = ae.getSource();
		if (source.equals(btnOk)) // "确定"按钮
		{
			register();
		}
		if (source.equals(btnCancel)) // "返回"按钮
		{
			new Login();
			this.dispose();
		}
		if (source.equals(btnClear)) // "清空"按钮
		{
			txtUserName.setText("");
			pwdUserPassword.setText("");
			pwdConfirmPass.setText("");
		}
	} // actionPerformed()结束

	// ////////"确定"按钮事件响应//////////
	public void register() {
		// 接受客户的详细资料

		String custName = txtUserName.getText();
		String custPassword = new String(pwdUserPassword.getPassword());

		// 验证用户名是否为空
		if (custName.length() == 0) {
			JOptionPane.showMessageDialog(null, "用户名不能为空");
			return;
		}
		String regEx = "\\d{10}";
		Pattern pattern = Pattern.compile(regEx);
		boolean rs = pattern.matcher(custName).matches();
		System.out.println(rs);
		if (!rs) {
			JOptionPane.showMessageDialog(null, "用户名为固定10位的数字");
			return;
		}
		// 验证密码是否为空
		if (custPassword.length() == 0) {
			JOptionPane.showMessageDialog(null, "密码不能为空");
			return;
		}

		//
		String regEx2 = "[0-9]{6,8}";
		Pattern pattern2 = Pattern.compile(regEx2);
		boolean rs2 = pattern2.matcher(custPassword).matches();

		if (!rs2) {
			JOptionPane.showMessageDialog(null, "密码为6-8数字");
			return;
		}

		// 验证密码的一致性
		if (!custPassword.equals(new String(pwdConfirmPass.getPassword()))) {
			JOptionPane.showMessageDialog(null, "密码两次输入不一致，请重新输入");
			return;
		}

		// 验证Email的正确性

		try {
			// 连接到服务器

			DatagramSocket client = new DatagramSocket();

			Message message = new Message("REG");

			User user = new User(custName, custPassword);
			message.setObject(user);
			Util.clientSend(client, message);

			String result = (String) Util.clientReceive(client);

			int status = Integer.parseInt(result);
			if (status == StatusCode.REGSUCCESSFUL) {
				JOptionPane.showMessageDialog(null, "注册成功");
				this.dispose();
				new Login();

			} else if (status == StatusCode.USEEXIST) {
				JOptionPane.showMessageDialog(null, "用户名已经被注册");
			} else if (status == StatusCode.REGFAILL) {
				JOptionPane.showMessageDialog(null, "注册失败");
			} else {
				JOptionPane.showMessageDialog(null, "未知错误");
			}

			client.close();
		} catch (InvalidClassException e1) {
			JOptionPane.showMessageDialog(null, "类错误!");
		} catch (NotSerializableException e2) {
			JOptionPane.showMessageDialog(null, "对象未序列化!");
		} catch (IOException e3) {
			JOptionPane.showMessageDialog(null, "不能写入到指定服务器!");
		}

	} // 方法register()结束

	public static void main(String args[]) {
		new Register();
	}

} // class Register 结束
