package net.javaguides.springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.repository.UserRepository;

@Service("userService")
public class UserService {

	private UserRepository userRepository;

	//@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User findByEmail(String email) {
		List<User> list=userRepository.findAll();
		for (User user : list) {
			if(email.equals(user.getEmail()))
				return user;
		}
		return null;
	}
	
	public User findByConfirmationToken(String confirmationToken) {
		return userRepository.findByConfirmationToken(confirmationToken);
	}
	
	public void saveUser(User user) {
		userRepository.save(user);
	}
	
	public List<User> findAll() {
		return userRepository.findAll();
	}

	public void deleteByEmail(String email) {
		
		List<User> list=userRepository.findAll();
		for (User user : list) {
			
			if(email.equals(user.getEmail()))
			userRepository.deleteById(user.getId());
		}
		
	}
	

}