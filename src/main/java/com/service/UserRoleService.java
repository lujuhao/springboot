package com.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.entity.Role;
import com.entity.UserRole;

/**
 * 用户角色表Service
 * @author ljh
 */
public interface UserRoleService extends IService<UserRole> {

	/**
	 * 用户分配角色
	 * @param userRoleList
	 */
	void asignUserRoles(List<UserRole> userRoleList);
	
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
