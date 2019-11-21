package com.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.Menu;
import com.entity.User;
import com.service.PermissionService;
import com.service.UserService;
import com.utils.Constant;
import com.utils.ImageCode;
import com.vo.ReturnResult;

/**
 * 主页/登陆
 * @author ljh
 */
@Controller
@RequestMapping(value = "/")
public class IndexController extends BaseController {

    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 主页
     * @param model
     * @return
     */
    @RequestMapping(value = "index")
    public String index(Model model) {
        // 获取当前用户菜单
        List<Menu> menus = permissionService.createMenu(getCurrentLoginId());
        model.addAttribute("menus",menus);
        return "index";
    }

    /**
     * 主页预览
     * @return
     */
    @RequestMapping(value = "index_1")
    public String index_1() {
        return "/md";
    }

    /**
     * 登陆页面
     * @return
     */
    @GetMapping("login")
    public String login() {
        return "login";
    }

    /**
	 * 检测验证码
	 * @param session
	 * @param request
	 */
	private void verifyCode(HttpSession session, HttpServletRequest request) {
		String rightCode = (String) session.getAttribute(Constant.RANDOMCODE);
		String userCode = request.getParameter(Constant.RANDOMCODE);
		
		if (userCode == null  || !userCode.equalsIgnoreCase(rightCode)) {
			throw new AuthenticationException("验证码错误");
		}
	}
    
