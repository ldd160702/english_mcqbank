package com.example.english_mcqbank.controller;


import com.example.english_mcqbank.model.*;
import com.example.english_mcqbank.service.ExamService;
import com.example.english_mcqbank.service.LogService;
import com.example.english_mcqbank.service.TopicService;
import com.example.english_mcqbank.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    final UserDetailsServiceImpl userService;
    final LogService logService;
    final TopicService topicService;
    final PasswordEncoder passwordEncoder;
    final ExamService examService;

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

    @RequestMapping(value = "/admin/addExam", method = RequestMethod.GET)
    public ModelAndView addExam() {
        List<Topic> topics = topicService.getAllTopics();
        ModelAndView modelAndView = new ModelAndView("addExam");
        modelAndView.addObject("topics", topics);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/addExam", method = RequestMethod.POST)
    public ModelAndView addExam(@RequestParam("questionNo") String questionNo,
                                @RequestParam("topicId") int topicId,
                                RedirectAttributes redirectAttributes) {
        try {
            Exam exam = new Exam();
            exam.setQuestionNo(Integer.parseInt(questionNo));
            exam.setTopicId(topicId);
            exam.setTime(new Date());
            exam.setPercent(0);
            examService.saveExam(exam);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error adding exam");
            return new ModelAndView("redirect:/admin");
        }
        ModelAndView modelAndView1 = new ModelAndView("redirect:/admin/exams");
        redirectAttributes.addFlashAttribute("message", "Exam added successfully");
        return modelAndView1;
    }

    @RequestMapping(value = "/admin/exams", method = RequestMethod.GET)
    public ModelAndView exams() {
        ModelAndView modelAndView = new ModelAndView("exams");
        List<Exam> exams = examService.getAllExams();
        modelAndView.addObject("exams", exams);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/deleteExam", method = RequestMethod.GET)
    public ModelAndView deleteExam(@RequestParam("examId") int id, RedirectAttributes redirectAttributes) {
        try {
            Exam exam = examService.getExamById(id);
            if (exam == null) {
                redirectAttributes.addFlashAttribute("message", "Exam does not exist");
                return new ModelAndView("redirect:/admin/exams");
            }
            examService.deleteExam(exam);
            redirectAttributes.addFlashAttribute("message", "Exam deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting exam");
        }
        return new ModelAndView("redirect:/admin/exams");
    }

    @RequestMapping(value = "/admin/results/{examId}", method = RequestMethod.GET)
    public ModelAndView results(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @PathVariable("examId") int examId) {
        ModelAndView modelAndView = new ModelAndView("userResult");
        List<Result> results = examService.getResultsByExamId(examId, page, size);
        modelAndView.addObject("results", results);
        modelAndView.addObject("title", "All Users results for exam " + examId);
        modelAndView.addObject("currentPage", page);
        assert results != null;
        boolean hasNext = results.size() >= size;
        modelAndView.addObject("hasNext", hasNext);
        //modelAndView.addObject("examId", examId);
        return modelAndView;
    }
}
