package com.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.entity.Permission;
import com.entity.RolePermission;
import com.vo.ZTreeNodes;

/**
 * 角色权限表Service
 * @author ljh
 */
public interface RolePermissionService extends IService<RolePermission> {
	
	/**
	 * 删除角色权限
	 * @param id 角色id
	 */
	void deleteByRoleId(String id);

	/**
     * 获取角色权限
     * @param id 角色id
     * @return
     */
	List<ZTreeNodes> getRolePermissionsForzTree(String id);
	
	/**
     * 获取角色权限
     * @param id 角色id
     * @return
     */
	List<Permission> getRolePermissions(String id);

	/**
     * 分配角色权限
     * @param roleId 角色id
     * @param rolePermissions 角色权限
     * @return
     */
	void asignRolePermissions(String roleId, List<RolePermission> rolePermissions);
	
	
}
