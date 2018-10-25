package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.rest.v1.validator.ArticleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/matchandtrade-api/v1")
public class ArticleController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private ArticleValidator articleValidator;

	@RequestMapping(path="/articles/{articleId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("articleId") Integer articleId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		articleValidator.validateDelete(authenticationProvider.getAuthentication().getUser().getUserId(), articleId);
		// Delegate to service layer
		articleService.delete(articleId);
	}

	@RequestMapping(path = "/articles", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ArticleJson post(@RequestBody ArticleJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		articleValidator.validatePost(authenticationProvider.getAuthentication().getUser().getUserId(), requestJson);
		// Transform the request
		ArticleEntity articleEntity = ArticleTransformer.transform(requestJson);
		// Delegate to service layer
		articleService.create(authenticationProvider.getAuthentication().getUser(), articleEntity);
		// Transform the response
		ArticleJson response = ArticleTransformer.transform(articleEntity);
		// Assemble links
//		ArticleLinkAssember.assemble(response, membershipId);
		return response;
	}

	@RequestMapping(path = "/articles/{articleId}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public ArticleJson put(@PathVariable Integer articleId, @RequestBody ArticleJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		requestJson.setArticleId(articleId); // Always get the id from the URL when working on PUT methods
		articleValidator.validatePut(authenticationProvider.getAuthentication().getUser().getUserId(), requestJson);
		// Transform the request
		ArticleEntity articleEntity = ArticleTransformer.transform(requestJson);
		// Delegate to service layer
		articleService.update(articleEntity);
		// Transform the response
		ArticleJson response = ArticleTransformer.transform(articleEntity);
		// Assemble links
//		ArticleLinkAssember.assemble(response, membershipId);
		return response;
	}

	@RequestMapping(path="/articles/{articleId}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ArticleJson get(@PathVariable("articleId") Integer articleId) {
		// Validate the request
		articleValidator.validateGet(articleId);
		// Delegate to service layer
		ArticleEntity articleEntity = articleService.get(articleId);
		// Transform the response
		ArticleJson response = ArticleTransformer.transform(articleEntity);
		// Assemble links
//		ArticleLinkAssember.assemble(response, membershipId);
		return response;
	}

}