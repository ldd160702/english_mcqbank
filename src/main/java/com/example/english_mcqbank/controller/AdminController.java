package com.example.english_mcqbank.controller;


import com.example.english_mcqbank.model.Log;
import com.example.english_mcqbank.model.UserEntity;
import com.example.english_mcqbank.service.LogService;
import com.example.english_mcqbank.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    final UserDetailsServiceImpl userService;
    final LogService logService;
    final PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/admin/addUser", method = RequestMethod.GET)
    public String addUser(Model model) {
        model.addAttribute("user", new UserEntity());
        return "addUser"; // Trả về admin.jsp
    }

    @RequestMapping(value = "/admin/addUser", method = RequestMethod.POST)
    public ModelAndView registerUser(RedirectAttributes redirectAttributes,
                                     @ModelAttribute("user") UserEntity user,
                                     @RequestParam("confirmPassword") String confirmPassword,
                                     @RequestParam("role") String role) {
        if (userService.isUserPresent(user.getUsername())
                || userService.isEmailPresent(user.getEmail())
                || userService.isPhonePresent(user.getPhone())) {
            redirectAttributes.addFlashAttribute("message", "User already exists");
            return new ModelAndView("redirect:/admin");
        }

        if (!user.getPassword().equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("message", "Password and confirm password do not match");
            return new ModelAndView("redirect:/admin");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedDate(new Date());
        user.setStatus(1);
        if (role.equals("admin")) {
            user.setGroupId(0);
        } else {
            user.setGroupId(1);
        }

        try {
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("message", "User added successfully!");
            Log log = new Log();
            log.setUser(user);
            log.setDatetime(new Date());
            log.setName("User added successfully!");
            logService.saveLog(log);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error adding user");
        }

        return new ModelAndView("redirect:/admin");
    }


    @RequestMapping(value = "/admin/delete")
    public ModelAndView deleteUser(@RequestParam("username") String username, RedirectAttributes redirectAttributes) {
        try {
            UserEntity user = userService.findByUsername(username);
            if (user == null) {
                redirectAttributes.addFlashAttribute("message", "User does not exist");
                return new ModelAndView("redirect:/admin");
            }
            if (user.getGroupId() == 0) {
                redirectAttributes.addFlashAttribute("message", "Cannot delete admin");
                return new ModelAndView("redirect:/admin");
            }
            List<Log> logs = logService.getLogsByUser(user);
            if (logs != null) {
                logService.deleteAllLog(logs);
            }
            userService.deleteUser(user);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting user");
        }

        return new ModelAndView("redirect:/admin");
    }

    @RequestMapping(value = "/admin/allLogs")
    public ModelAndView allLogs(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int size) {
        ModelAndView modelAndView = new ModelAndView("allLogs");
        List<Log> logs = logService.findAllLogs(page, size);
        modelAndView.addObject("logs", logs);
        modelAndView.addObject("currentPage", page);
        assert logs != null;
        boolean hasNext = logs.size() >= size;
        modelAndView.addObject("hasNext", hasNext);

        return modelAndView;
    }
}
