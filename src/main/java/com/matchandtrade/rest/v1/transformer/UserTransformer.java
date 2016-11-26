package com.matchandtrade.rest.v1.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchResult;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.UserJson;

@Component
public class UserTransformer {
	
	@Autowired
	private UserModel userModel;

	public UserEntity transform(UserJson json, boolean loadEntity) {
		UserEntity result;
		if (loadEntity) {
			result = userModel.get(json.getUserId());
		} else {
			result = new UserEntity();
		}
		result.setEmail(json.getEmail());
		result.setName(json.getName());
		result.setUserId(json.getUserId());
		return result;
	}
	
	public UserEntity transform(UserJson json) {
		return transform(json, false);
	}
	
	public static UserJson transform(UserEntity entity) {
		if (entity == null) {
			return null;
		}
		UserJson result = new UserJson();
		result.setEmail(entity.getEmail());
		result.setName(entity.getName());
		result.setUserId(entity.getUserId());
		return result;
	}

	public static SearchResult<UserJson> transform(SearchResult<UserEntity> searchResult) {
		List<UserJson> resultList = new ArrayList<>();
		for(UserEntity u : searchResult.getResultList()) {
			resultList.add(transform(u));
		}
		return new SearchResult<>(resultList, searchResult.getPagination());
	}
	
}