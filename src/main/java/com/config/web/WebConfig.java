package com.config.web;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web相关配置
 * @author ljh
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${server.port}")
    private int serverPort;

    @Value("${server.http.port}")
    private int serverHttpPort;
    
    @Value("${file-upload-path}")
    private String fileUploadPath;
    
    /**
	 * 配置项目默认访问页面
	 * @param registry
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry){
		registry.addViewController("").setViewName("login");
	}
    
	/**
	 * 静态资源配置
	 */
    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	//项目静态资源映射
    	registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    	//服务器磁盘文件映射
    	registry.addResourceHandler("/myWorkspace/mySpringBoot/uploadPath/**").addResourceLocations("file:"+fileUploadPath);
	}

	/**
     * 解决跨域问题
     * @param registry
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置你要允许的网站域名，*表示任意域名
        config.addAllowedOrigin("*");
        // 表示你要允许的请求头部信息
        config.addAllowedHeader("*");
        // 设置你要允许的请求方法
        config.addAllowedMethod("GET,POST,PUT,DELETE,HEAD,OPTIONS");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
        // 这个顺序很重要，为避免麻烦请设置在最前
        bean.setOrder(0);
        return bean;

    }
    
    /**
     * Tomcat配置Https
     * @return
     */
    @Bean
    public TomcatServletWebServerFactory  servletContainer() {
        TomcatServletWebServerFactory  tomcat = new TomcatServletWebServerFactory () {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }

    /**
     * 配置监听端口
     */
    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        //Connector监听的http的端口号 
        connector.setPort(serverHttpPort);
        connector.setSecure(false);
        //监听到http的端口号后转向到的https的端口号
        connector.setRedirectPort(serverPort);
        return connector;
    }
}