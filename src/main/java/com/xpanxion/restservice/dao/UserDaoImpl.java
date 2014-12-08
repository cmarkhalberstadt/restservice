package com.xpanxion.restservice.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.xpanxion.restservice.dto.beans.UserBean;
import com.xpanxion.restservice.dto.entity.UserEntity;

/**
 * Implementation of the UserDao interface. 
 * 
 * @author mhalberstadt
 *
 */
@Repository
public class UserDaoImpl implements UserDao {
	
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<UserEntity> getAllItems() {
		//return this.sessionFactory.getCurrentSession().getNamedQuery("userNamesAndPasswords.getAll").list();
		
		
		
		Session session = null;
		Transaction tx = null;
		ArrayList<UserEntity> retval = new ArrayList<UserEntity>();
		
		try{
			session = this.sessionFactory.openSession();
		} catch (HibernateException ex){
			System.out.println("Exception thrown while opening session: " + ex);
		}
		
		try {
			tx = session.beginTransaction();
		} catch (HibernateException ex){
			System.out.println("Expection thrown while beginning transaction: " + ex);
		}
		
		retval = (ArrayList<UserEntity>) session.createQuery("From UserEntity").list();
		
		try {
			tx.commit();
		} catch (HibernateException ex){
			System.out.println("Expection thrown while committing transaction: " + ex);
			if (tx != null){
				tx.rollback();
			}
		}
		
		
		try {
			session.close();
		} catch (HibernateException ex){
			System.out.println("Exception thrown while closing session: " + ex);
		}
		
		Collections.sort(retval);
		
		return retval;
	}
	
	
	private ArrayList<Object> runSQLQueryAndGetReturnList(String SQLQuery){
    	Session session = this.sessionFactory.openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
		} catch (HibernateException ex){
			System.err.println("Exception thrown when beginning transaction: " + ex);
		}
		
		
		
		ArrayList returnValue = (ArrayList) session.createSQLQuery(SQLQuery).list();
		
		try {
			if (tx != null){
				tx.commit();
			}
		} catch (HibernateException ex){
			System.err.println("Exception thrown when committing transaction: " + ex);
			if (tx != null){
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return returnValue;
    }
    
    private int runSQLQueryWithNoReturnValue(String SQLQuery){
    	Session session = this.sessionFactory.openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
		} catch (HibernateException ex){
			System.err.println("Exception thrown when beginning transaction: " + ex);
		}
		
		
		int returnValue = session.createSQLQuery(SQLQuery).executeUpdate();
		
		try {
			if (tx != null){
				tx.commit();
			}
		} catch (HibernateException ex){
			System.err.println("Exception thrown when committing transaction: " + ex);
			if (tx != null){
				tx.rollback();
			}
		} finally {
			session.close();
		}
		return returnValue;
    }
    
    private long getIDForGivenUsernameInDatabase(String username){
    	String tableName = "usernamesandpasswords";
		String SQLQuery = "SELECT id FROM " + tableName + "\n";
		SQLQuery += "WHERE username=" + "'" + username + "'" + ";";
		
		ArrayList list = this.runSQLQueryAndGetReturnList(SQLQuery);
		
		if (list == null){
			return -1;
		}
		if (list.size() == 0){
			return -1;
		}
		
		for (Object o : list){
			if (o != null){
				Integer i = (Integer)o;
				return i.longValue();
			}
		}
		// should never get to this point.
		return -1;
    }
    
    private String getPasswordForGivenUsernameInDatabase(String username){
        String tableName = "usernamesandpasswords";
		String SQLQuery = "SELECT password FROM " + tableName + "\n";
		SQLQuery += "WHERE username=" + "'" + username + "'" + ";";
		
		ArrayList list = this.runSQLQueryAndGetReturnList(SQLQuery);
		
		if (list == null){
			return "";
		}
		if (list.size() == 0){
			return "";
		}
		
		for (Object o : list){
			if (o != null){
				return o.toString();
			}
		}
		// should never get to this point.
		return "";
	}
    
    public void deleteGivenUserFromDataBase(String Username){
		String tableName = "usernamesandpasswords";
		
		String SQLQuery = "DELETE FROM " + tableName  + "\n";
		SQLQuery += "WHERE username='" + Username + "';";
		
		this.runSQLQueryWithNoReturnValue(SQLQuery);
		
	}
	
	
	
	/**
     * Sets the session factory for this dao to use. 
     * 
     * @param factory the session factory for this dao.
     */
    @Resource
    public void setSesionFactory(SessionFactory factory) {
        this.sessionFactory = factory;
    }
    
    
    
	public UserBean getUserWithUsername(String Username) {
		String password = this.getPasswordForGivenUsernameInDatabase(Username);
		if (password == null){
			return null;
		}
		if (password.isEmpty()){
			return null;
		}
		// else
		UserBean retval = new UserBean();
		retval.setPassword(password);
		retval.setUsername(Username);
		retval.setId(this.getIDForGivenUsernameInDatabase(Username));
		return retval;
	}
	
	
	public void changePasswordOfUser(String Username, String newPassword) {
		String tableName = "usernamesandpasswords";
		
		String SQLQuery = "";
		
		SQLQuery = "UPDATE " + tableName + "\n";
		SQLQuery += "SET password=" + "'" + newPassword + "'" + "\n";
		SQLQuery += "WHERE username=" + "'" + Username + "'" + ";";
		this.runSQLQueryWithNoReturnValue(SQLQuery);
	}
	
	public void addUserToDatabase(String Username, String Password) {
		String tableName = "usernamesandpasswords";
		
		String SQLQuery = "INSERT INTO " + tableName + " (username, password)" + "\n";
		SQLQuery += "VALUES ('" + Username + "', '" + Password + "');";
		
		this.runSQLQueryWithNoReturnValue(SQLQuery);
	}
	
	
	public void deleteUserFromDatabase(String Username) {
		this.deleteGivenUserFromDataBase(Username);
	}
	
	
	public boolean isUsernameInDatabase(String Username) {
		String toCheck = this.getPasswordForGivenUsernameInDatabase(Username);
		if (toCheck == null){
			return false;
		}
		if (toCheck.isEmpty()){
			return false;
		}
		// else
		return true;
	}
	
	
	public boolean isPasswordCorrectForGivenUsername(String Username,
			String Password) {
		if (this.isUsernameInDatabase(Username)){
			UserBean u = this.getUserWithUsername(Username);
			if (Password.equals(u.getPassword())){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
