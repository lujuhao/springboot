package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.PermissionDao;
import com.dao.RolePermissionDao;
import com.entity.Menu;
import com.entity.Permission;
import com.entity.RolePermission;
import com.github.pagehelper.PageHelper;
import com.service.PermissionService;
import com.service.RolePermissionService;
import com.vo.Page;
import com.vo.ZTreeNodes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限表Service实现
 * @author ljh
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, Permission> implements PermissionService {

	@Autowired
	private PermissionDao permissionDao;
	
    @Autowired
    private RolePermissionDao rolePermissionMapper;
    
    @Autowired
    private RolePermissionService iRolePermissionService;

    /**
     * 分页查询权限列表
     * @param permission 过滤条件
     * @return
     */
	@Override
	public Page<Permission> selectPermissionByPage(Page<Permission> page, Permission permission) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		permission.setPage(page);
		List<Permission> permissionList = permissionDao.selectPermissionList(permission);
		page.setList(permissionList);
		return page;
	}
    
    /**
     * 创建登陆用户菜单
     * @param uid
     * @return
     */
    @Override
    public List<Menu> createMenu(String uid) {
        // 创建菜单对象
        List<Menu> menus = new ArrayList<>();

        // 查询父菜单
        List<Permission> parentMenu = baseMapper.findParentMenu(uid);
        parentMenu.forEach(p -> {
            Menu menu = new Menu();
            BeanUtils.copyProperties(p, menu);
            menus.add(menu);
        });

        // 查询子菜单
        menus.forEach(m -> {
            List<Menu> temp = new ArrayList<>();
            List<Permission> children = baseMapper.findSubMenu(uid, m.getId());
            children.forEach(c -> {
                Menu menu = new Menu();
                BeanUtils.copyProperties(c, menu);
                temp.add(menu);
            });
            m.setChildren(temp);
        });
        return menus;
    }

    @Override
    public List<ZTreeNodes> findPermissionZTreeNodes(Long roleId) {
        // 查询所有权限列表
        EntityWrapper<Permission> permissionEntityWrapper = new EntityWrapper<>();
        permissionEntityWrapper.orderBy("sort", true);
        List<Permission> permissions = baseMapper.selectList(permissionEntityWrapper);

        // 查询角色拥有那些权限
        EntityWrapper<RolePermission> rolePermissionEntityWrapper = new EntityWrapper<>();
        rolePermissionEntityWrapper.eq("rid", roleId);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(rolePermissionEntityWrapper);

        List<ZTreeNodes> zNodes = new ArrayList<>();
        permissions.forEach(p -> {
        	ZTreeNodes node = new ZTreeNodes();
            BeanUtils.copyProperties(p, node);
            // 设置展开节点
            if (p.getType() == 0 || p.getType() == 1) {
                node.setOpen(true);
            }
            // 设置角色拥有的权限选中
            rolePermissions.forEach(rp -> {
                if (p.getId().equals(rp.getPid())) {
                    node.setChecked(true);
                }
            });
            zNodes.add(node);
        });
        return zNodes;
    }

    @Transactional
    @Override
    public boolean modifyRolePermission(List<RolePermission> rolePermissions) {
        // 删除原表角色权限
        EntityWrapper<RolePermission> rolePermissionEntityWrapper = new EntityWrapper<>();
        rolePermissionEntityWrapper.eq("rid", rolePermissions.get(0).getRid());
        rolePermissionMapper.delete(rolePermissionEntityWrapper);

        // 新增角色权限 并返回处理结果
        return iRolePermissionService.insertBatch(rolePermissions);
    }

    @Transactional
    @Override
    public boolean deletePermissionRole(String id) {
        // 删除关联表数据
        EntityWrapper<RolePermission> rolePermissionEntityWrapper = new EntityWrapper<>();
        rolePermissionEntityWrapper.eq("pid", id);
        rolePermissionMapper.delete(rolePermissionEntityWrapper);

        // 删除权限表数据
        Integer result = baseMapper.deleteById(Long.valueOf(id));

        return result >= 1 ? true : false;
    }

}
