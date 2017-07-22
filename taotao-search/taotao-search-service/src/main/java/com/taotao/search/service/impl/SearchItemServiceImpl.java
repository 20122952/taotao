package com.taotao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SearchItemMapper searchItemMapper;
	
	@Autowired
	private SolrServer solrServer;
	
/*	1、查询所有商品数据。
	2、创建一个SolrServer对象。
	3、为每个商品创建一个SolrInputDocument对象。
	4、为文档添加域
	5、向索引库中添加文档。
	6、返回TaotaoResult。
*/
	@Override
	public TaotaoResult importItemsToIndex() {

		try {
			List<SearchItem> itemList = searchItemMapper.getItemList();
			for (SearchItem searchItem : itemList) {
				SolrInputDocument document = new SolrInputDocument();
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				document.addField("item_desc", searchItem.getItem_desc());
				
				solrServer.add(document);
				
			}
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, "数据导入失败");
		}
		return TaotaoResult.ok();
	}

}
