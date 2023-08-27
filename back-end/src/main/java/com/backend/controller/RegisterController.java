package com.backend.controller;

import com.backend.entity.RestBean;
import com.backend.service.AuthorizeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/user")
public class RegisterController {
    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-z]{2,}$";
    private final String USERNAME_REGEX = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";

    //JavaMailSender是专门用于发送邮件的对象，自动配置类已经提供了Bean
    @Autowired
    JavaMailSender MailSender;
    @Autowired
    AuthorizeService authorizeService;

    //获取验证码
    @PostMapping("/email")
    public RestBean<String> email(@Pattern(regexp = EMAIL_REGEX)
                                  @RequestParam("email") String email,
                                  HttpSession session) {
        String s = authorizeService.sendVaildEmail(email, session.getId());

        if (s == null) {
            return RestBean.success("邮件已发送，请注意查收");
        } else {
            return RestBean.failure(400, s);
        }
    }

    // 注册
    @PostMapping("/register")
    public RestBean<String> register(@Pattern(regexp = USERNAME_REGEX) @Length(min=2,max=8) @RequestParam("username") String username,
                                     @Length(min=6,max=16) @RequestParam("password") String password,
                                     @Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                     @Length(min = 6,max = 6) @RequestParam("code") String code,
                                     HttpSession session) {

        String s = authorizeService.validateAndRegister(username,password,email,code,session.getId());
        if (s == null) {
            return RestBean.success("注册成功");
        }else {
            return RestBean.failure(400,s);
        }


    }
}
