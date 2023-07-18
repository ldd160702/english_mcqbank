package com.example.english_mcqbank.controller;


import com.example.english_mcqbank.model.Log;
import com.example.english_mcqbank.model.UserEntity;
import com.example.english_mcqbank.service.LogService;
import com.example.english_mcqbank.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    final UserDetailsServiceImpl userService;
    final LogService logService;
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

    @RequestMapping(value = "/user/profile/edit", method = RequestMethod.GET)
    public ModelAndView editUserProfile(Authentication authentication, Model model) {
        ModelAndView editUserModelAndView = new ModelAndView("editUser");
        String username = authentication.getName();
        UserEntity user = userService.getUserByUsername(username);
        if (user == null) {
            return new ModelAndView("redirect:/user/profile");
        }
        //editUserModelAndView.addObject("user", user);
        model.addAttribute("currentUser", user);
        return editUserModelAndView; // Trả về user.jsp
    }

    @RequestMapping(value = "/user/profile/change-password", method = RequestMethod.GET)
    public ModelAndView changePassword() {
        ModelAndView changePasswordModelAndView = new ModelAndView("change-password");

        return changePasswordModelAndView; // Trả về user.jsp
    }
    @RequestMapping(value = "/user/profile/change-password", method = RequestMethod.POST)
    public ModelAndView changePassword(@RequestParam("oldPassword") String oldPassword,
                                       @RequestParam("newPassword") String newPassword,
                                       @RequestParam("confirmNewPassword") String confirmNewPassword,
                                       Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        UserEntity user = userService.getUserByUsername(username);
        boolean check = passwordEncoder.matches(oldPassword, user.getPassword());
        if (!check) {
            redirectAttributes.addFlashAttribute("errorMessage", "Incorrect password!");
            return new ModelAndView("redirect:/user/profile/change-password");
        }

        if (newPassword.equals(confirmNewPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Update Password successfully!");
            return new ModelAndView("redirect:/user/profile");
        }

        redirectAttributes.addFlashAttribute("errorMessage", "Password and Confirm Password do not match!");

        return new ModelAndView("redirect:/user/profile/change-password");
    }

    @RequestMapping(value = "/user/profile/edit", method = RequestMethod.POST)
    public ModelAndView editUserProfile(Authentication authentication,
                                        @ModelAttribute("currentUser") UserEntity user,
                                        RedirectAttributes redirectAttributes) {
        ModelAndView editUserModelAndView = new ModelAndView("editUser");
        String username = authentication.getName();
        UserEntity userEntity = userService.getUserByUsername(username);
        if (userEntity == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Update profile failed!");
            return new ModelAndView("redirect:/user/profile");
        }

        userEntity.setFullName(user.getFullName());
        if (!user.getEmail().equals(userEntity.getEmail()) && userService.isEmailPresent(user.getEmail())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email is already in use!");
            return editUserModelAndView;
        } else {
            userEntity.setEmail(user.getEmail());
        }
        if (!user.getPhone().equals(userEntity.getPhone()) && userService.isPhonePresent(user.getPhone())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Phone is already in use!");
            return editUserModelAndView;
        } else {
            userEntity.setPhone(user.getPhone());
        }
        userEntity.setAddress(user.getAddress());

        try {
            userService.saveUser(userEntity);
            redirectAttributes.addFlashAttribute("successMessage", "Update profile successfully!");
            return new ModelAndView("redirect:/user/profile");
        } catch (Exception e) {
            return editUserModelAndView;
        }
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
