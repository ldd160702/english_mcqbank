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
public class UserController {
    final UserDetailsServiceImpl userService;
    final LogService logService;
    final PasswordEncoder passwordEncoder;

    @RequestMapping("/user/profile")
    public ModelAndView userProfile(Authentication authentication) {
        ModelAndView userProfileModelAndView = new ModelAndView("profile");
        String username = authentication.getName();
        UserEntity user = userService.getUserByUsername(username);
        userProfileModelAndView.addObject("user", user);
        return userProfileModelAndView; // Trả về user.jsp
    }

    @RequestMapping("/user/profile/logs")
    public ModelAndView userLogs(Authentication authentication,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size) {
        ModelAndView userLogsModelAndView = new ModelAndView("logs");
        String username = authentication.getName();
        UserEntity user = userService.getUserByUsername(username);
        if (user == null) {
            return new ModelAndView("redirect:/user/profile");
        }

        List<Log> logs = logService.getLogsByUser(user, page, size);
        userLogsModelAndView.addObject("logs", logs);
        userLogsModelAndView.addObject("currentPage", page);
        assert logs != null;
        boolean hasNext = logs.size() >= size;
        userLogsModelAndView.addObject("hasNext", hasNext);
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
            //redirectAttributes.addFlashAttribute("errorMessage", "Incorrect password!");
            ModelAndView view = new ModelAndView("change-password");
            view.addObject("errorMessage", "Incorrect password!");
            //return new ModelAndView("redirect:/user/profile/change-password");
            return view;
        }

        if (newPassword.equals(confirmNewPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.saveUser(user);
            //redirectAttributes.addFlashAttribute("successMessage", "Update Password successfully!");
            ModelAndView view = new ModelAndView("profile");
            view.addObject("successMessage", "Update Password successfully!");
            return view;
        }

//        redirectAttributes.addFlashAttribute("errorMessage", "Password and Confirm Password do not match!");
//
//        return new ModelAndView("redirect:/user/profile/change-password");
        ModelAndView view = new ModelAndView("change-password");
        view.addObject("errorMessage", "Password and Confirm Password do not match!");
        return view;
    }

    @RequestMapping(value = "/user/profile/edit", method = RequestMethod.POST)
    public ModelAndView editUserProfile(Authentication authentication,
                                        @ModelAttribute("currentUser") UserEntity user,
                                        RedirectAttributes redirectAttributes) {
        ModelAndView editUserModelAndView = new ModelAndView("editUser");
        String username = authentication.getName();
        UserEntity userEntity = userService.getUserByUsername(username);
        if (userEntity == null) {
            ModelAndView view = new ModelAndView("profile");
            view.addObject("errorMessage", "User not found!");
            return view;
        }

        userEntity.setFullName(user.getFullName());
        if (!user.getEmail().equals(userEntity.getEmail()) && userService.isEmailPresent(user.getEmail())) {
            //redirectAttributes.addFlashAttribute("errorMessage", "Email is already in use!");
            editUserModelAndView.addObject("errorMessage", "Email is already in use!");
            return editUserModelAndView;
        } else {
            userEntity.setEmail(user.getEmail());
        }
        if (!user.getPhone().equals(userEntity.getPhone()) && userService.isPhonePresent(user.getPhone())) {
            //redirectAttributes.addFlashAttribute("errorMessage", "Phone is already in use!");
            editUserModelAndView.addObject("errorMessage", "Phone is already in use!");
            return editUserModelAndView;
        } else {
            userEntity.setPhone(user.getPhone());
        }
        userEntity.setAddress(user.getAddress());

        try {
            userService.saveUser(userEntity);
            //redirectAttributes.addFlashAttribute("successMessage", "Update profile successfully!");
            ModelAndView view = new ModelAndView("redirect:/user/profile");
            view.addObject("successMessage", "Update profile successfully!");
            return view;
            //return new ModelAndView("redirect:/user/profile");
        } catch (Exception e) {
            return editUserModelAndView;
        }
    }
}
