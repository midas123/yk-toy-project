package com.yk.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yk.web.KeywordFomatter;
import com.yk.web.dao.ItemIndexRepository;
import com.yk.web.dao.ItemRepository;
import com.yk.web.dto.ItemRequestDto;
import com.yk.web.entity.ItemIndexes;
import com.yk.web.entity.Items;

@Service
public class ItemService {
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	ItemIndexRepository itemIndexRepository;
	
	@Autowired
	KeywordFomatter keywordFomatter;
	
	public List<Items> searchArticle(ItemRequestDto dto){
		List<String> keywords = keywordFomatter.listingKeyword(dto);
		List<ItemIndexes> itemIndexes1R = itemIndexRepository.findByTokensContaining(keywords.get(0));
		List<ItemIndexes> itemIndexes2R = new ArrayList<>();
		List<Items> searchResult = new ArrayList<>();
		
		if(keywords.size()>1) {
			itemIndexes2R = keywordFomatter.searchByKeywordsAndToken(keywords, itemIndexes1R);
			searchResult = findItemByIndexes(itemIndexes2R);
		} else {
			searchResult = findItemByIndexes(itemIndexes1R);
		}
		return searchResult;
	}
	
	private List<Items> findItemByIndexes(List<ItemIndexes> itemIndexes){
		List<Items> searchResult = new ArrayList<>();
		
		for(int i=0; i<itemIndexes.size(); i++) {
			Items item = itemRepository.findByItemId(itemIndexes.get(i).getItem().getItemId());
			searchResult.add(item);
		}
		
		return searchResult;
	}
}