	/**
	 * 生成图片验证码
	 * @return
	 */
	@RequestMapping("createImgCode")
	public void createImgCode(HttpServletResponse response,HttpSession session){
		// 设置响应的类型格式为图片格式
		response.setContentType("image/jpeg");
		// 禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("expires", -1);

		ImageCode vCode = new ImageCode(100, 34, 4, 10);
		session.setAttribute("randomCode", vCode.getCode());
		try {
			vCode.write(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * 验证登录
     * @param loginName
     * @param password
     * @param map
     * @return
     */
    @RequestMapping("auth")
    @ResponseBody
    public ReturnResult auth(String loginName, String password, HttpSession session,
    		@RequestParam(required=false,defaultValue="false")boolean rememberMe,
    		HttpServletRequest request) {
    	UsernamePasswordToken token = new UsernamePasswordToken(loginName, password, rememberMe);
		Subject subject = SecurityUtils.getSubject();
		
		if (subject.isAuthenticated()) {
			return renderSuccess("index");
		}
		
	    try{
			verifyCode(session,request);
			subject.login(token);  
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			SecurityUtils.getSubject().getSession().setAttribute("user", user);
			session.removeAttribute(Constant.RANDOMCODE);
			return renderSuccess("index");
		}catch (UnknownAccountException e) {
			// 用户名未知...
			return renderError("此用户不存在");
		} catch (IncorrectCredentialsException e) {
			// 凭据不正确，例如密码不正确 ...
			return renderError("密码不正确");
		} catch (LockedAccountException e) {
			// 用户被锁定，例如管理员把某个用户禁用...
			return renderError("此用户已被禁用");
		} catch (ExcessiveAttemptsException e) {
			// 尝试认证次数多余系统指定次数 ...
			return renderError("登录次数过多");
		} catch (AuthenticationException e) {
			// 其他自定义异常
			return renderError(e.getMessage());
		}
    }

    /**
     * 用户注册界面
     * @return
     */
    @GetMapping(value = "register")
    public String register() {
        return "register";
    }

    /**
     * 用户注册
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/register")
    public ReturnResult register(User user, String code) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String sessionCode = (String) session.getAttribute("code");
        session.removeAttribute("code");
        if (sessionCode == null) {
            return renderSuccess("验证码过期请重新获取");
        }
        if (!sessionCode.equals(code)) {
            return renderError("验证码错误");
        }
        user.setStatus(5); // 状态待审核
        // 创建盐, 散列加密
        String salt = String.valueOf(System.currentTimeMillis());
        SimpleHash password = new SimpleHash("MD5", user.getPassword(), salt);
        user.setPassword(password.toString()); // 设置密码
        user.setNickname(user.getUsername()); // 设置昵称
        return userService.addUser(user) ? renderSuccess("注册成功") : renderSuccess("注册失败");
    }

    /**
     * 邮箱发送验证码
     * @param mail
     * @return
     */
    @ResponseBody
    @PostMapping("/sencCode")
    public ReturnResult sendCode(@RequestBody String mail) {
        mail = mail.replace("%40", "@");// 字符串替换

        // 判断是否已经注册
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq("email", mail);
        User user = userService.selectOne(wrapper);
        if (user != null) {
            return renderError("邮箱已注册");
        }
        int code = (int)((Math.random()*9+1)*100000);

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        session.setAttribute("code", code+"");

        /*SimpleMailMessage message = new SimpleMailMessage();*/
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            // 指明邮件的发件人
            mimeMessage.setFrom(new InternetAddress("fanshuye1304@163.com"));
            //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mail));
            //邮件的标题
            mimeMessage.setSubject("Bing-Upms 注册验证");
            //邮件的文本内容
            mimeMessage.setContent("<html><head><title>编辑邮件正文</title><style>html{word-wrap:break-word;}body{color:#000000;font-size:14px;font-family:Arial;line-height:1.7;padding:8px 10px;margin:0;background-color:#fff;}pre{white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;font-family:arial;}span.typoRed{border-bottom:2px dotted #ff0000;cursor:pointer;}.nui-scroll{overflow:auto}.nui-scroll::-webkit-scrollbar{width: 8px;height:8px}.nui-scroll::-webkit-scrollbar-track{border-radius:10px;-webkit-box-shadow:inset 0 0 6px rgba(0,0,0,0);}.nui-scroll::-webkit-scrollbar-track:hover{-webkit-box-shadow:inset 0 0 6px rgba(0,0,0,0.4);background-color:rgba(0,0,0,0.01)}.nui-scroll::-webkit-scrollbar-track:active{-webkit-box-shadow:inset 0 0 6px rgba(0,0,0,0.4);background-color:rgba(0,0,0,0.05)}.nui-scroll::-webkit-scrollbar-thumb{background-color: rgba(0, 0, 0, 0.05);border-radius:10px;-webkit-box-shadow:inset 1px 1px 0 rgba(0,0,0,.1)}.nui-scroll:hover::-webkit-scrollbar-thumb{background-color: rgba(0, 0, 0, 0.2);border-radius:10px;-webkit-box-shadow:inset 1px 1px 0 rgba(0,0,0,.1)}.nui-scroll::-webkit-scrollbar-thumb:hover{background-color:rgba(0, 0, 0, 0.4);-webkit-box-shadow:inset 1px 1px 0 rgba(0,0,0,.1)}.nui-scroll::-webkit-scrollbar-thumb:active{background:rgba(0,0,0,0.6);}</style></head><body class=\"nui-scroll\" contenteditable=\"true\"><div style=\"text-align: center;\"><span style=\"font-size: 24px;\"><b>Bing-UPMS 注册验证</b></span></div><div style=\"text-align: left;\"><span style=\"font-size: 18px;\">\u200B欢迎注册Bing-UPMS 验证码:&nbsp;"+code+"</span></div></body></html>", "text/html;charset=UTF-8");
        } catch (MessagingException e) {
            e.printStackTrace();
        }


        /*message.setFrom("fanshuye1304@163.com");//发送者.
        message.setTo(mail); // 接收者.
        message.setSubject("Bing-Upms 注册验证"); // 邮件主题.
        message.setText("<h1>验证码</h1>"); // 邮件内容.*/

        try {
            mailSender.send(mimeMessage); //发送邮件
            return renderSuccess("请查看邮箱验证码");
        } catch (MailException e) {
            return renderError("邮箱发送失败,请重新获取");
        }
    }

}
