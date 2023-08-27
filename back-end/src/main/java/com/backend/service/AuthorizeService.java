package com.backend.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends UserDetailsService {

    // 发送邮件
    String sendVaildEmail(String emailAddress,String sessionId);

    // 注册验证
    String validateAndRegister(String username, String password, String email, String code, String sessionId);
}
