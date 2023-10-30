package com.apacheapi.cms.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
	
	private String username;
	private String password;
	private String gender;
	private String mobile;
	private String email;
	private LocalDate dateOfBirth;

}
