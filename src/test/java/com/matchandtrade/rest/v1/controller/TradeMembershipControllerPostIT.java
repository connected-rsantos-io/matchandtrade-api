package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.rest.v1.json.TradeMembershipJson;
import com.matchandtrade.rest.v1.validator.ValidationException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeMembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeMembershipControllerPostIT {
	
	private TradeMembershipController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getTradeMembershipController();
		}
	}
	
	@Test
	public void postPositive() {
		TradeMembershipJson requestJson = tradeMembershipRandom.nextJson();
		TradeMembershipJson responseJson = fixture.post(requestJson);
		assertNotNull(responseJson.getTradeMembershipId());
	}
	
	@Test(expected=ValidationException.class)
	public void postNegativeInvalidUser() {
		TradeMembershipJson requestJson = tradeMembershipRandom.nextJson();
		requestJson.setUserId(-1);
		fixture.post(requestJson);
	}
	
	@Test(expected=ValidationException.class)
	public void postNegativeInvalidTrade() {
		TradeMembershipJson requestJson = tradeMembershipRandom.nextJson();
		requestJson.setTradeId(-1);
		fixture.post(requestJson);
	}
}