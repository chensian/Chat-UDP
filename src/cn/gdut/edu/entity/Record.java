package cn.gdut.edu.entity;

public class Record {

	private String type;
	private String content;
	
	private String sendName;
	private String receivename;
	private boolean isread = false;

	public Record(String type, String content) {
		super();
		this.type = type;
		this.content = content;
	}

	public Record() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getReceivename() {
		return receivename;
	}

	public void setReceivename(String receivename) {
		this.receivename = receivename;
	}

	public boolean isIsread() {
		return isread;
	}

	public void setIsread(boolean isread) {
		this.isread = isread;
	}
	
	

}
