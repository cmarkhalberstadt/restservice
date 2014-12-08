package com.xpanxion.restservice.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.xpanxion.restservice.dto.beans.UserBean;
import com.xpanxion.restservice.dto.entity.UserEntity;

/**
 * Interface for the User Dao
 * 
 * @author mhalberstadt
 *
 */
public interface UserDao {
	/**
     * Returns all of the User entities.
     * 
     * @return all of the User entities. 
     */
    List<UserEntity> getAllItems();
    
    
    
    public UserBean getUserWithUsername(String Username);
    public void changePasswordOfUser(String Username, String newPassword);
    public void addUserToDatabase(String Username, String Password);
    public void deleteUserFromDatabase(String Username);
    public boolean isUsernameInDatabase(String Username);
    public boolean isPasswordCorrectForGivenUsername(String Username, String Password);
}
