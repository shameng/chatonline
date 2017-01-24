package com.meng.chatonline.service;

import java.util.List;

/**
 * BaseService 接口
 */
public interface BaseService<T> {
	//写操作
	public void saveEntity(T t);
	public void updateEntity(T t);
	public T saveOrUpdateEntity(T t);
	public void deleteEntity(T t);
	public void BatchEntityByJPQL(String jpql, Object... objects);
	//执行sql语句
	public void executeSql(String sql, Object... objects);
		
	//读操作
	public T loadEntity(Integer id);
	public T getEntity(Integer id);
	public List<T> findEntityByJPQL(String jpql, Object... objects);
	//单值检索，要确保查询结果有且只有一条记录
	public Object uniqueResult(String jpql, Object... objects);
	//查找所有的实体
	public List<T> findAllEntities();
	//执行sql语句，可以通过制定clazz是否为空指定是否封装为实体
	public <R> List<R> executeSQLQuery(Class<R> clazz, String sql, Object...objects);
}
