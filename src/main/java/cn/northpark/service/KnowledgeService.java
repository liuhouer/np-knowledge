package cn.northpark.service;

import java.util.List;
import java.util.Map;

import cn.northpark.model.Knowledge;

public interface KnowledgeService {
	
    int insert(Knowledge record);

    List<Knowledge> selectAll();

    List<Knowledge> selectByCondition(Map<String,Object> para);
    
}