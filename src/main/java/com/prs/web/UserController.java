package com.prs.web;

import java.util.List;   
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.User;
import com.prs.db.UserRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserRepo userRepo;
	
	// List all user
	@GetMapping("/")
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}
	
	// Login by username and password
	@GetMapping("/{userName}/{passWord}")
	public User getUserLogin(@PathVariable String userName, @PathVariable String passWord) {
		User u = userRepo.findByUserNameAndPassWord(userName, passWord);
		if (u!=null) {
			return u;
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username and password not found");
		}
	} 
	
	// Get user by id
	@GetMapping("/{id}")
	public Optional<User> getUser(@PathVariable int id) {
		Optional<User> u = userRepo.findById(id);
		if (u.isPresent()) {
			return u;
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		
	}
	
	// Add a user
	@PostMapping("/")
	public User addUser(@RequestBody User u) {
		return userRepo.save(u);
	}
	
	// Update a user
	@PutMapping("/{id}")
	public User updateUser(@RequestBody User u, @PathVariable int id) {
		if (id == u.getId()) {
			return userRepo.save(u);
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id does not match");
		}
	}
	
	// Delete a user
	@DeleteMapping("/{id}")
	public Optional<User> deleteUser(@PathVariable int id) {
		Optional<User> u = userRepo.findById(id);
		if (u.isPresent()) {
			userRepo.deleteById(id);
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id does not found");
		}
		return u;	
	}
}
