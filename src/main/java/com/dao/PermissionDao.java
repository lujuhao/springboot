package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.Permission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限表接口
 * @author ljh
 */
@Repository
public interface PermissionDao extends BaseMapper<Permission> {

    /**
     * 查询父菜单
     * @param uid 用户ID
     * @return
     */
    List<Permission> findParentMenu(String uid);

    /**
     * 查询子菜单
     * @param uid 用户ID
     * @param pid 父ID
     * @return
     */
    List<Permission> findSubMenu(@Param("uid")String uid, @Param("pid")Long pid);

    /**
     * 查询用户拥有那些权限
     * @param uid
     * @return
     */
    List<Permission> findUserPermission(String uid);



}