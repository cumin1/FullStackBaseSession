package com.backend.service.Impl;

import com.backend.entity.Account;
import com.backend.mapper.UserMapper;
import com.backend.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Resource
    UserMapper userMapper;
    @Autowired
    JavaMailSender MailSender;
    @Value("${spring.mail.username}")
    String From;



    // 这是登录之后在数据库中验证的服务
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        Account account = userMapper.findAccountByNameOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User
                .withUsername(account.getUsername())
                .password("{bcrypt}" + account.getPassword())
                .roles("user")
                .build();
    }

    @Override
    public boolean sendVaildEmail(String emailAddress) {
        // 生成验证码 把邮箱和对应验证码放到对应Redis中
        // (过期时间1分钟)如果此时重新要求发邮件 那么只需要剩余时间低于1分种就可以重新发送
        // 发送验证码到指定邮箱 发送失败吧Redis里的删除
        // 用户注册时，再从redis中取出键值对，验证是否一致
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;   // 生成验证码

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("该文件为验证码发送测试");
        message.setText("验证码为："+ code);
        message.setTo(emailAddress);  // 设置目标邮件地址
        message.setFrom(From);  //设置邮件发送地址

        try {
            MailSender.send(message);   //发送！
            return true;
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }

    }
}
