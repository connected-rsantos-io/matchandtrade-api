package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.rest.v1.json.search.Recipe;
import com.matchandtrade.rest.v1.json.search.SearchCriteriaJson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserValidatorUT {
	private UserEntity existingAuthenticatedUser;
	private UserValidator fixture;
	@Mock
	private UserService mockUserService;

	@Before
	public void before() {
		fixture = new UserValidator();
		existingAuthenticatedUser = new UserEntity();
		existingAuthenticatedUser.setUserId(1);
		existingAuthenticatedUser.setEmail("existing-authenticated-user@test.com");
		when(mockUserService.find(1)).thenReturn(existingAuthenticatedUser);
		UserEntity existingUnrelatedUser = new UserEntity();
		existingUnrelatedUser.setUserId(2);
		existingUnrelatedUser.setEmail("existing-unrelated-user@test.com");
		when(mockUserService.find(2)).thenReturn(existingUnrelatedUser);
		fixture.userService = mockUserService;
	}

	@Test(expected = RestException.class)
	public void validatePut_When_AuthenticatedUserIsNotSameAsGiveUser_Then_Forbidden() {
		UserJson givenUser = new UserJson();
		givenUser.setUserId(2);
		givenUser.setEmail("another-user@test.com");
		try {
			fixture.validatePut(existingAuthenticatedUser, givenUser);
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			assertEquals("User.userId is not reference the authenticated user", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePut_When_AuthenticatedUserIsSameAsGiveUserButEmailIsDifferent_Then_Forbidden() {
		UserJson givenUser = new UserJson();
		givenUser.setUserId(1);
		givenUser.setEmail("different-email@test.com");
		try {
			fixture.validatePut(existingAuthenticatedUser, givenUser);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("User.email cannot be updated", e.getDescription());
			throw e;
		}
	}

	@Test
	public void validatePut_When_AuthenticatedUserIsSameAsGivenUserAndEmailDidNotChange_Then_Succeeds() {
		UserJson givenUser = new UserJson();
		givenUser.setUserId(existingAuthenticatedUser.getUserId());
		givenUser.setEmail(existingAuthenticatedUser.getEmail());
		fixture.validatePut(existingAuthenticatedUser, givenUser);
	}
}