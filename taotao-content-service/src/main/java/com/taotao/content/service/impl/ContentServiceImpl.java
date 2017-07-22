package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired 
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;
	@Override
	public TaotaoResult addContent(TbContent content) {
	
		content.setCreated(new Date());
		content.setUpdated(new Date());
		
		contentMapper.insert(content);
		return TaotaoResult.ok();
	}


	@Override
	public List<TbContent> getContentByCid(long id) {
		
		try {
			String json = jedisClient.hget(INDEX_CONTENT, id+"");
			if(StringUtils.isNotEmpty(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(id);
		
		List<TbContent> list = contentMapper.selectByExample(example);
		
		try {
			jedisClient.hset("INDEX_CONTENT", id + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
