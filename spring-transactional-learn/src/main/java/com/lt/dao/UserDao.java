package com.lt.dao;

import com.lt.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liangtao
 * @description
 * @Date 2021/7/12
 **/
@Mapper
public interface UserDao {
    List<User> list();

    void insert(User user);

    void updateById(User user);

    User getByName(@Param("name") String name);

    void deleteByName(@Param("name")String name);

    void updateByName(User user);
}
