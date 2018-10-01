package task.commander.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import task.commander.model.User;
import task.commander.service.UserService;

@Controller
@RequestMapping(value="/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	
	/**
	 * provera da li takav korisnik postoji kod nas ili ga treba napraviti
	 * ako postoji povuci sve njegove grupe i taskove
	 */
	@RequestMapping(
			value = "/initialRequest/{user_email}/{user_uid}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> createUser(@PathVariable("user_email") String user_email ,
			@PathVariable("user_uid") String user_uid,
			HttpServletRequest request) throws Exception {
		
		User user = userService.initialRequestProcessing(user_email, user_uid);
		
		System.out.println("-------------------POGODJEN-----------------------------");
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	

}
