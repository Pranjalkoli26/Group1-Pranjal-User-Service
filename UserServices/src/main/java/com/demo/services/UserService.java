package com.demo.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.entities.User;
import com.demo.entities.UserDto;
import com.demo.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	BCryptPasswordEncoder bcrypt;

	@Autowired
	ModelMapper mapper;

	// method for save user in db
	public ResponseEntity<UserDto> saveUser(UserDto dto) {

		String encryptedPassword = bcrypt.encode(dto.getPassword());
		User user = mapToUser(dto);
		user.setPassword(encryptedPassword); // encodes password before saving in db
		User newUser = userRepo.save(user);
		UserDto responseDto = mapToDto(newUser);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	// method for authenticating existing user
	public ResponseEntity<String> authenticate(UserDto dto) {
		Long id = dto.getId();
		User existingUser = userRepo.findById(dto.getId())
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

		if (bcrypt.matches(dto.getPassword(), existingUser.getPassword())
				&& dto.getUsername().equals(existingUser.getUsername())) {
			return new ResponseEntity<>("user exist", HttpStatus.OK);
		} else {
			throw new WrongCredentialsException("give correct username and password matching with input id");
		}

	}

	// method to find user by id
	public ResponseEntity<UserDto> findUserById(long id) {

		User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
		UserDto responseDto = mapToDto(user);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}

	// method to find user by username
	public ResponseEntity<UserDto> findUserByUsername(String name) {

		User user = userRepo.findByUsername(name)
				.orElseThrow(() -> new UserNotFoundException("User not found with name: " + name));
		UserDto responseDto = mapToDto(user);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}

	// method to find user by email
	public ResponseEntity<UserDto> findUserByEmail(String email) {

		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
		UserDto responseDto = mapToDto(user);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);

	}

	// method to get all users as a list
	public ResponseEntity<List<UserDto>> getAllUsers() {

		List<User> userList = userRepo.findAll();
		List<UserDto> dtoList = userList.stream().map(user -> mapToDto(user)).toList();
		return new ResponseEntity<>(dtoList, HttpStatus.OK);

	}

	// method to update user
	public ResponseEntity<UserDto> updateUser(UserDto dto, Long id) {

		User existingUser = userRepo.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

		existingUser.setEmail(dto.getEmail());
		existingUser.setUsername(dto.getUsername());
		String encryptedPassword = bcrypt.encode(dto.getPassword());
		existingUser.setPassword(encryptedPassword);

		User updatedUser = userRepo.save(existingUser);
		UserDto responseDto = mapToDto(updatedUser);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);

	}

	// method to delete user from db
	public ResponseEntity<String> deleteUser(Long userId) {
		User existingUser = userRepo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
		userRepo.deleteById(userId);
		return new ResponseEntity<>("user deleted succefully!", HttpStatus.OK);

	}

	// mapping methods to map user and userDto
	public UserDto mapToDto(User User) {
		UserDto dto = mapper.map(User, UserDto.class);
		dto.setPassword("******");     //mask the password when showing response of API call
		return dto;
	}

	public User mapToUser(UserDto dto) {
		User user = mapper.map(dto, User.class);
		return user;
	}
}