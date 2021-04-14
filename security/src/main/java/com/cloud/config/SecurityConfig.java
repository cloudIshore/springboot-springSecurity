package com.cloud.config;


import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/index").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");

        //设定登录页面：loginPage("/login")
        //设定登录页面跳转url <form th:action="@{/toLogin}" method="post">
        //设定登录成功跳转页面 defaultSuccessUrl("/index1") 与controller  @RequestMapping("index1") 一致
        http.formLogin().loginPage("/login").loginProcessingUrl("/toLogin").defaultSuccessUrl("/index");

        //get方式提交注销，用post方式提交，不用
        http.csrf().disable();

        //自定义注销成功返回界面：/index
        http.logout().logoutSuccessUrl("/index");

        //自定义记住我 “remember”，登录界面 <input type="checkbox" name="remember">记住我
        http.rememberMe().rememberMeParameter("remember");


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("cloud").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1","vip2","vip3")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2");
    }
}
