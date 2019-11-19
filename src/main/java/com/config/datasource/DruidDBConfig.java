package com.config.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * Druid数据源配置
 * @author ljh
 */
@Configuration
public class DruidDBConfig {
	
	/**
	 * 配置数据源
	 * @return
	 */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }
    
   /**
    * 配置事务管理器
    * @return
    */
    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
    	return new DataSourceTransactionManager(dataSource);
    }
    
    /**
     * 配置监控服务器(一个管理后台的Servlet)http://localhost:8080/druid/login.html
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Bean
    public ServletRegistrationBean statViewServlet() {
        @SuppressWarnings("unchecked")
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        // IP白名单
        servletRegistrationBean.addInitParameter("allow","");//默认运行所有
        // IP黑名单(共同存在时，deny优先于allow)
        servletRegistrationBean.addInitParameter("deny","192.168.1.1");
        //控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername","admin");
        servletRegistrationBean.addInitParameter("loginPassword","admin");
        //是否能够重置数据
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }

    /**
     * 配置服务过滤器
     * @return 返回过滤器配置对象
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 拦截的请求
        filterRegistrationBean.addUrlPatterns("/*");
        // 忽略的请求
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,");
        return filterRegistrationBean;
    }
}
