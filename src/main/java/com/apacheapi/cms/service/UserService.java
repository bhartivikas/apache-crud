package com.apacheapi.cms.service;

import java.util.List;

import com.apacheapi.cms.dto.UserRequestDto;
import com.apacheapi.cms.dto.UserResponseDto;
import com.apacheapi.cms.dto.UserUpdateDto;

public interface UserService {
	
	public void saveUser(UserRequestDto userRequestDto);
	
	public List<UserResponseDto> getAllUsers();
	
	public void updateUser(String username, UserUpdateDto userUpdateDto);

}
