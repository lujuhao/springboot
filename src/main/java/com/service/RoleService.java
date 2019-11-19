package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.Role;
import com.entity.UserRole;

import java.util.List;

/**
 * 角色表Service
 * @author ljh
 */
public interface RoleService extends IService<Role> {

	/**
	 * 获取全部角色
	 * @return
	 */
	List<Role> selectAllRole();
	
    /**
     * 删除角色表并且删除角色权限表关联数据
     * @param id
     * @return
     */
    boolean deleteRoleByIdAndPermission(String id);

    /**
     * 分配用户角色
     * @param userRoles
     * @return
     */
    boolean modifyUserRole(List<UserRole> userRoles);
}
