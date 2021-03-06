package com.bank.config;

import com.bank.service.serviceImpl.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.SecureRandom;

/**
 * Created by yubraj on 12/29/16.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private Environment env;

    @Autowired
    private UserSecurityService userSecurityService;

    private static final String SALT = "thisisVeryStrongSalt:)";

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }

    public static final String[] PUBLIC_MATCHERS = {
            "/webjars/**",
            "/js/**",
            "/css/**",
            "/images/**",
            "/",
            "/about/**",
            "/contact/**",
            "/error/**/*",
            "/signup"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS)
                    .permitAll()
                        .anyRequest()
                            .authenticated();

        http.authorizeRequests()
                .antMatchers("/api/**").hasRole("ADMIN")
                    .anyRequest().authenticated();

        http
            .csrf().disable()
            .cors().disable()
            .formLogin()
                .failureUrl("/index?error")
                .defaultSuccessUrl("/userFront")
                .loginPage("/index")
                .permitAll()
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/index?logout")
                .deleteCookies("remember-me")
                .permitAll();
    }

    @Autowired
    public void ConfigureGlobal(AuthenticationManagerBuilder builder) throws Exception{
        builder.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
    }
}
