package com.example.restfulwebservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception {
        //AuthenticationManagerBuilder jdbc 관련 인증 또는 메모리를 통한 인증방식 사용 가능
        //.password("{12345") <-pliain Text 에러 발생 Encoding 없이 사용한다고 명시해야 에러가 안납니다.
        auth.inMemoryAuthentication()
                .withUser("gunho")
                .password("{noop}12345")
                .roles("USERS"); //login시 사용 권한
    }
}
