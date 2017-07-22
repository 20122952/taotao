package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

/*商品查询Controller
 * 20170212
 * 
 */

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("item/{itemId}")
	@ResponseBody
	private TbItem getItemById(@PathVariable Long itemId){
		TbItem tbItem =  itemService.getItemById(itemId);
		return tbItem;
		
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
		
	}
	
}
