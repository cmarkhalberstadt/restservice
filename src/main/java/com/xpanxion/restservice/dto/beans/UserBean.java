package com.xpanxion.restservice.dto.beans;

import java.io.Serializable;



public class UserBean implements Serializable{
	private long id;
	private String username;
	private String password;
	
	public UserBean(){}
	
	/**
     * Sets the id of this bean
     * 
     * @param id
     *            the id to set
     */
	public void setId(long id){
		this.id = id;
	}
	
	/**
     * Returns the id of this bean
     * 
     * @return the id the id of this bean
     */
    public long getId() {
        return this.id;
    }
	
	/**
	 * Sets the user name of this bean
	 * 
	 * @param userName the username to set
	 */
	public void setUsername(String username){
		this.username = username;
	}
	
	/**
	 * Returns the user name of this bean
	 * 
	 * @return the user name of this bean
	 */
	public String getUsername(){
		return this.username;
	}
	
	/**
	 * Sets the password of this bean
	 * 
	 * @param password the password to set
	 */
	public void setPassword(String password){
		this.password = password;
	}
	
	/**
	 * Returns the password of this bean
	 * 
	 * @return the password of this bean
	 */
	public String getPassword(){
		return this.password;
	}
}
