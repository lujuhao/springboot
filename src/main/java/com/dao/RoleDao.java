package com.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.Role;

/**
 * 角色表接口
 * @author ljh
 */
@Repository
public interface RoleDao extends BaseMapper<Role> {

	/**
	 * 获取全部角色
	 * @return
	 */
	List<Role> selectAllRole();

}