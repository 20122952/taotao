package com.taotao.service.impl;

import java.util.Date;

import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

/**
 * 商品管理Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name = "itemAddtopic")
	private Destination destination;
	
	@Override
	public TbItem getItemById(long itemId) {
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		return item;
	}


	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		
		return result;
	}


	@Override
	public TaotaoResult addItem(TbItem item, String desc) {
		// 1、生成商品id
		final long itemId = IDUtils.genItemId();
		
		// 2、补全TbItem对象的属性
		item.setId(itemId);
		
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		
		// 3、向商品表插入数据
		itemMapper.insert(item);
		
		// 4、创建一个TbItemDesc对象
		TbItemDesc itemDesc = new TbItemDesc();
		
		// 5、补全TbItemDesc的属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
			
		// 6、向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
		
		//向activemq发消息
		jmsTemplate.send(destination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId +"");
				return textMessage;
			}
		});
		
		// 7、TaotaoResult.ok()
		
		return TaotaoResult.ok();
	}


	@Override
	public TbItemDesc getItemDescById(long itemId) {
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		return itemDesc;
	}

	

}
