package com.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.UserRoleDao;
import com.entity.Role;
import com.entity.UserRole;
import com.service.UserRoleService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户角色表Service实现
 * @author ljh
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements UserRoleService {

	@Autowired
	private UserRoleDao userRoleDao;
	
	/**
	 * 用户分配角色
	 * @param userRoleList
	 */
	@Override
	public void asignUserRoles(List<UserRole> userRoleList) {
		userRoleDao.deleteUserRoles(userRoleList.get(0).getUid());
		userRoleDao.asignUserRoles(userRoleList);
	}
	
	/**
	 * 获取用户拥有的角色
	 * @param id 用户id
	 * @return
	 */
	@Override
	public List<Role> getRoleListByUserId(String id) {
		return userRoleDao.getRoleListByUserId(id);
	}

	/**
	 * 删除用户角色
	 * @param userId 用户id
	 */
	@Override
	public void deleteUserRoles(String userId) {
		userRoleDao.deleteUserRoles(userId);
	}
	
}
