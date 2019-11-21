package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import org.springframework.web.multipart.MultipartFile;

import com.entity.Role;
import com.entity.User;
import com.entity.UserRole;
import com.service.RoleService;
import com.service.UserRoleService;
import com.service.UserService;
import com.utils.FileUtil;
import com.vo.Page;
import com.vo.ReturnResult;

/**
 * 用户Controller
 * @author ljh
 */
@Controller
@RequestMapping("/user/")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private UserRoleService userRoleService;


    /**
     * 用户列表页面
     * @return
     */
    @RequiresPermissions("user:show")
    @GetMapping("list")
    public String list(Model model) {
        List<Role> allRole = roleService.selectAllRole();
        model.addAttribute("allRole", allRole);
        return "user/userList";
    }
    
    /**
	 * 分页查询用户列表
	 * @param user 过滤条件
	 * @return
	 */
    @RequiresPermissions("user:show")
    @GetMapping()
    @ResponseBody
    public Map<String, Object> selectUserByPage(HttpServletRequest request,User user) {
    	Map<String, Object> map = new HashMap<String, Object>();
		Page<User> pageInfo = userService.selectUserByPage(new Page<User>(request),user);
		map.put("total", pageInfo.getTotal());
		map.put("rows", pageInfo.getList());
		return map;
    }

    /**
     * 添加用户
     * @param user 用户信息
     * @param 用户头像
     * @return
     */
    @ResponseBody
    @PostMapping
    public ReturnResult addUser(User user,@RequestParam(name="addHeadImg",required=false)MultipartFile file) {
    	try {
    		String uploadPath = FileUtil.uploadUserHeadImg(file);
    		user.setHeadImg(uploadPath);
    		userService.addUser(user);
    		return renderSuccess("添加用户成功");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("添加用户失败");
		}
    }

    /**
	 * 根据用户id获取用户
	 * @param userId 用户id
	 * @return
	 */
    @ResponseBody
	@GetMapping("{userId}")
	public User getUserById(@PathVariable("userId")String userId){
		return userService.selectById(userId);
	}
    
    /**
	 * 验证用户名是否存在
	 * @param userId 用户id
	 * @param userName 用户名
	 * @return
	 */
    @ResponseBody
	@RequestMapping("checkUserName")
	public Map<String, Boolean> checkUserName(@RequestParam(required = false)String userId,String userName){
    	boolean flag = false;
    	
    	User user = userService.getUserByName(userName);
    	if (StringUtils.isEmpty(userId)) {
    		if (null == user) {
				flag = true;
			}
    	}else {
    		User oldUser = userService.selectById(userId);
			if(oldUser.getUsername().equals(userName)){
				flag = true;
			}else {
				if (null == user) {
					flag = true;
				}
			}
		}
    	
    	Map<String, Boolean> map = new HashMap<String, Boolean>();
    	map.put("valid", flag);
		return map;
	}
    
    /**
     * 修改用户
     * @param user
     * @param file
     * @return
     */
    @ResponseBody
    @PutMapping()
    public ReturnResult updateUser(User user, @RequestParam(value="updateHeadImg",required=false)MultipartFile multipartFile) {
    	try {
    		if (null != multipartFile) {
    			String filePath = FileUtil.uploadUserHeadImg(multipartFile);
    			user.setHeadImg(filePath);
			}
    		userService.updateUser(user);
    		return renderSuccess("修改用户成功");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("修改用户失败");
		}
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("{id}")
    public ReturnResult deleteUser(@PathVariable("id")String id) {
        if (StringUtils.isEmpty(id)) {
            return renderError("请选择要删除的用户");
        }
        try {
        	userService.deleteUserById(id);
        	return renderSuccess("删除用户成功");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("删除用户失败");
		}
    }
    
    /**
     * 为用户分配角色
     * @param userId 用户id
     * @param roleIdList 角色id集合
     * @return
     */
    @ResponseBody
    @PostMapping("asignUserRoles")
    public ReturnResult asignUserRoles(@RequestBody List<UserRole> userRoleList){
    	try {
    		userRoleService.asignUserRoles(userRoleList);
    		return renderSuccess("分配角色成功");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("分配角色失败");
		}
    }
    
    /**
     * 获取用户角色
     * @param userId 用户id
     * @return
     */
    @ResponseBody
    @GetMapping("{id}/role")
    public ReturnResult getUserRoles(@PathVariable("id") String userId){
    	try {
    		return renderSuccess(userRoleService.getRoleListByUserId(userId));
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("获取用户角色失败");
		}
    }
    

}
