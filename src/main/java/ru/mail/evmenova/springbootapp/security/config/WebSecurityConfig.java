package ru.mail.evmenova.springbootapp.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.mail.evmenova.springbootapp.security.handlers.LoginSuccessHandler;
import ru.mail.evmenova.springbootapp.security.handlers.LogoutSuccessHandlerImpl;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsServiceImpl;

    @Autowired
    private DataSource dataSource;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public LogoutSuccessHandlerImpl logoutSuccessHandler() {
        return new LogoutSuccessHandlerImpl();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/user/**").hasAnyRole("user", "admin")
                .and()
                .authorizeRequests().antMatchers("/login**").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(loginSuccessHandler())
                .permitAll()
                .and()
                .logout().logoutSuccessHandler(logoutSuccessHandler()).permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied");
    }
}
