package cn.northpark.mapper;

import cn.northpark.model.KnowledgeMessageBoard;
import java.util.List;

public interface KnowledgeMessageBoardMapper {
    int insert(KnowledgeMessageBoard record);

    List<KnowledgeMessageBoard> selectAll();
}