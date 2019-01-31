package cn.northpark.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.northpark.mapper.KnowledgeMapper;
import cn.northpark.mapper.KnowledgeMessageBoardMapper;
import cn.northpark.model.Knowledge;
import cn.northpark.model.KnowledgeMessageBoard;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {

	@Autowired
	private KnowledgeMapper kmapper;
	
	@Autowired
	private KnowledgeMessageBoardMapper kmbmapper;
	
	@Override
	public int insert(Knowledge record) {
		// TODO Auto-generated method stub
		return kmapper.insert(record);
	}

	@Override
	public List<Knowledge> selectAll() {
		// TODO Auto-generated method stub
		return kmapper.selectAll();
	}

	@Override
	public List<Knowledge> selectByCondition(Map<String,Object> para) {
		// TODO Auto-generated method stub
		return kmapper.selectByCondition(para);
	}

	@Override
	public Knowledge selectByPrimaryKey(Integer key) {
		// TODO Auto-generated method stub
		return kmapper.selectByPrimaryKey(key);
	}

	@Override
	public int update(Knowledge model) {
		// TODO Auto-generated method stub
		return kmapper.update(model);
	}

	@Override
	public List<Knowledge> selectIndexByTag(Map<String, Object> para) {
		// TODO Auto-generated method stub
		return kmapper.selectIndexByTag(para);
	}

	@Override
	public int addKnowledgeMessageBoard(KnowledgeMessageBoard km) {
		// TODO Auto-generated method stub
		return kmbmapper.insert(km);
	}

}

