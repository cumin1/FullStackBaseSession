package com.backend.controller;

import com.backend.entity.Account;
import com.backend.entity.RestBean;
import com.backend.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Validated
@RestController
@RequestMapping("/api/user")
public class RegisterController {
    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-z]{2,}$";

    //JavaMailSender是专门用于发送邮件的对象，自动配置类已经提供了Bean
    @Autowired
    JavaMailSender MailSender;
    @Autowired
    AuthorizeService authorizeService;

    //获取验证码
    @PostMapping("/email")
    public RestBean<String> email(@Pattern(regexp = EMAIL_REGEX)
                                      @RequestParam("email") String email,
                                  HttpSession session){
//        Random random = new Random();
//        int code = random.nextInt(900000) + 100000;   // 生成验证码
//        session.setAttribute("code",code);    // 将验证码存入session
//        session.setAttribute("email",email);  // 将邮箱地址存入session
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setSubject("该文件为验证码发送测试");
//        message.setText("验证码为："+ code);
//        message.setTo(email);  // 设置目标邮件地址
//        message.setFrom("fuhao6363@163.com");  //设置邮件发送地址
//        MailSender.send(message);
        if (authorizeService.sendVaildEmail(email,session.getId())) {
            return RestBean.success("邮件已发送，请注意查收");
        }else {
            return RestBean.failure(400,"邮件发送失败，请联系管理员");
        }


    }


    // 注册
    @PostMapping("/register")
    public RestBean register(@RequestBody Account account,HttpSession session){
        String sessionCode = session.getAttribute("code").toString();
        String sessionEmail = session.getAttribute("email").toString();
        String email = account.getEmail();


        return RestBean.success();
    }
}
