package com.backend.service.Impl;

import com.backend.entity.Account;
import com.backend.mapper.UserMapper;
import com.backend.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Resource
    UserMapper userMapper;
    @Autowired
    JavaMailSender MailSender;
    @Value("${spring.mail.username}")
    String From;
    @Resource
    StringRedisTemplate template;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


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
    public String sendVaildEmail(String emailAddress, String sessionId) {
        String key = "email:" + sessionId + ":" + emailAddress;
        if (Boolean.TRUE.equals(template.hasKey(key))) {  // 限制时间功能，一分钟内不能连续发送两次获取验证码请求
            Long expire = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);   // 获取该key剩余时间
            if (expire > 60) {
                return "请求频繁，请稍后再试";
            }
        }
        if (userMapper.findAccountByNameOrEmail(emailAddress) != null) { // 如果邮箱已经注册过了 返回false
            return "此邮箱已被其他用户注册";
        }

        // 生成验证码 把邮箱和对应验证码放到对应Redis中
        // (过期时间1分钟)如果此时重新要求发邮件 那么只需要剩余时间低于1分种就可以重新发送
        // 发送验证码到指定邮箱 发送失败吧Redis里的删除
        // 用户注册时，再从redis中取出键值对，验证是否一致
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;   // 生成验证码

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("该文件为验证码发送测试");
        message.setText("验证码为：" + code);
        message.setTo(emailAddress);  // 设置目标邮件地址
        message.setFrom(From);  //设置邮件发送地址

        try {
            MailSender.send(message);   //发送！
            // 将sessionid和邮件地址存入Redis  设置过期时间 1分钟
            template.opsForValue().set(key, String.valueOf(code), 1, TimeUnit.MINUTES);
            return null;
        } catch (MailException e) {
            e.printStackTrace();
            return "邮件发送失败，请检查邮件地址是否有效";
        }
    }


    //验证并注册
    @Override
    public String validateAndRegister(String username,
                                       String password,
                                       String email,
                                       String code,
                                       String sessionId) {
        String key = "email:" + sessionId + ":" + email;

        if( Boolean.TRUE.equals(template.hasKey(key))){
            String s = template.opsForValue().get(key);   //去redis中去key
            if(s==null){
                return "验证码失效，请重新请求";
            }
            if(s.equals(code)) {  // 判断验证码是否正确
                password = encoder.encode(password);
                if(userMapper.createAccount(username,password,email)>0){
                    return null;
                }else{
                    return "内部错误，请联系管理员";
                }
            }else{
                return "验证码错误";
            }

        }else{
            return "请先请求一封验证码邮件";
        }

    }
}
