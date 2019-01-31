package cn.northpark.service;

import java.util.List;
import java.util.Map;

import cn.northpark.model.Knowledge;
import cn.northpark.model.KnowledgeMessageBoard;

public interface KnowledgeService {
	
    int insert(Knowledge record);

    List<Knowledge> selectAll();

    List<Knowledge> selectByCondition(Map<String,Object> para);

	Knowledge selectByPrimaryKey(Integer valueOf);

	int update(Knowledge model);

	List<Knowledge> selectIndexByTag(Map<String, Object> para);

	int addKnowledgeMessageBoard(KnowledgeMessageBoard km);
    
}