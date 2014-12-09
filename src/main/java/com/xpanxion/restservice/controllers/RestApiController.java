package com.xpanxion.restservice.controllers;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xpanxion.restservice.dto.beans.ChangeUserPasswordBean;
import com.xpanxion.restservice.dto.beans.UserBean;
import com.xpanxion.restservice.service.UserService;

/**
 * RESTFul Webservice Controller
 * @author mhalberstadt
 *
 */
@Controller
public class RestApiController {
	private UserService userService;
	
	/**
	 * Returns a list of user beans as a JSON object
	 * @return
	 */
	@RequestMapping(value="/api/users", method=RequestMethod.GET)
	@ResponseBody
	public List<UserBean> getAllUsers_ReturnAsJSONResponseBody(){
		System.out.println("New Project get all users executed.");
		return this.userService.getUserBeans();
	}
	
	
	/**
	 * Returns a specific user's information as a JSON string based on the input username
	 * @param Username String of the username to be found in the list and have user info returned for
	 * @return String containing JSON information 
	 */
	@RequestMapping(value = "/api/user/{Username}", method=RequestMethod.GET)
	@ResponseBody
	public UserBean getSpecificUserByPathVariable(@PathVariable String Username, HttpServletResponse response){
		return this.userService.getUserWithUsername(Username);
	}
	
	
	
	/**
	 * Returns a specific user's information as a JSON string based on the input username
	 * @param Username String of the username to be found in the list and have user info returned for
	 * @return String containing JSON information 
	 */
	@RequestMapping(value="/api/user", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public UserBean getSpecificUserByRequestParam(@RequestParam String Username, HttpServletResponse response){
		return this.userService.getUserWithUsername(Username);
	}
	
	/**
	 * This method will delete the userinformation associated with the username passed in through the path variable
	 * @param Username - the username of the user profile to be deleted
	 */
	@RequestMapping(value="/api/user/{Username}", method=RequestMethod.DELETE, produces="application/json")
	@ResponseBody
	public void deleteGivenUser(@PathVariable String Username){
		this.userService.deleteUserFromDatabase(Username);
	}
	
	/**
	 * This method will change the password for a given user in the database
	 * @param Username - username of profile for password to be changed
	 * @param changeUserPasswordBean - Bean containing both the old password as well as the new password
	 * @return - The UserBean Object showing the user profile with updated information or null if the old password
	 * was not input correctly
	 */
	@RequestMapping(value="/api/user/{Username}", method=RequestMethod.PUT, produces="application/json")
	@ResponseBody
	public UserBean changePasswordForGivenUser(@PathVariable String Username, @RequestBody ChangeUserPasswordBean changeUserPasswordBean){
		UserBean fromDatabase = this.userService.getUserWithUsername(Username);
		if (fromDatabase.getPassword().equals(changeUserPasswordBean.getOldpassword())){
			// old password matches the password stored in the database
			this.userService.changePasswordOfUser(Username, changeUserPasswordBean.getNewpassword());
			return this.userService.getUserWithUsername(Username);
		} else {
			return null;
		}
	}
	
	
	/**
	 * Posts a new user into the database
	 * @param request - the UserBean object containing the username and password of the user to be added
	 * @return - returns the UserBean of the newly created user with Usernmae, password, and ID number upon success.
	 * Will return null if the given Username is already in the database and, therefore, is not available.
	 */
	@RequestMapping(value= "/api/user", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public @ResponseBody UserBean addAUserToTheDatabase(@RequestBody UserBean request){
		UserBean checkFromDatabase = this.userService.getUserWithUsername(request.getUsername());
		if (checkFromDatabase != null){
			if (!checkFromDatabase.getUsername().isEmpty()){
				if (checkFromDatabase.getUsername().equals(request.getUsername())){
					return null;
				}
			}
		}
		this.userService.addUserToDatabase(request.getUsername(), request.getPassword());
		return this.userService.getUserWithUsername(request.getUsername());
	}
	
	
	
	
	
	/**
     * Sets the user test service for this controller
     * 
     * @param service the  user test service to use in this controller. 
     */
    @Resource
    public void setUserTestService(UserService service){
    	this.userService = service;
    }
    
    /**
     * Returns the User Test Service
     * 
     * @return Returns the User Test Service
     */
    public UserService getUserTestService(){
    	return this.userService;
    }
	
	
}
