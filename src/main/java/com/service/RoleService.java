package com.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.entity.Role;
import com.vo.Page;

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
     * 分页查询角色列表
     * @param page 分页信息
     * @param role 过滤条件
     * @return
     */
	Page<Role> selectRoleByPage(Page<Role> page, Role role);

	/**
	 * 根据名称获取角色
	 * @param name
	 * @return
	 */
	Role getRoleByName(String name);

	/**
	 * 创建角色
	 * @param role
	 */
	void addRole(Role role);
	
	/**
     * 删除角色
     * @param id
     * @return
     */
	void deleteRoleById(String id);
}
