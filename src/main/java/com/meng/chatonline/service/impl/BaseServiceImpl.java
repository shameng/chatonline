package com.meng.chatonline.service.impl;

import com.meng.chatonline.dao.BaseDao;
import com.meng.chatonline.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

public abstract class BaseServiceImpl<T> implements BaseService<T>
{

	private BaseDao<T> dao;
	
	private Class<T> clazz;
	
	@SuppressWarnings("unchecked")
	public BaseServiceImpl()
	{
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];
	}
	
	//！！！不能在定义字段上使用该注释，因为需要子类覆盖该 set 方法
	@Resource
	public void setDao(BaseDao<T> baseDao) {
		this.dao = baseDao;
	}

	@Transactional
	public void saveEntity(T t) {
		dao.saveEntity(t);
	}

	@Transactional
	public void updateEntity(T t) {
		dao.updateEntity(t);
	}

	@Transactional
	public T saveOrUpdateEntity(T t) {
		return dao.saveOrUpdateEntity(t);
	}

	@Transactional
	public void deleteEntity(T t) {
		dao.deleteEntity(t);
	}

	@Transactional
	public void BatchEntityByJPQL(String jpql, Object... objects) {
		dao.BatchEntityByJPQL(jpql, objects);
	}
	
	//执行sql语句
	@Transactional
	public void executeSql(String sql, Object... objects)
	{
		dao.executeSql(sql, objects);
	}

	@Transactional
	public T loadEntity(Integer id) {
		return dao.loadEntity(id);
	}

	@Transactional
	public T getEntity(Integer id) {
		return dao.getEntity(id);
	}

//	@Transactional
	public List<T> findEntityByJPQL(String jpql, Object... objects) {
		return dao.findEntityByJPQL(jpql, objects);
	}
	
	//单值检索，要确保查询结果有且只有一条记录
//	@Transactional
	public Object uniqueResult(String jpql, Object... objects)
	{
		return dao.uniqueResult(jpql, objects);
	}
	
	//查找所有的实体
//	@Transactional
	public List<T> findAllEntities()
	{
		String jpql = "from " + clazz.getSimpleName();
		return this.findEntityByJPQL(jpql);
	}
	
	//执行sql语句
//	@Transactional
	public <R> List<R> executeSQLQuery(Class<R> clazz, String sql, Object...objects)
	{
		return dao.executeSQLQuery(clazz, sql, objects);
	}

}
