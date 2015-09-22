package com.netease.vcloud.service;

public interface BaseService<T> {

	public T save(T o) throws ServiceException;

	public T update(T o) throws ServiceException;

	public T findById(String uid);

	public void deleteById(String uid) throws ServiceException;

}
