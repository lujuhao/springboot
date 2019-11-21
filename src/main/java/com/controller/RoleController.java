package com.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.entity.Role;
import com.entity.RolePermission;
import com.service.RolePermissionService;
import com.service.RoleService;
import com.vo.Page;
import com.vo.ReturnResult;
import com.vo.ZTreeNodes;

/**
 * 角色Controller
 * @author ljh
 */
@Controller
@RequestMapping("/role/")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private RolePermissionService rolePermissionService;
	
    /**
     * 角色列表页面
     * @return
     */
    @RequiresPermissions("role:show")
    @GetMapping("list")
    public String list() {
        return "role/roleList";
    }

    /**
   	 * 分页查询角色列表
   	 * @param role 过滤条件
   	 * @return
   	 */
    @RequiresPermissions("user:show")
    @GetMapping()
    @ResponseBody
    public Map<String, Object> selectRoleByPage(HttpServletRequest request,Role role) {
    	Map<String, Object> map = new HashMap<String, Object>();
		Page<Role> pageInfo = roleService.selectRoleByPage(new Page<Role>(request),role);
		map.put("total", pageInfo.getTotal());
		map.put("rows", pageInfo.getList());
		return map;
    }

    /**
     * 添加角色
     * @param role 角色信息
     * @return
     */
    @ResponseBody
    @PostMapping
    public ReturnResult addRole(Role role) {
    	try {
    		roleService.addRole(role);
    		return renderSuccess("添加角色成功");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("添加角色失败");
		}
    }
    
    /**
	 * 验证角色名是否存在
	 * @param id 角色id
	 * @param name 角色名称
	 * @return
	 */
    @ResponseBody
	@RequestMapping("checkRoleName")
	public Map<String, Boolean> checkUserName(@RequestParam(required = false)String id,String name){
    	boolean flag = false;
    	
    	Role role = roleService.getRoleByName(name);
    	if (StringUtils.isEmpty(id)) {
    		if (null == role) {
				flag = true;
			}
    	}else {
    		Role oldRole = roleService.selectById(id);
			if(oldRole.getName().equals(name)){
				flag = true;
			}else {
				if (null == role) {
					flag = true;
				}
			}
		}
    	
    	Map<String, Boolean> map = new HashMap<String, Boolean>();
    	map.put("valid", flag);
		return map;
	}
    
    /**
     * 删除角色
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("{id}")
    public ReturnResult deleteRole(@PathVariable("id")String id) {
        if (StringUtils.isEmpty(id)) {
            return renderError("请选择要删除的角色");
        }
        try {
        	roleService.deleteRoleById(id);
        	return renderSuccess("删除角色成功");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("删除角色失败");
		}
    }
    
    /**
     * 修改角色
     * @param role
     * @return
     */
    @ResponseBody
    @PutMapping()
    public ReturnResult updateUser(Role role) {
    	try {
    		roleService.updateById(role);
    		return renderSuccess("修改角色成功");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("修改角色失败");
		}
    }
    
    /**
     * 获取角色权限
     * @param id 角色id
     * @return
     */
    @ResponseBody
    @GetMapping("{id}/permissions")
    public ReturnResult getRolePermissionsForzTree(@PathVariable("id") String id){
    	try {
    		List<ZTreeNodes> permissions = rolePermissionService.getRolePermissionsForzTree(id);
    		return renderSuccess(permissions);
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("获取角色权限失败");
		}
    }
    
    /**
     * 分配角色权限
     * @param roleId 角色id
     * @param rolePermissions 角色权限
     * @return
     */
    @ResponseBody
    @PostMapping("{id}/permissions")
    public ReturnResult asignRolePermissions(@PathVariable("id") String roleId, @RequestBody List<RolePermission> rolePermissions){
    	try {
    		rolePermissionService.asignRolePermissions(roleId, rolePermissions);
    		return renderSuccess("分配权限成功");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("分配权限失败");
		}
    }
}
