package com.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.RoleDao;
import com.dao.RolePermissionDao;
import com.entity.Role;
import com.github.pagehelper.PageHelper;
import com.service.RoleService;
import com.utils.RandomUtil;
import com.vo.Page;

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

    /**
	 * 获取全部角色
	 * @return
	 */
	@Override
	public List<Role> selectAllRole() {
		return roleDao.selectAllRole();
	}

    /**
     * 分页查询角色列表
     * @param page 分页信息
     * @param role 过滤条件
     * @return
     */
	@Override
	public Page<Role> selectRoleByPage(Page<Role> page, Role role) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		role.setPage(page);
		List<Role> list = roleDao.selectRoleList(role);
		page.setList(list);
		return page;
	}

	/**
	 * 根据名称获取角色
	 * @param name
	 * @return
	 */
	@Override
	public Role getRoleByName(String name) {
		return roleDao.getRoleByName(name);
	}

	/**
	 * 创建角色
	 * @param role
	 */
	@Override
	public void addRole(Role role) {
		role.setId(RandomUtil.getUUID());
		role.setGmtCreate(new Date());
		
		roleDao.insert(role);
	}
	
	/**
     * 删除角色
     * @param id
     * @return
     */
	@Override
	@Transactional
	public void deleteRoleById(String id) {
		String [] ids = id.split(",");
		for (String roleId : ids) {
			rolePermissionDao.deleteByRoleId(roleId);
			roleDao.deleteById(roleId);
		}
	}

}
