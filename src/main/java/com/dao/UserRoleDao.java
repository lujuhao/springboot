package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.Role;
import com.entity.UserRole;

/**
 * 用户角色关联表接口
 * @author ljh
 */
@Repository
public interface UserRoleDao extends BaseMapper<UserRole> {

	/**
	 * 用户分配角色
	 * @param userRoleList
	 */
	void asignUserRoles(@Param("userRoleList")List<UserRole> userRoleList);
	
	/**
	 * 获取用户拥有的角色
	 * @param userId 用户id
	 * @return
	 */
	List<Role> getRoleListByUserId(String userId);

	/**
	 * 删除用户角色
	 * @param userId 用户id
	 */
	void deleteUserRoles(String userId);

}