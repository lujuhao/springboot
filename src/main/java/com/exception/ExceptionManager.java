package com.exception;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 自定义异常管理
 * @author ljh
 */
@ControllerAdvice
public class ExceptionManager {
	
	/**
	 * 未定义异常处理
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler({RuntimeException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String exceptionHandler(HttpServletRequest request, Exception e){
		return "error/error";
	}
	
	/**
	 * 未授权异常处理
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = UnauthorizedException.class)
	public String unauthorizedExceptionHandler(HttpServletRequest request, Exception e){
		return "error/unauthorized";
	}
}
