package com.example.english_mcqbank.controller;


import com.example.english_mcqbank.model.Log;
import com.example.english_mcqbank.model.UserEntity;
import com.example.english_mcqbank.service.LogService;
import com.example.english_mcqbank.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    final UserDetailsServiceImpl userService;
    final LogService logService;

    @RequestMapping(value = {"/", "/home", "/index"})
    public ModelAndView homepage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return new ModelAndView("redirect:/main");
        }

        return new ModelAndView("index"); // Trả về index.jsp
    }



    @RequestMapping("/admin")
    public ModelAndView admin() {
        ModelAndView adminModelAndView = new ModelAndView("admin");
        adminModelAndView.addObject("admin", "admin");
        List<UserEntity> users = userService.getAllUsers();
        adminModelAndView.addObject("users", users);
        return adminModelAndView; // Trả về admin.jsp
    }


    @RequestMapping("/user")
    public ModelAndView user() {
        ModelAndView userModelAndView = new ModelAndView("user");
        userModelAndView.addObject("user", "user");
        return userModelAndView; // Trả về user.jsp
    }

    @RequestMapping("/user/profile")
    public ModelAndView userProfile(Authentication authentication) {
        ModelAndView userProfileModelAndView = new ModelAndView("profile");
        String username = authentication.getName();
        UserEntity user = userService.getUserByUsername(username);
        userProfileModelAndView.addObject("user", user);
        return userProfileModelAndView; // Trả về user.jsp
    }

    @RequestMapping("/user/profile/logs")
    public ModelAndView userLogs(Authentication authentication) {
        ModelAndView userLogsModelAndView = new ModelAndView("logs");
        String username = authentication.getName();
        UserEntity user = userService.getUserByUsername(username);
        if (user == null) {
            return new ModelAndView("redirect:/user/profile");
        }
        List<Log> logs = logService.getLogsByUser(user);
        userLogsModelAndView.addObject("user", user);
        userLogsModelAndView.addObject("logs", logs);
        return userLogsModelAndView; // Trả về user.jsp
    }

    @RequestMapping("/main")
    public ModelAndView hello(Authentication authentication) {
        ModelAndView helloModelAndView = new ModelAndView("main");
        helloModelAndView.addObject("username", authentication.getName());
        return helloModelAndView; // Trả về
    }



    /*public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }*/
}
