package com.matchandtrade.rest.handler;

import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.JsonLinkSupport;

@ControllerAdvice
public class RestResponseAdvice implements ResponseBodyAdvice<Object> {
	
	private class PaginationHeader {
		public String previousPage;
		public String nextPage;
		public String totalCount;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(RestResponseAdvice.class);

	/**
	 * When <i>body</i> is an instance of <i>JsonLinkSupport<i> or SearchResult<JsonLinkSupport>, then
	 * load the HATEOAS links of each JsonLinkSupport object.
	 * 
	 * Additionally, sets status code as HttpStatus.NOT_FOUND of body is null or SearchResult.getResultList().isEmpty().
	 * 
	 * @see http://projects.spring.io/spring-hateoas/
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object beforeBodyWrite(
				Object body,
				MethodParameter returnType,
				MediaType selectedContentType,
				Class<? extends HttpMessageConverter<?>> selectedConverterType,
				ServerHttpRequest request,
				ServerHttpResponse response) {
		

		// If body is null, then return HttpStatus.NOT_FOUND
		if (body == null) {
			response.setStatusCode(HttpStatus.NOT_FOUND);
			return null;
		}
		logger.debug("Processing body instance of [{}] for URI [{}].", body.getClass(), request.getURI());
		// If is a JsonLinkSupport, then build its links using Spring HATEOAS.
		if (body instanceof JsonLinkSupport) {
			((JsonLinkSupport) body).buildLinks();
			return body;
		}
		/*
		 * SearchResult is going to be serialized as an JSON array.
		 * Also, build SearchResult.getResultList() links using Spring HATEOAS.
		 * Also, is assumed that SearchResult should only be returned by Controller classes and are SearchResult<Json>. 
		 */
		// 
		if (body instanceof SearchResult) {
			// Handle SearchResult
			SearchResult<Json> searchResult = (SearchResult) body;
			handleSearchResult(searchResult, response);
			// Build pagination header
			PaginationHeader paginationHeader = buildPaginationHeader(request, searchResult);
			// Handle headers
			handlePaginationHeaders(response, paginationHeader);
			return body;
			
		}
		return body;
	}

	private PaginationHeader buildPaginationHeader(ServerHttpRequest request, SearchResult<Json> searchResult) {
		// URIBuilder for the basic URI (URL without query params)
		URIBuilder basicUriBuilder = new URIBuilder()
				.setScheme(request.getURI().getScheme())
				.setHost(request.getURI().getHost())
				.setPath(request.getURI().getPath());
		String basicUri = null;
		try {
			basicUri = basicUriBuilder.build().toString();
		} catch (URISyntaxException e) {
			logger.error("Unable to build URI.", e);
		}
		// Rebuild query parameters without _pageSize and _pageNumber
		List<NameValuePair> queryParams = URLEncodedUtils.parse(request.getURI(), "UTF-8");
		for (NameValuePair param : queryParams) {
			if (!param.getName().equals(Pagination.Parameter.SIZE.toString()) && !param.getName().equals(Pagination.Parameter.NUMBER.toString())) {
				basicUriBuilder.addParameter(param.getName(), param.getValue());
			}
		}
		// Build nextPage, previousPage query strings
		String leadingAmpersand = (queryParams.isEmpty() ? "" : "&");
		String nextPage = basicUri + leadingAmpersand + Pagination.Parameter.NUMBER + "=" + (searchResult.getPagination().getNumber() + 1);
		String previousPage = basicUri + leadingAmpersand + Pagination.Parameter.NUMBER + "=" + (searchResult.getPagination().getNumber()  - 1);
		// Build the result
		PaginationHeader result = new PaginationHeader();
		String totalCount = String.valueOf(searchResult.getPagination().getTotal());
		result.totalCount = totalCount;
		result.nextPage = nextPage;
		result.previousPage = previousPage;
		return result;
	}

	private void handlePaginationHeaders(ServerHttpResponse response, PaginationHeader paginationHeader) {
		response.getHeaders().add("X-Pagination-Total-Count", paginationHeader.totalCount);
		response.getHeaders().add("Link", paginationHeader.nextPage);
		response.getHeaders().add("Link", paginationHeader.previousPage);
	}

	private void handleSearchResult(SearchResult<Json> searchResult, ServerHttpResponse response) {
		if (searchResult.getResultList().isEmpty()) {
			response.setStatusCode(HttpStatus.NOT_FOUND);
		} else {
			for (Json j : searchResult.getResultList()) {
				if (j instanceof JsonLinkSupport) {
					JsonLinkSupport jAsJsonLinkSupport = (JsonLinkSupport) j;
					jAsJsonLinkSupport.buildLinks();
				}
			}
		}
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

}