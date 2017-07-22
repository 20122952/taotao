package com.taotao.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;

@Repository
public class SearchDao {
	@Autowired
	private SolrServer solrServer;
	
	public SearchResult search(SolrQuery  query)throws Exception{
		
		QueryResponse response = solrServer.query(query);
		SolrDocumentList solrDocumentList = response.getResults();
		List<SearchItem> itemList = new ArrayList<>();
		
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItem item = new SearchItem();
			item.setId((String) solrDocument.get("id"));
			item.setCategory_name((String) solrDocument.get("item_category_name"));
			
			
			String image = (String) solrDocument.get("item_image");
			if(StringUtils.isNotBlank(image)){
				image = image.split(",")[0];
			}
			
			item.setImage(image);
			item.setPrice((long) solrDocument.get("item_price"));
			item.setSell_point((String) solrDocument.get("item_sell_point"));

			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle = "";
			//有高亮显示的内容时。
			if (list != null && list.size() > 0) {
				itemTitle = list.get(0);
			} else {
				itemTitle = (String) solrDocument.get("item_title");
			}
			item.setTitle(itemTitle);
			//添加到商品列表
			itemList.add(item);
		}
		SearchResult result = new SearchResult();
		//商品列表
		result.setItemList(itemList);
		//总记录数
		result.setRecordCount(solrDocumentList.getNumFound());
		
		return result;

		
	}

}
