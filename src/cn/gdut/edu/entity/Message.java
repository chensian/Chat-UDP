package cn.gdut.edu.entity;

import java.io.Serializable;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object object;
	private String type;
	private String sendName;
	private String receiveName;

	public Message() {
		super();
	}

	
	public Message(String type) {
		super();
		this.type = type;
	}


	public Message(Object object, String type) {
		super();
		this.object = object;
		this.type = type;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getSendName() {
		return sendName;
	}


	public void setSendName(String sendName) {
		this.sendName = sendName;
	}


	public String getReceiveName() {
		return receiveName;
	}


	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}


	@Override
	public String toString() {
		return "Message [object=" + object + ", type=" + type + ", sendName="
				+ sendName + ", receiveName=" + receiveName + "]";
	}

	
	
	
}
