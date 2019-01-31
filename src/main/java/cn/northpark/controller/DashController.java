package cn.northpark.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import cn.northpark.model.Knowledge;
import cn.northpark.model.KnowledgeMessageBoard;
import cn.northpark.result.Result;
import cn.northpark.result.ResultGenerator;
import cn.northpark.service.KnowledgeService;
import cn.northpark.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DashController {
	
	@Autowired
	private KnowledgeService kService;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private final static int PER_PAGE_SIZE = 8;
	
	private final static String REDIS_BOOK_INDEX = "book-index";
	
	private final static String REDIS_MOOC_INDEX = "mooc-index";
	
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
	@RequestMapping(value="/archives/{id}")
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
	@RequestMapping(value="/mooc/index")
	public String moocindex(ModelMap map) {
		List<Knowledge> list =null;
		
		//从缓存取
		String mooclist = (String) redisTemplate.opsForValue().get(REDIS_MOOC_INDEX);
		if(Strings.isNullOrEmpty(mooclist)) {
			//从数据库取
			Map<String, Object> para = Maps.newHashMap();
			para.put("tags_code", "classhare");
			list = kService.selectIndexByTag(para );

		    //放到缓存
			redisTemplate.opsForValue().set(REDIS_MOOC_INDEX, JSON.toJSONString(list) ,30 ,TimeUnit.DAYS);
		}else {
			//反序列化
			list = JSON.parseArray(mooclist, Knowledge.class);
		}
		map.put("list", list);
		map.put("h1", "MOOC LIST");
		map.put("h2", "课程索引");
		return "classindex";
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value="/book/index")
	public String bookindex(ModelMap map) {
		
		List<Knowledge> list = null;
		
		//从缓存取
		
		String booklist = (String) redisTemplate.opsForValue().get(REDIS_BOOK_INDEX);
		if(Strings.isNullOrEmpty(booklist)) {
			//从数据库取
			Map<String, Object> para = Maps.newHashMap();
			para.put("tags_code", "bookshare");
		    list = kService.selectIndexByTag(para );

			//放到缓存
			redisTemplate.opsForValue().set(REDIS_BOOK_INDEX, JSON.toJSONString(list) ,30 ,TimeUnit.DAYS);
		}else {
			//反序列化
			list = JSON.parseArray(booklist, Knowledge.class);
		}
		map.put("list", list);
		map.put("h1", "IT Books List");
		map.put("h2", "书籍索引");
		return "classindex";
	}
	
	
	/**
	 * @return
	 */
	@RequestMapping(value="/search/{word}")
	public String search(ModelMap map,@PathVariable String word) {
		//校验
		Preconditions.checkNotNull(word);
		Preconditions.checkArgument(
				  !(word.contains("insert")
				||word.contains("update")
				||word.contains("delete")
				||word.contains("drop" )
				||word.contains("create")
				||word.contains("rename")
				||word.contains("truncate")
				||word.contains("alter")
				||word.contains("exists")
				||word.contains("master")
				||word.contains("restore")
				||word.contains("where")),
				
				"臭傻逼你想干什么？");

		Map<String, Object> para = Maps.newHashMap();
		para.put("word", word);
		List<Knowledge> list = kService.selectByCondition(para );

		map.put("list", list);
		return "index";
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
	/**
	 * @return
	 */
	@RequestMapping(value="/mail/contactMe")
	@ResponseBody
	public Result<String> contactMe(HttpServletRequest request) {
		String name    = request.getParameter("name");
		String phone   = request.getParameter("phone");
		String email   = request.getParameter("email");
		String message = request.getParameter("message");
		KnowledgeMessageBoard km = new KnowledgeMessageBoard();
		km.setEmail(email);
		km.setMessage(message);
		km.setName(name);
		km.setPhone(phone);
		km.setPostDate(TimeUtils.nowdate());
		kService.addKnowledgeMessageBoard(km);
		return ResultGenerator.genSuccessResult("ok");
	}
	
	
	
	
}