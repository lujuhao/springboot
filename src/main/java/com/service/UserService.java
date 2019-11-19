package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.User;
import com.vo.Page;

/**
 * 用户表Service
 * @author ljh
 */
public interface UserService extends IService<User> {

	/**
     * 分页查询用户列表
     * @param page 分页信息
     * @param user 过滤条件
     * @return
     */
	Page<User> selectUserByPage(Page<User> page,User user);
	
    /**
     * 添加用户
     * @param user
     * @return
     */
    boolean addUser(User user);

    /**
     * 修改用户
     * @param user
     * @return
     */
    boolean updateUser(User user);

    /**
     * 根据用户名称获取用户详情
     * @param loginName 用户名称
     * @return
     */
	public User getUserByName(String loginName);
    
    /**
     * 删除用户表并且删除用户角色关联表数据
     * @param id
     * @return
     */
    public boolean deleteUserByIdAndRole(String id);

}
