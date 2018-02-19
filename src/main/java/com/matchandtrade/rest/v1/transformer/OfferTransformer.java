package com.matchandtrade.rest.v1.transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.repository.ItemRepository;
import com.matchandtrade.rest.v1.json.OfferJson;

@Component
public class OfferTransformer {
	
	@Autowired
	private ItemRepository itemRepository;
	
	// Utility classes should not have public constructors
	private OfferTransformer() {}

	public OfferEntity transform(OfferJson json) {
		OfferEntity result = new OfferEntity();
		result.setOfferId(json.getOfferId());
		result.setOfferedItem(itemRepository.findOne(json.getOfferedItemId()));
		result.setWantedItem(itemRepository.findOne(json.getWantedItemId()));
		return result;
	}

	public static OfferJson transform(OfferEntity entity) {
		OfferJson result = new OfferJson();
		result.setOfferId(entity.getOfferId());
		result.setOfferedItemId(entity.getOfferedItem().getItemId());
		result.setWantedItemId(entity.getWantedItem().getItemId());
		return result;
	}

}