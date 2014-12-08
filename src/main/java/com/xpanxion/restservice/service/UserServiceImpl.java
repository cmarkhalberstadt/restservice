package com.xpanxion.restservice.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xpanxion.restservice.dao.UserDao;
import com.xpanxion.restservice.dto.beans.UserBean;
import com.xpanxion.restservice.dto.entity.UserEntity;

/**
 * Implementation of the user test service interface. 
 * 
 * @author mhalberstadt
 *
 */
@Transactional
@Service
public class UserServiceImpl implements UserService {
	
	private UserDao userDao;
	
	public List<UserBean> getUserBeans() {
		List<UserEntity> userItems = this.userDao.getAllItems();
		List<UserBean> output = new ArrayList<UserBean>();
		for (UserEntity user : userItems){
			UserBean bean = new UserBean();
			bean.setId(user.getId());
			bean.setUsername(user.getUsername());
			bean.setPassword(user.getPassword());
			output.add(bean);
		}
		return output;
	}
	
	
	public boolean validateAndChangeUserPassword(String username, String oldPassword, String newPassword){
		//Obtain the current password from the database 
		String oldPasswordFromDatabase = this.getUserWithUsername(username).getPassword();
		
		if (oldPasswordFromDatabase.equals(oldPassword)){
			if (newPassword.isEmpty()){
				// the new password is empty - this check should always be redundant - should always happen client-side.
				return false;
			} else {
				// change the password
				this.changePasswordOfUser(username, newPassword);
				return true;
			}
		} else {
			// old password does not match password in database
			return false;
		}
	}
	
	
	public boolean validateAndAddNewUserToDatabase(
			String username,
			String password
			) {
		// check to see if username is already in database
		if (this.isUsernameInDatabase(username)){
			// returning anything other than true says that the user will not be added to the database
			return false;
		}
		
		// make sure password field is not empty
		if (password.isEmpty()){
			// this check is redundant - should be done first on the client.
			return false;
		}
		// add new user to the database 
		this.addUserToDatabase(username, password);
		return true;
	}
	
	/**
	 * Sets the UserDao for this service to use
	 * 
	 * @param dao the dao for this service to use
	 */
	@Resource
	public void setUserDao(UserDao dao){
		this.userDao = dao;
	}

	
	public UserBean getUserWithUsername(String Username) {
		return this.userDao.getUserWithUsername(Username);
	}
	
	
	public void changePasswordOfUser(String Username, String newPassword) {
		this.userDao.changePasswordOfUser(Username, newPassword);
	}
	
	public void addUserToDatabase(String Username, String Password) {
		this.userDao.addUserToDatabase(Username, Password);
	}
	
	public boolean deleteUserFromDatabase(String Username) {
		if (this.isUsernameInDatabase(Username)){
			this.userDao.deleteUserFromDatabase(Username);
			return true;
		} else {
			return false;
		}
		
	}
	
	public boolean isUsernameInDatabase(String Username) {
		return this.userDao.isUsernameInDatabase(Username);
	}
	
	public boolean isPasswordCorrectForGivenUsername(String Username,
			String Password) {
		return this.userDao.isPasswordCorrectForGivenUsername(Username, Password);
	}
}
