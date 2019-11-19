package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.RoleDao;
import com.dao.RolePermissionDao;
import com.dao.UserRoleDao;
import com.entity.Role;
import com.entity.RolePermission;
import com.entity.UserRole;
import com.service.RoleService;
import com.service.UserRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色表Service实现
 * @author ljh
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

	@Autowired
    private RoleDao roleDao;
	
    @Autowired
    private RolePermissionDao rolePermissionDao;
    
    @Autowired
    private UserRoleDao  userRoleDao;
    
    @Autowired
    private UserRoleService userRoleService;

    /**
	 * 获取全部角色
	 * @return
	 */
	@Override
	public List<Role> selectAllRole() {
		return roleDao.selectAllRole();
	}
    
	/**
     * 删除角色表并且删除角色权限表关联数据
     * @param id
     * @return
     */
    @Transactional
    @Override
    public boolean deleteRoleByIdAndPermission(String id) {
        // 拼接角色表删除list
        String[] split = id.split(",");
        List<String> userId = new ArrayList<>();
        for (String item:split) {
            userId.add(item);
        }
        // 删除角色表
        baseMapper.deleteBatchIds(userId);

        // 删除角权限关联表数据
        EntityWrapper<RolePermission> permissionEntityWrapper = new EntityWrapper<>();
        permissionEntityWrapper.in("rid", id);
        rolePermissionDao.delete(permissionEntityWrapper);

        return true;
    }

    /**
     * 分配用户角色
     * @param userRoles
     * @return
     */
    @Transactional
    @Override
    public boolean modifyUserRole(List<UserRole> userRoles) {
        // 删除用户角色原表数据
        EntityWrapper<UserRole> userRoleEntityWrapper = new EntityWrapper<>();
        userRoleEntityWrapper.eq("uid", userRoles.get(0).getUid());
        userRoleDao.delete(userRoleEntityWrapper);

        // 新增用户角色数据 并 返回
        return userRoleService.insertBatch(userRoles);
    }
    
}
