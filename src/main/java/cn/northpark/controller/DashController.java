package cn.northpark.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.northpark.model.Knowledge;
import cn.northpark.service.KnowledgeService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DashController {
	
	@Autowired
	private KnowledgeService kService;
	
	/**
	 * @return
	 */
	@RequestMapping(value="/index.html")
	public String indexPage(ModelMap map) {
		
		PageHelper.startPage(1, 5);
		List<Knowledge> all = kService.selectAll();
		PageInfo<Knowledge> pageinfo = new PageInfo<>(all);
		
		log.info(JSON.toJSONString(pageinfo));
		map.put("list", pageinfo.getList());
		map.put("pageinfo", pageinfo);
		
		return "index";
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value="/post.html")
	public String postPage() {
		
		return "post";
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value="/contact.html")
	public String contactPage() {
		
		return "contact";
	}
	
	
	/**
	 * @return
	 */
	@RequestMapping(value="/about.html")
	public String aboutPage() {
		
		return "about";
	}
	
}