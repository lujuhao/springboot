package com.config.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理
 * @author ljh
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Shiro未授权异常处理
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 * @throws Exception
	 */
    @ExceptionHandler(value = UnauthorizedException.class)
    public String unauthorizedExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
    	return "error/unauthorized";
    }

}
