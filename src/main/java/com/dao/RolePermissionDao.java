package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.Permission;
import com.entity.RolePermission;

/**
 * 角色权限表接口
 * @author ljh
 */
@Repository
public interface RolePermissionDao extends BaseMapper<RolePermission> {

	/**
	 * 删除角色权限
	 * @param id 角色id
	 */
	void deleteByRoleId(@Param("id")String id);
	
	/**
	 * 获取角色权限
	 * @param roleId 角色id
	 * @return
	 */
	List<Permission> getRolePermissions(@Param("id")String id);

}