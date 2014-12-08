package com.xpanxion.restservice.dto.entity;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.web.bind.annotation.RequestBody;




import org.hibernate.annotations.*;

//@NamedQuery(name = "userNamesAndPasswords.getAll", query = "from UserEntity")
//@SQLInsert(sql="INSERT INTO userNamesAndPasswords(username, password) VALUES(?, ?)")
/**
 * User Entity
 * 
 * Represents items from the user test table.  
 * Exposes one named query that returns all entities from the table
 * 
 * @author bsmith
 *
 */
@Entity
@Table(name = "userNamesAndPasswords")
public class UserEntity implements Comparable, Serializable{
	private long id;
	private String username;
	private String password;
	
	
	
	/**
     * Returns the Id of the entity.  This is the primary key. 
     * 
     * @return the id of the entity
     */
    @Id
    @GeneratedValue
    public long getId() {
        return this.id;
    }
    
    /**
     * Returns the entity's user name text field
     * 
     * @return the user name text field. 
     */
    @Column(name="username")
    public String getUsername() {
        return this.username;
    }
    
    /**
     * Returns the entity's password text field
     * 
     * @return the password text field. 
     */
    @Column(name="password")
    public String getPassword() {
        return this.password;
    }
    
    /**
     * Sets the entity's id. 
     * 
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * Sets the entity's user name text field
     * 
     * @param userName the user name to set
     */
    public void setUsername(String username){
    	this.username = username;
    }
    
    /**
     * Sets the entity's password test field
     * 
     * @param password the password to set
     */
    public void setPassword(String password){
    	this.password = password;
    }

    
	public int compareTo(Object o) {
		if (o != null){
			UserEntity u = null;
			try {
				u = (UserEntity)o;
			} catch (ClassCastException ex){
				return 1;
			}
			
			if (this.id > u.getId()){
				return 1;
			} else if (this.id == u.getId()){
				return 0;
			} else {
				return -1;
			}
		} else {
			return 1;
		}
	}
}
