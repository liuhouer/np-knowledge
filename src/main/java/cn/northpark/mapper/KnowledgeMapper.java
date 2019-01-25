package cn.northpark.mapper;

import java.util.List;
import java.util.Map;

import cn.northpark.model.Knowledge;

public interface KnowledgeMapper {
    int insert(Knowledge record);

    List<Knowledge> selectAll();
    
    int countByRetcode(String retcode);

	List<Knowledge> selectByCondition(Map<String, Object> para);
}