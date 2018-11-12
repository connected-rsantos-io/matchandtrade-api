package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleServiceIT {

	@Autowired
	private ArticleService fixture;
	@Autowired
	private ArticleRandom articleRandom;

	/**
	 * Alert, this test is not tread save.
	 * In a multi-threaded environment, when asserting results, assertion {@code assertTrue(actual.getResultList().contains(article))}
	 * might fail if other tests inserted many articles in the data storage; resulting in a false positive.
	 */
	@Test
	public void get_When_ThereIsOneArticleForTheUser_Then_ReturnsOneArticle() {
		// Setting a long page size to
		int pageSize = 50;
		int startingTotal = (int) fixture.search(1, pageSize).getPagination().getTotal();
		ArticleEntity article = articleRandom.createPersistedEntity();
		SearchResult<ArticleEntity> actual = fixture.search((startingTotal/pageSize)+1, pageSize);
		assertTrue(actual.getPagination().getTotal() > startingTotal); // This assertion should e enough for most cases
		assertTrue(actual.getResultList().contains(article));
	}

}
