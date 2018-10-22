package com.matchandtrade.rest.v1.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ArticleControllerPostIT.class,
	ArticleControllerGetIT.class,
	ArticleControllerPutIT.class,
	ArticleControllerDeleteIT.class,
	MembershipArticleControllerPostIT.class,
	MembershipArticleControllerDeleteIT.class
})
public class ArticleSuite {

}
