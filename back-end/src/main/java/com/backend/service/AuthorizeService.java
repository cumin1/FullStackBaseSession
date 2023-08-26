package com.backend.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends UserDetailsService {

    // 发送邮件
    boolean sendVaildEmail(String emailAddress);
}
