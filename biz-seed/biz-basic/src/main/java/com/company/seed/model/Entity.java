package com.company.seed.model;

import java.io.Serializable;

/**
 * 实体基类(ID的类型可能为String/Integer)
 * Created by yoara on 2015/12/21.
 */
public class Entity<T> implements Serializable, Comparable<Entity<T>> {
	private static final long serialVersionUID = 1L;
	private T id;
	public T getId() {
		return id;
	}
	public void setId(T id) {
		this.id = id;
	}
	public int compareTo(Entity<T> o) {
		return 0;
	}
	
}
