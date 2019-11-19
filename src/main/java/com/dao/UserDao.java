package com.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.User;

/**
 * 用户表接口
 * @author ljh
 */
@Repository
public interface UserDao extends BaseMapper<User> {
	
	/**
	 * 获取用户列表
	 * @param user 查询条件
	 * @return
	 */
	List<User> selectUserList(User user);

	/**
     * 根据用户名称获取用户详情
     * @param loginName 用户名称
     * @return
     */
	User getUserByName(String loginName);
}