package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.Menu;
import com.entity.Permission;
import com.entity.RolePermission;
import com.vo.Page;
import com.vo.ZTreeNodes;
import java.util.List;

/**
 * 权限表Service
 * @author ljh
 */
public interface PermissionService extends IService<Permission> {

	/**
     * 分页查询权限列表
     * @param permission 过滤条件
     * @return
     */
	Page<Permission> selectPermissionByPage(Page<Permission> page, Permission permission);
	
	
    /**
     * 创建登陆用户菜单
     * @param uid
     * @return
     */
    List<Menu> createMenu(String uid);

    /**
     * 查询所有权限树形展示 并且选中角色拥有的树节点
     * @param roleId 角色ID
     * @return
     */
    List<ZTreeNodes> findPermissionZTreeNodes(Long roleId);

    /**
     * 修改角色权限
     * @param rolePermissions
     * @return
     */
    boolean modifyRolePermission(List<RolePermission> rolePermissions);

    /**
     * 删除权限 和 权限角色表数据
     * @param id
     * @return
     */
    boolean deletePermissionRole(String id);
}
