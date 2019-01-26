package cn.northpark.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import cn.northpark.model.Knowledge;
import cn.northpark.service.KnowledgeService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DashController {
	
	@Autowired
	private KnowledgeService kService;
	
	private final static int PER_PAGE_SIZE = 8;
	
	
	/**
	 * @return
	 */
	@RequestMapping(value="/")
	public String indexPage(ModelMap map,HttpServletRequest request) {
		
		PageHelper.startPage(1, PER_PAGE_SIZE);
		List<Knowledge> all = kService.selectAll();
		PageInfo<Knowledge> pageinfo = new PageInfo<>(all);
		
		log.info(JSON.toJSONString(pageinfo));
		map.put("list", pageinfo.getList());
		map.put("pageinfo", pageinfo);
		map.put("uri", "/index");
		return "index";
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value="/index/{page}")
	public String indexPage(ModelMap map,HttpServletRequest request,@PathVariable String page) {
		
		//校验
		Preconditions.checkNotNull(page);
		Preconditions.checkArgument(Integer.valueOf(page)>0, "url注入: index/%s",page);
		
		//获取page页
		Integer pageNum = 1;
		if(!Strings.isNullOrEmpty(page)) pageNum = Integer.valueOf(page);
		
		PageHelper.startPage(pageNum, PER_PAGE_SIZE);
		List<Knowledge> all = kService.selectAll();
		PageInfo<Knowledge> pageinfo = new PageInfo<>(all);
		
		log.info(JSON.toJSONString(pageinfo));
		map.put("list", pageinfo.getList());
		map.put("pageinfo", pageinfo);
		map.put("uri", "/index");
		
		return "index";
	}
	
	
	
	/**
	 * 分类首页
	 * @param map
	 * @param request
	 * @param tagscode
	 * @return
	 */
	@RequestMapping(value="/category/{tagscode}")
	public String category(ModelMap map,HttpServletRequest request,@PathVariable String tagscode) {
		
		//校验
		Preconditions.checkNotNull(tagscode);
		
		//获取page页
		PageHelper.startPage(1, PER_PAGE_SIZE);
		Map<String, Object> para = Maps.newHashMap();
		para.put("tags_code", tagscode);
		List<Knowledge> all = kService.selectByCondition(para );
		PageInfo<Knowledge> pageinfo = new PageInfo<>(all);
		
		log.info(JSON.toJSONString(pageinfo));
		map.put("list", pageinfo.getList());
		map.put("pageinfo", pageinfo);
		map.put("uri", "/category/"+tagscode);
		
		return "index";
	}
	
	/**
	 * 
	 * 分类-分页
	 * @param map
	 * @param request
	 * @param tagscode
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/category/{tagscode}/{page}")
	public String categoryPage(ModelMap map,HttpServletRequest request,@PathVariable String tagscode,@PathVariable String page) {
		
		//校验
		Preconditions.checkNotNull(tagscode);
		Preconditions.checkArgument(Integer.valueOf(page)>0, "url注入: category/%s/%s",tagscode,page);
		//获取page页
		Integer pageNum = 1;
		if(!Strings.isNullOrEmpty(page)) pageNum = Integer.valueOf(page);
		
		PageHelper.startPage(pageNum, PER_PAGE_SIZE);
		
		Map<String, Object> para = Maps.newHashMap();
		para.put("tags_code", tagscode);
		List<Knowledge> all = kService.selectByCondition(para );
		PageInfo<Knowledge> pageinfo = new PageInfo<>(all);
		
		log.info(JSON.toJSONString(pageinfo));
		map.put("list", pageinfo.getList());
		map.put("pageinfo", pageinfo);
		map.put("uri", "/category/"+tagscode);
		
		return "index";
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value="/archives/{id}.html")
	public String postPage(ModelMap map,@PathVariable String id) {
		

		//校验
		Preconditions.checkNotNull(id);
		Preconditions.checkArgument(Integer.valueOf(id)>0, "url注入: post/%s",id);
		
		Knowledge model = kService.selectByPrimaryKey(Integer.valueOf(id));
		
		Preconditions.checkNotNull(model,"错误的请求文章");

		AtomicLong viewtime = new AtomicLong(model.getViewTimes());
		
		model.setViewTimes(viewtime.incrementAndGet());
		
		kService.update(model);
		

		
		map.put("post", model);
		
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