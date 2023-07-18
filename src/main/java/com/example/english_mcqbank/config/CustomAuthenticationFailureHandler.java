package com.example.english_mcqbank.config;

import com.example.english_mcqbank.model.Log;
import com.example.english_mcqbank.model.UserEntity;
import com.example.english_mcqbank.service.LogService;
import com.example.english_mcqbank.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    LogService logService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException, IOException {
        String username = request.getParameter("username");
        Log log = new Log();

        log.setDatetime(new java.util.Date());
        log.setStatus(0);
        UserEntity user = userDetailsService.getUserByUsername(username);

        if (user != null) {
            log.setUser(user);
            log.setName("UNAUTHORIZED: " + user.getUsername() + " tried to log in ");
            logService.saveLog(log);
        } else {
            log.setName("NOT FOUND: " + username + " tried to log in ");
            logService.saveLog(log);
        }
//        System.out.println("Authentication failed for username: " + username);
        request.getSession().setAttribute("errorMessage", "Invalid Username or Password");
        redirectStrategy.sendRedirect(request, response, "/login-page?error");
    }
}

