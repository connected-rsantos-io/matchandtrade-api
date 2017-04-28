package com.matchandtrade.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.dao.TradeDao;
import com.matchandtrade.persistence.dao.TradeMembershipDao;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;

@Repository
public class TradeRepository {

	@Autowired
	private TradeDao tradeDao;
	
	@Autowired
	private TradeMembershipDao tradeMembershipDao;

	@Transactional
	public TradeEntity get(Integer tradeId) {
		return tradeDao.get(TradeEntity.class, tradeId);
	}

	@Transactional
	public void save(TradeEntity entity) {
		tradeDao.save(entity);
	}
	
	@Transactional
	public SearchResult<TradeEntity> search(SearchCriteria searchCriteria) {
		return tradeDao.search(searchCriteria);
	}

	@Transactional
	public void delete(Integer tradeId) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(TradeMembershipEntity.Field.tradeId, tradeId);
		SearchResult<TradeMembershipEntity> sr = tradeMembershipDao.search(searchCriteria);
		for (TradeMembershipEntity tm : sr.getResultList()) {
			tradeMembershipDao.delete(tm);
		}
		TradeEntity t = get(tradeId);
		tradeDao.delete(t);
	}
}
