package cn.northpark.mapper;

import cn.northpark.model.KnowledgeTest;
import java.util.List;

public interface KnowledgeTestMapper {
    int insert(KnowledgeTest record);

    List<KnowledgeTest> selectAll();
}