package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.ListingService;
import com.matchandtrade.rest.v1.json.ListingJson;
import com.matchandtrade.rest.v1.validator.ListingValidator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/matchandtrade-api/v1/listing")
public class ListingController {
	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	private ListingValidator listingValidator;
	@Autowired
	private ListingService listingService;

	@RequestMapping(path="/", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void post(ListingJson request) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		listingValidator.validatePost(authenticationProvider.getAuthentication().getUser().getUserId(), request);
		// Delegate to service layer
		listingService.create(request.getMebershipId(), request.getArticleId());
		// TODO add heteroas
	}

	@RequestMapping(path="/{membershipId}/{articleId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(ListingJson request) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		listingValidator.validateDelete(authenticationProvider.getAuthentication().getUser().getUserId(), request);
		// Delegate to service layer
		listingService.delete(request.getMebershipId(), request.getArticleId());
		// TODO add heteroas
	}

}