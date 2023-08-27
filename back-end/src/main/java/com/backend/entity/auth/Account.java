package com.backend.entity.auth;

import lombok.Data;

@Data
public class Account {
    private Integer id;
    private String username;
    private String password;
    private String email;
}
