package com.backend.config;


import com.alibaba.fastjson.JSONObject;
import com.backend.entity.RestBean;
import com.backend.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurithConfiguration {

    @Resource
    AuthorizeService authorizeService;
    @Resource
    DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           PersistentTokenRepository persistentTokenRepository) throws Exception {
        return http
                .authorizeHttpRequests()
                .requestMatchers("/api/user/**").permitAll() // 放行该请求路径下的所有请求
                .anyRequest().authenticated() //所有界面都需要登录验证后才能访问
                .and()
                .formLogin() //使用表单登录
                .loginProcessingUrl("/api/user/login")   //配置登录发起请求的路径
                .successHandler(this::onAuthenticationSuccess) // 配置登录成功后的响应
                .failureHandler(this::onAuthenticationFailure) // 配置登录失败后的响应
                .and()
                .logout()
                .logoutUrl("/api/user/logout")   // 配置登出发起的请求
                .logoutSuccessHandler(this::onAuthenticationSuccess) //配置登出成功地响应
                .and()          //                .userDetailsService(authorizeService) // 配置登录验证所需要用到的服务（查表验证）
                .rememberMe()
                .rememberMeParameter("remember")  //配置前端记住我的功能
                .tokenRepository(persistentTokenRepository)  //配置持久化token
                .tokenValiditySeconds(3600*24*7) //token存在时间
                .and()
                .csrf()     // 关掉csrf 不需要网站内所有请求都携带csrf-token
                .disable()
                .cors()
                .configurationSource(this.corsConfigurationSource())  // 处理跨域问题
                .and()
                .exceptionHandling().authenticationEntryPoint(this::onAuthenticationFailure) // 发生异常或者没有权限的处理
                .and()
                .build();

    }


    //配置持久化token 用于前端记住账号的功能
    @Bean
    public PersistentTokenRepository tokenRepository(){ //这是security底下的包 专门用于rememberme 的操作
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl(); //token存在数据库
        jdbcTokenRepository.setDataSource(dataSource); //设置数据库
        jdbcTokenRepository.setCreateTableOnStartup(false); //第一次不知道用什么表所以创建一个 之后就设置为false不创建了
        return jdbcTokenRepository;
    }

    // 解决跨域问题
    private CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOriginPattern("*");
        cors.setAllowCredentials(true);
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",cors);
        return source;
    }

    @Bean// 配置登录验证所需要用到的服务（查表验证）
    public AuthenticationManager authenticationManager(HttpSecurity security) throws Exception {
        return security
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(authorizeService)
                .passwordEncoder(new BCryptPasswordEncoder())
                .and()
                .build();
    }

    // 登录成功的响应  默认的成功处理是重定向302 的continue路径
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        if(request.getRequestURI().endsWith("/login"))
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
        else if (request.getRequestURI().endsWith("/logout"))
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("登出成功")));
    }

    //登录失败的处理
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, exception.getMessage())));
    }

}
