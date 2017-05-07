package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.repository.ItemRepository;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.ItemService;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.rest.v1.transformer.ItemTransformer;
import com.matchandtrade.rest.v1.validator.ItemValidator;

@RestController
@RequestMapping(path = "/rest/v1/trade-memberships")
public class ItemController {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	ItemValidator itemValidator;
	@Autowired
	TradeMembershipRepository tradeMembershipRepository;
	@Autowired
	ItemTransformer itemTransformer;
	
	@Autowired
	ItemService itemService;

	@RequestMapping(path = "/{tradeMembershipId}/items", method = RequestMethod.POST)
	public ItemJson post(@PathVariable Integer tradeMembershipId, @RequestBody ItemJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		itemValidator.validatePost(authenticationProvider.getAuthentication().getUser().getUserId(), tradeMembershipId, requestJson);
		// Transform the request
		ItemEntity itemEntity = ItemTransformer.transform(requestJson);
		// Delegate to Service layer
		itemService.create(tradeMembershipId, itemEntity);
		return ItemTransformer.transform(itemEntity);
	}

}