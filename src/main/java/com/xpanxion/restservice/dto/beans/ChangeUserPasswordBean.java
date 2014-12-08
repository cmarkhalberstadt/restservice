package com.xpanxion.restservice.dto.beans;

import java.io.Serializable;

public class ChangeUserPasswordBean implements Serializable{
	
	private String oldpassword;
	private String newpassword;
	
	public ChangeUserPasswordBean(){}
	
	public String getNewpassword() {
		return newpassword;
	}


	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}


	public String getOldpassword() {
		return oldpassword;
	}


	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}
}
