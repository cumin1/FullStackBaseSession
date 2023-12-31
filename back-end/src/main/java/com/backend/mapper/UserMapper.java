package com.backend.mapper;

import com.backend.entity.auth.Account;
import com.backend.entity.user.AccountUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    
    @Select("select * from db_account where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);

    @Insert("insert into db_account (username,password,email) values (#{username},#{password},#{mail})")
    int createAccount(String username,String password,String mail);

    @Select("select * from db_account where username = #{text} or email = #{text}")
    AccountUser findAccountUserByNameOrEmail(String text);
}
