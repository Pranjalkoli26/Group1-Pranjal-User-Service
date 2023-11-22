package com.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.entities.UserDto;
import com.demo.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService userService;

	// save user in database(db)
	@PostMapping
	public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
		return userService.saveUser(userDto);
	}

	// authenticate user
	@GetMapping("/authenticate")
	public ResponseEntity<String> authenticateUser(@RequestBody UserDto userDto) {
		return userService.authenticate(userDto);
	}

	// Retrieve user by id from db
	@GetMapping("/id/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable("userId") long id) {
		return userService.findUserById(id);
	}

	// Retrieve user by username from db
	@GetMapping("/username/{username}")
	public ResponseEntity<UserDto> getUserByUsername(@PathVariable("username") String name) {
		return userService.findUserByUsername(name);
	}

	// Retrieve user by email from db
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
		return userService.findUserByEmail(email);
	}

	// get all users as a list
	@GetMapping("/all")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		return userService.getAllUsers();
	}

	// update user in db by id
	@PutMapping("/update/{userid}")
	public ResponseEntity<UserDto> updateUser(@PathVariable("userid") Long userid, @RequestBody UserDto userDto) {
		return userService.updateUser(userDto, userid);
	}

	// delete user by id
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
		return userService.deleteUser(userId);
	}
}

