package cn.gdut.edu.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import cn.gdut.edu.entity.Record;
import cn.gdut.edu.entity.StatusCode;

import com.mysql.jdbc.PreparedStatement;

public class MyDb {

	private String Driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/use";
	private String use = "root";
	private String password = "root";
	private Connection connection;
	private Statement statement;

	public MyDb() {
		try {
			Class.forName(Driver);
			connection = DriverManager.getConnection(url, use, password);
			statement = connection.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭数据库
	 */

	public void closeDb() {
		try {
			if (!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据库中注册了的所有用户
	 * 
	 * @param uselist
	 * @return
	 */
	public Set<String> getAlluser() {

		String sql = "select name from use_name ;";
		Set<String> allUseLlist = new HashSet<String>();
		try {
			ResultSet rSet = statement.executeQuery(sql);
			while (rSet.next()) {
				String name = rSet.getString("name");
				allUseLlist.add(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allUseLlist;
	}

	/**
	 * 检查用户是否存在
	 * 
	 * @param name
	 * @return
	 */

	public boolean CheckUse(String name) {
		try {
			if (!connection.isClosed()) {

				String sql = "select name from use_name where name='" + name
						+ "'";
				ResultSet rSet = statement.executeQuery(sql);

				if (rSet.next()) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 添加新用户
	 * 
	 * @param name
	 * @param password
	 * @return StatusCode.REGSUCCESSFUL StatusCode.REGFAILL StatusCode.USEEXIST
	 * @throws NoSuchAlgorithmException 
	 */
	public int addNewUse(String name, String password) throws NoSuchAlgorithmException {
		if (CheckUse(name)) {
			return StatusCode.USEEXIST;
		} else {
			
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			password =Base64.encodeBase64String(md5.digest());
			
			String sql = "insert into use_name values( '" + name + "','"
					+ password + "');";
			try {
				int resultSet = statement.executeUpdate(sql);
				if (resultSet == 1) {
					return StatusCode.REGSUCCESSFUL;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return StatusCode.REGFAILL;
	}

	/**
	 * 登陆校验
	 * 
	 * @param name
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */

	public int LoginCheck(String name, String password) throws NoSuchAlgorithmException {
		if (!CheckUse(name)) {
			return StatusCode.WRONGUSEORPASSWORD;
		} else {
			String sql = "select password from use_name where name=" + name
					+ ";";
			try {
				ResultSet rSet = statement.executeQuery(sql);
				if (!rSet.next()) {
					return StatusCode.WRONGUSEORPASSWORD;
				} else {
					
					MessageDigest md5 = MessageDigest.getInstance("MD5");
					md5.update(password.getBytes());
					password =Base64.encodeBase64String(md5.digest());
					
					String passWord = rSet.getString("password");
					if (passWord.equals(password)) {
						return StatusCode.LOGSUCCESSFUL;
					} else {
						return StatusCode.WRONGUSEORPASSWORD;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return StatusCode.WRONGUSEORPASSWORD;
	}

	public void saveRecord(Record record) {
		String sql = "insert into record values(null,?,?,?,?,0)";
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, record.getType());
			pstmt.setString(2, record.getContent());
			pstmt.setString(3, record.getSendName());
			pstmt.setString(4, record.getReceivename());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<Record> selectRecordByreceiveName(String name) {
		String sql = "select * from record where receivename = ? and isread = 0";
		PreparedStatement pstmt;
		List<Record> list = new ArrayList<Record>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Record record = new Record();
				record.setType(rs.getString(2));
				record.setContent(rs.getString(3));
				record.setSendName(rs.getString(4));
				record.setReceivename(rs.getString(5));
				System.out.println("");
				list.add(record);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public int updateRecordByreceiveName(String name) {
		String sql = "update record set isread = 1 where receivename='" + name
				+ "'";
		PreparedStatement pstmt;
		int result = 0;
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			result = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	


}
