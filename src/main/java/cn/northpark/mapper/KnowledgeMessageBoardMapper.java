package cn.northpark.mapper;

import java.util.List;

import cn.northpark.model.KnowledgeMessageBoard;

public interface KnowledgeMessageBoardMapper {
    int insert(KnowledgeMessageBoard record);

    List<KnowledgeMessageBoard> selectAll();
}