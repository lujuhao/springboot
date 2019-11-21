package com.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.UserDao;
import com.dao.UserRoleDao;
import com.entity.User;
import com.github.pagehelper.PageHelper;
import com.service.UserService;
import com.utils.FileUtil;
import com.utils.RandomUtil;
import com.utils.ShiroMd5Util;
import com.utils.SysConfigContants;
import com.vo.Page;

/**
 * 用户表Service实现
 * @author ljh
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private UserRoleDao userRoleDao;

    /**
     * 分页查询用户列表
     * @param page 分页信息
     * @param user 过滤条件
     * @return
     */
	@Override
	public Page<User> selectUserByPage(Page<User> page, User user) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		user.setPage(page);
		List<User> list = userDao.selectUserList(user);
		page.setList(list);
		return page;
	}
    
	/**
     * 添加用户
     * @param user
     * @return
     */
    @Override
    public boolean addUser(User user) {
    	user.setId(RandomUtil.getUUID());
    	user.setGmtCreate(new Date());
    	
    	//密码加密
    	user.setPassword(ShiroMd5Util.SysMd5(user));
    	
    	//设置默认头像
    	if (StringUtils.isEmpty(user.getHeadImg())) {
    		user.setHeadImg(SysConfigContants.USER_HEADIMG_DEFAULT);
		}
        Integer result = userDao.insert(user);
        if (result >= 1) {
            return true;
        }
        return false;
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @Override
    public boolean updateUser(User user) {
        Integer result = userDao.updateById(user);
        if (result >= 1) {
            return true;
        }
        return false;
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Transactional
    @Override
    public boolean deleteUserById(String id) {
		// 拼接用户表删除list
		String[] split = id.split(",");
		for (String userId : split) {
			User user = userDao.selectById(userId);
			// 删除用户头像
			FileUtil.deleteFile(user.getHeadImg());
			
			// 删除用户
			userDao.deleteById(userId);
			
			// 删除用户角色
			userRoleDao.deleteUserRoles(userId);
		}

		return true;
    }

    /**
     * 根据用户名称获取用户详情
     * @param loginName 用户名称
     * @return
     */
	@Override
	public User getUserByName(String loginName) {
		return userDao.getUserByName(loginName);
	}

}
