package com.meng.chatonline.dao.impl;

import com.meng.chatonline.dao.BaseDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 抽象的 DAO ，专门用于继承
 */
public abstract class BaseDaoImpl<T> implements BaseDao<T>
{

	//通过 @PersistenceContext 注解获取与当前事务关联的 entityManager
	@PersistenceContext
	private EntityManager entityManager;
	
	private Class<T> clazz;
	
	@SuppressWarnings("unchecked")
	public BaseDaoImpl()
	{
		//得到泛型化超类
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];
	}

	public void saveEntity(T t) {
		entityManager.persist(t);
	}

	public void updateEntity(T t) {
		entityManager.merge(t);
	}

	public T saveOrUpdateEntity(T t) {
		return entityManager.merge(t);
	}

	public void deleteEntity(T t) {
		entityManager.remove(t);
	}

	public void BatchEntityByJPQL(String jpql, Object... objects) {
		Query query = entityManager.createQuery(jpql);
		//JPA设置参数从 1 开始
		for(int i=1; i<=objects.length; i++)
		{
			query.setParameter(i, objects[i-1]);
		}
		query.executeUpdate();
	}

	public void executeSql(String sql, Object...objects)
	{
		Query query = entityManager.createNativeQuery(sql);
		//JPA设置参数从 1 开始
		for(int i=1; i<=objects.length; i++)
		{
			query.setParameter(i, objects[i-1]);
		}
		query.executeUpdate();
	}

	//类似于hibernate的load方法,延迟加载.没相应数据时会出现异常,真正调用时才查找数据
	public T loadEntity(Integer id) {
		return (T) entityManager.getReference(clazz, id);
	}

	//类似于hibernate的get方法,没找到数据时，返回null
	public T getEntity(Integer id) {
		return (T) entityManager.find(clazz, id);
	}

	public List<T> findEntityByJPQL(String jpql, Object... objects) {
		Query query = entityManager.createQuery(jpql);
		//JPA设置参数从 1 开始
		for(int i=1; i<=objects.length; i++)
		{
			query.setParameter(i, objects[i-1]);
		}
		return query.getResultList();
	}
	
	//单值检索，要确保查询结果有且只有一条记录
	public Object uniqueResult(String jpql, Object... objects)
	{
		Query query = entityManager.createQuery(jpql);
		//JPA设置参数从 1 开始
		for(int i=1; i<=objects.length; i++)
		{
			query.setParameter(i, objects[i-1]);
		}
		return query.getSingleResult();
	}
	
	public <R> List<R> executeSQLQuery(Class<R> clazz, String sql, Object...objects)
	{
		Query query = entityManager.createNativeQuery(sql);
		//封装为实体
		if(clazz != null)
		{
			query = entityManager.createNativeQuery(sql, clazz);
		}
		//JPA设置参数从 1 开始
		for(int i=1; i<=objects.length; i++)
		{
			query.setParameter(i, objects[i-1]);
		}
		return query.getResultList();
	}

}
