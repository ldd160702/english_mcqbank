package com.example.english_mcqbank.config;

import com.example.english_mcqbank.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AppBasicAuthenticationEntryPoint basicAuthenticationEntryPoint;
    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler;


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // Cung cáp userservice cho spring security
        provider.setPasswordEncoder(passwordEncoder()); // cung cấp password encoder
        return provider;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/", "/home", "/login", "/register","/register2", "/css/**","/js/**","/test/**").permitAll() // Cho phép tất cả mọi người truy cập vào 2 địa chỉ này
                .anyRequest().authenticated() // Tất cả các request khác đều cần phải xác thực mới được truy cập
                .and()
                .formLogin() // Cho phép người dùng xác thực bằng form login
                .loginPage("/login-page")
                .loginProcessingUrl("/login-processing")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                //.defaultSuccessUrl("/main")
                .permitAll() // Tất cả đều được truy cập vào địa chỉ này
                .and()
                .logout() // Cho phép logout
                //.logoutSuccessUrl("/login-page")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied")
                .and()
                .httpBasic()
                .authenticationEntryPoint(basicAuthenticationEntryPoint);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

