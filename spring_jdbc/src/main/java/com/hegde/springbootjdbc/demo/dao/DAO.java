package com.hegde.springbootjdbc.demo.dao;

import java.util.*;

public interface DAO<T> {

    List<T> list();

    void create(T t);

    Optional<T> get(int id);

    void addRows(T t,Object o);

    void updateRows(T t, Object o);

}
