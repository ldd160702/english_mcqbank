package com.example.english_mcqbank.controller;


import com.example.english_mcqbank.model.Log;
import com.example.english_mcqbank.model.UserEntity;
import com.example.english_mcqbank.service.ExamService;
import com.example.english_mcqbank.service.LogService;
import com.example.english_mcqbank.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    final UserDetailsServiceImpl userService;
    final LogService logService;
    final ExamService examService;
    final PasswordEncoder passwordEncoder;

    @RequestMapping(value = {"/", "/home", "/index"})
    public ModelAndView homepage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserEntity user = userService.getUserByUsername(authentication.getName());
            if (user != null) {
                if (user.getRoles()[0].equals("ROLE_ADMIN")) {
                    return new ModelAndView("redirect:/admin");
                } else if (user.getRoles()[0].equals("ROLE_USER")) {
                    return new ModelAndView("redirect:/user");
                }
            }
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

    @RequestMapping("/admin/users/{id}")
    public ModelAndView viewUser(@PathVariable Integer id) {
        ModelAndView view = new ModelAndView("profile");
        UserEntity user = userService.getUserByUserid(id);
        view.addObject("user", user);
        return view; // Trả về admin.jsp
    }

    @RequestMapping("/admin/users/{id}/logs")
    public ModelAndView viewUserLogs(@PathVariable Integer id,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size) {
        ModelAndView logsModelAndView = new ModelAndView("logs");
        UserEntity user = userService.getUserByUserid(id);
        List<Log> logs = logService.getLogsByUser(user, page, size);
        logsModelAndView.addObject("logs", logs);
        logsModelAndView.addObject("currentPage", page);
        assert logs != null;
        boolean hasNext = logs.size() >= size;
        logsModelAndView.addObject("hasNext", hasNext);
        return logsModelAndView; // Trả về admin.jsp
    }

    @RequestMapping("/user")
    public ModelAndView user() {
        ModelAndView userModelAndView = new ModelAndView("user");
        userModelAndView.addObject("user", "user");
        return userModelAndView; // Trả về user.jsp
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
