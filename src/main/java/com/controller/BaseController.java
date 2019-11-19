package com.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;

import com.entity.User;
import com.vo.ReturnResult;

/**
 * 通用Controller
 * @author ljh
 */
public class BaseController {
	
    /**
     * 渲染失败数据
     * @return result
     */
    protected ReturnResult renderError() {
    	ReturnResult result = new ReturnResult();
        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.setCode(2);//null-默认,0-警告，1-成功，2-失败,5-哭,6-笑
        return result;
    }

    /**
     * 渲染失败数据（带消息）
     * @param msg 需要返回的消息
     * @return result
     */
    protected ReturnResult renderError(String msg) {
    	ReturnResult result = renderError();
        result.setMsg(msg);
        return result;
    }

    /**
     * 渲染成功数据
     * @return result
     */
    protected ReturnResult renderSuccess() {
    	ReturnResult result = new ReturnResult();
    	result.setStatus(HttpStatus.OK.value());
    	result.setCode(1);
        return result;
    }

    /**
     * 渲染成功数据（带信息）
     * @param msg 需要返回的信息
     * @return result
     */
    protected ReturnResult renderSuccess(String msg) {
    	ReturnResult result = renderSuccess();
        result.setMsg(msg);
        return result;
    }
    
    /**
     * 渲染成功数据（带数据）
     * @param msg 需要返回的信息
     * @return result
     */
    protected ReturnResult renderSuccess(Object object) {
    	ReturnResult result = renderSuccess();
        result.setData(object);
        return result;
    }

    /**
     * 获取当前登录用户名
     */
    public String getCurrentLoginUsername() {
        Subject currentUser = SecurityUtils.getSubject();
        User user = currentUser.getPrincipals().oneByType(User.class);
        return user.getUsername();
    }

    /**
     * 获取当前登陆用户id
     * @return
     */
    public String getCurrentLoginId(){
        Subject currentUser = SecurityUtils.getSubject();
        User user = currentUser.getPrincipals().oneByType(User.class);
        return user.getId();
    }

    /**
     * 获取当前登录用户对象
     */
    public User getCurrentUser() {
        Subject currentUser = SecurityUtils.getSubject();
        User user = currentUser.getPrincipals().oneByType(User.class);
        return user;
    }
    
}
