package cn.northpark.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.northpark.mapper.KnowledgeMapper;
import cn.northpark.model.Knowledge;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {

	@Autowired
	private KnowledgeMapper kmapper;
	
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

}

