package com.controller;


import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.Permission;
import com.entity.RolePermission;
import com.service.PermissionService;
import com.vo.ReturnResult;
import com.vo.ZTreeNodes;

/**
 * 权限表 Controller
 * @author ljh
 *
 */
@Controller
@RequestMapping("/permission/")
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 权限列表页面
     * @return
     */
    @RequiresPermissions("permission:show")
    @GetMapping("list")
    public String list() {
        return "permission/permissionList";
    }

    /**
     * 查询权限列表
     * @param permission 过滤条件
     * @return
     */
    @RequiresPermissions("permission:show")
    @GetMapping()
    @ResponseBody
    public List<Permission> selectUserByPage() {
		List<Permission> list = permissionService.selectList(null);
		return list;
    }

    /**
     * 查询所有权限树形展示 并且选中角色拥有的树节点
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getPermissionZTreeNodes")
    public List<ZTreeNodes> getPermissionZTreeNodes(Long roleId){
        return permissionService.findPermissionZTreeNodes(roleId);
    }

    /**
     * 修改角色权限
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modifyRolePermission")
    public ReturnResult modifyRolePermission(@RequestBody List<RolePermission> rolePermissions) {
        return permissionService.modifyRolePermission(rolePermissions) ? renderSuccess("分配成功"): renderError("分配失败");
    }

    /**
     * 添加权限
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addPermission")
    public ReturnResult addPermission(Permission permission) {
        if (permission.getType() == 0) {
            permission.setPid("0");
        }
        return permissionService.insert(permission) ? renderSuccess("添加成功") : renderError("添加失败");
    }

    /**
     * 修改权限
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updatePermission")
    public ReturnResult updatePermission(Permission permission) {
        if (permission.getType() == 0) {
            permission.setPid("0");
        }
        return permissionService.updateById(permission) ? renderSuccess("修改成功") : renderError("修改失败");
    }

    /**
     * 删除权限 和 权限角色表数据
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/deletePermissionRole")
    public ReturnResult deletePermissionRole(@RequestParam(value = "id", required = false) String id) {
        if (StringUtils.isEmpty(id)) {
            return renderError("请选择数据");
        }
        EntityWrapper<Permission> wrapper = new EntityWrapper<>();
        wrapper.eq("pid", id);
        List<Permission> permissions = permissionService.selectList(wrapper);
        if (permissions.size() >= 1) {
            return renderError("请删除子节点数据");
        }
        return permissionService.deletePermissionRole(id) ? renderSuccess("删除成功") : renderError("删除失败");
    }

    /**
     * 获取父级select
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getTypePermission/{type}")
    public List<Permission> getTypePermission(@PathVariable("type") Integer type) {
        if (type == null) {
            type = 0;
        }
        if (type < 0 || type >1) {
            return null;
        }
        EntityWrapper<Permission> permissionEntityWrapper = new EntityWrapper<>();
        permissionEntityWrapper.eq("type", type);
        permissionEntityWrapper.orderBy("sort", true);
        List<Permission> permissions = permissionService.selectList(permissionEntityWrapper);
        return permissions;
    }

    /**
     * 传入Id 查询一条权限值返回
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getPermissionById")
    public Permission getPermissionById(Long id) {
        return permissionService.selectById(id);
    }

}
