package com.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;


@Controller
public class IndexController {

	@Autowired
	private ContentService contentService;
	
	
	@Value("${AD1_CATEGORY_ID}")
	private Long AD1_CATEGORY_ID;
	@Value("${AD1_WIDTH}")
	private Integer AD1_WIDTH;
	@Value("${AD1_WIDTH_B}")
	private Integer AD1_WIDTH_B;
	@Value("${AD1_HEIGHT}")
	private Integer AD1_HEIGHT;
	@Value("${AD1_HEIGHT_B}")
	private Integer AD1_HEIGHT_B;
	
	
	@RequestMapping("/index")
	public String showIndex(Model model){
		List<TbContent> contentList = contentService.getContentByCid(AD1_CATEGORY_ID);
		List<AD1Node> list = new ArrayList<>();
		
		for (TbContent tbContent : contentList) {
			AD1Node node = new AD1Node();
			node.setAlt(tbContent.getTitle());
			node.setHeight(AD1_HEIGHT);
			node.setHeightB(AD1_HEIGHT_B);
			node.setWidth(AD1_WIDTH);
			node.setWidthB(AD1_WIDTH_B);
			node.setHref(tbContent.getUrl());
			node.setSrc(tbContent.getPic());
			node.setSrcB(tbContent.getPic2());
			list.add(node);
		}
		model.addAttribute("ad1",JsonUtils.objectToJson(list));
		return "index";
	}
	
}
