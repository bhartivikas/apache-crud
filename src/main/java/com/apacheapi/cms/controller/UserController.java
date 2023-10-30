package com.apacheapi.cms.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apacheapi.cms.dto.UserRequestDto;
import com.apacheapi.cms.dto.UserResponseDto;
import com.apacheapi.cms.dto.UserUpdateDto;
import com.apacheapi.cms.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

	private final UserService userService;

	@PostMapping
	public ResponseEntity<String> saveUser(@RequestBody UserRequestDto userRequestDto) {
		this.userService.saveUser(userRequestDto);
		return new ResponseEntity<String>("user saved successfully", HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		List<UserResponseDto> userResponseDtos = new ArrayList<>();
		userResponseDtos = this.userService.getAllUsers();
		return ResponseEntity.ok(userResponseDtos);

	}

	@PutMapping("/{username}")
	public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody UserUpdateDto userUpdateDto) {
		this.userService.updateUser(username, userUpdateDto);
		return new ResponseEntity<String>("User updated successfully", HttpStatus.OK);
	}

}
