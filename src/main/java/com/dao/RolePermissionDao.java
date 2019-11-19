package com.dao;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.RolePermission;

/**
 * 角色权限表接口
 * @author ljh
 */
@Repository
public interface RolePermissionDao extends BaseMapper<RolePermission> {

}