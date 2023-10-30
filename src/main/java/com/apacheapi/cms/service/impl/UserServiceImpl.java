package com.apacheapi.cms.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import com.apacheapi.cms.dto.UserRequestDto;
import com.apacheapi.cms.dto.UserResponseDto;
import com.apacheapi.cms.dto.UserUpdateDto;
import com.apacheapi.cms.entity.User001;
import com.apacheapi.cms.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final ObjectContext objectContext;

	@Override
	public void saveUser(UserRequestDto userRequestDto) {

		// STEP 1: First check in database if user exist for the username or not
		var response = findByUsername(userRequestDto.getUsername());

		// STEP 2: If user exist then throw exception user can't created as, already
		// exist
		if (!CollectionUtils.isEmpty(response)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already present");
		}

		// STEP 3: Now no user exist so creating new entity
		User001 user = this.objectContext.newObject(User001.class);

		user.setUsername(userRequestDto.getUsername());
		user.setPassword(userRequestDto.getPassword());
		user.setEmail(userRequestDto.getEmail());
		user.setGender(userRequestDto.getGender());
		user.setMobile(userRequestDto.getMobile());
		user.setDateOfBirth(userRequestDto.getDateOfBirth());

		// STEp 4: commit all the changes
		this.objectContext.commitChanges();

	}

	@Override
	public List<UserResponseDto> getAllUsers() {
		List<User001> users = ObjectSelect.query(User001.class).select(objectContext);
		return users.stream()
				.map(user -> {
					var dto = new UserResponseDto();
					dto.setUsername(user.getUsername());
					dto.setPassword(user.getPassword());
					dto.setMobile(user.getMobile());
					dto.setGender(user.getGender());
					dto.setEmail(user.getEmail());
					dto.setDateOfBirth(user.getDateOfBirth());

					return dto;
				})

				.collect(Collectors.toList());
	}

	@Override
	public void updateUser(String username, UserUpdateDto userUpdateDto) {

		try {

			// STEP 1: Get the user from the database via its username
			User001 entity = ObjectSelect
					.query(User001.class)
					.where(User001.USERNAME.eq(username))
					.selectOne(objectContext);// selectOne method can cause an exception if more than 1 record is
												// present

			// STEP 2: If the entity is null then throw an exception that user not found
			if (Objects.isNull(entity)) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
			}

			// STEP 3: Check every value if not null then update in the entity
			if (isNotBlank(userUpdateDto.getEmail())) {
				entity.setEmail(userUpdateDto.getEmail());
			}
			if (isNotBlank(userUpdateDto.getMobile())) {
				entity.setMobile(userUpdateDto.getMobile());
			}
			if (isNotBlank(userUpdateDto.getPassword())) {
				entity.setPassword(userUpdateDto.getPassword());
			}

			// STEP 4: Commit the transaction
			objectContext.commitChanges();

		} catch (CayenneRuntimeException ex) {
			log.error("Error while updating user", ex);
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Exception while updated");
		}

	}

	private List<User001> findByUsername(String username) {

		if (isBlank(username)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username required");
		}

		return ObjectSelect
				.query(User001.class)
				.where(User001.USERNAME.eq(username))
				.select(objectContext);
	}

	private boolean isNotBlank(String value) {
		return Objects.nonNull(value) && value.trim().length() > 0;
	}

	private boolean isBlank(String value) {
		return !isNotBlank(value);
	}

}
