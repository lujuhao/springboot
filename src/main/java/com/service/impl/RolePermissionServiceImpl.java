package com.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.PermissionDao;
import com.dao.RolePermissionDao;
import com.entity.Permission;
import com.entity.RolePermission;
import com.service.RolePermissionService;
import com.utils.RandomUtil;
import com.vo.ZTreeNodes;

/**
 * 角色权限表Service实现
 * @author ljh
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionDao, RolePermission> implements RolePermissionService {

	@Autowired
	private RolePermissionDao rolePermissionDao;
	
	@Autowired
	private PermissionDao permissionDao;
	
	
	/**
	 * 删除角色权限
	 * @param id 角色id
	 */
	@Override
	public void deleteByRoleId(String id) {
		rolePermissionDao.deleteByRoleId(id);
	}

	/**
     * 获取角色权限zTree数据
     * @param id 角色id
     * @return
     */
	@Override
	public List<ZTreeNodes> getRolePermissionsForzTree(String id) {
		List<ZTreeNodes> zTreeNodes = new ArrayList<ZTreeNodes>();
		
		List<Permission> allPermissions = permissionDao.selectList(null); //全部权限
		List<Permission> rolePermissions = rolePermissionDao.getRolePermissions(id); //角色拥有的权限
		allPermissions.forEach(permission -> {
			ZTreeNodes nodes = new ZTreeNodes();
			nodes.setId(permission.getId());
			nodes.setPid(permission.getPid());
			nodes.setParent(permission.getPid().equals("0")?true:false);
			nodes.setName(permission.getName());
			nodes.setOpen(true);
			rolePermissions.forEach(rolePermission -> {
				if (permission.getId().equals(rolePermission.getId())) {
					nodes.setChecked(true);
				}
			});
			zTreeNodes.add(nodes);
		});
		return zTreeNodes;
	}

	/**
     * 获取角色权限
     * @param id 角色id
     * @return
     */
	@Override
	public List<Permission> getRolePermissions(String id) {
		return rolePermissionDao.getRolePermissions(id);
	}
	
	/**
     * 分配角色权限
     * @param roleId 角色id
     * @param rolePermissions 角色权限
     * @return
     */
	@Override
	@Transactional
	public void asignRolePermissions(String roleId, List<RolePermission> rolePermissions) {
		deleteByRoleId(roleId);
			
		rolePermissions.forEach(rolePermission -> {
			rolePermission.setId(RandomUtil.getUUID());
			rolePermission.setGmtCreate(new Date());
			rolePermissionDao.insert(rolePermission);
		});
	}
	
}
