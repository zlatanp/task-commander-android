package task.commander.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import task.commander.exceptions.NotFoundException;
import task.commander.model.User;
import task.commander.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public User initialRequestProcessing(String email, String uid){
		
		try {
			User user = userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
			return user;
		} catch (NotFoundException e) {
			User user = new User(email, uid);
			return userRepository.save(user);
		}
		
	}
	
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
	}
}
