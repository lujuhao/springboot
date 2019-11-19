package com.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.entity.Role;
import com.entity.User;
import com.entity.UserRole;
import com.service.RoleService;
import com.service.UserRoleService;
import com.vo.ReturnResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色Controller
 * @author ljh
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 角色列表页面
     * @return
     */
    @GetMapping("list")
    public String list() {
        return "role/list";
    }

    /**
     * 获取角色列表
     * @param pageNumber 当前页
     * @param pageSize 每页显示条数
     * @param searchText 搜索名称
     * @return
     */
    @ResponseBody
    @PostMapping("getList")
    public Map<String, Object> getUserList(int pageNumber, int pageSize, String searchText) {
        Map<String,Object> result = new HashMap<String,Object>();
        Page<Role> page = new Page<>(pageNumber, pageSize);
        EntityWrapper<Role> wrapper = new EntityWrapper<>();
        if (!StringUtils.isEmpty(searchText)) {
            wrapper.like("name", searchText);
        }
        wrapper.orderBy("sort", true);
        Page<Role> rolePage = roleService.selectPage(page, wrapper);
        result.put("total", rolePage.getTotal());
        result.put("rows", rolePage.getRecords());
        return result;
    }

    /**
     * 添加用户
     * @param role
     * @return
     */
    @ResponseBody
    @PostMapping("/addRole")
    public ReturnResult addRole(Role role) {
        return roleService.insert(role) ? renderSuccess("添加成功") : renderError("添加失败");
    }

    /**
     * 修改用户
     * @param role
     * @return
     */
    @ResponseBody
    @PostMapping("/updateRole")
    public ReturnResult updateRole(Role role) {
        return roleService.updateById(role) ? renderSuccess("修改成功") : renderError("修改失败");
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete")
    public ReturnResult delete(@RequestParam(value = "id", required = false) String id) {
        if (StringUtils.isEmpty(id)) {
            return renderError("请选择数据");
        }
        return roleService.deleteRoleByIdAndPermission(id) ? renderSuccess("删除成功") : renderError("删除失败");
    }

    /**
     * 分配角色
     * 查询用户拥有的角色
     * @return
     */
    @ResponseBody
    @RequestMapping("/getUserRole")
    public ReturnResult getUserRole(@RequestBody User user) {
        EntityWrapper<UserRole> wrapper = new EntityWrapper<>();
        wrapper.eq("uid", user.getId());
        List<UserRole> userRoles = userRoleService.selectList(wrapper);
        return renderSuccess("分配成功");
    }

    /**
     * 分配用户角色
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyUserRole")
    public ReturnResult modifyUserRole(@RequestBody List<UserRole> userRoles) {
        return roleService.modifyUserRole(userRoles) ? renderSuccess("分配成功"): renderError("分配失败");
    }

}
