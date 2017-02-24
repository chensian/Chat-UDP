package cn.gdut.edu.entity;

public class Friend {
	
	String nameA;
	String nameB;
	
	public Friend() {
		super();
	}

	public Friend(String nameA, String nameB) {
		super();
		this.nameA = nameA;
		this.nameB = nameB;
	}

	public String getNameA() {
		return nameA;
	}

	public void setNameA(String nameA) {
		this.nameA = nameA;
	}

	public String getNameB() {
		return nameB;
	}

	public void setNameB(String nameB) {
		this.nameB = nameB;
	}
	
}
