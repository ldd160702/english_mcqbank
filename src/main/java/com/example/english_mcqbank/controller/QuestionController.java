package com.example.english_mcqbank.controller;

import com.example.english_mcqbank.model.Question;
import com.example.english_mcqbank.model.Result;
import com.example.english_mcqbank.model.UserEntity;
import com.example.english_mcqbank.service.QuestionService;
import com.example.english_mcqbank.service.ResultService;
import com.example.english_mcqbank.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private UserDetailsServiceImpl userService;

//    private Map<Integer, Question> questionMap;
//
//    @RequestMapping("/list")
//    public ModelAndView list() {
//        ModelAndView modelAndView = new ModelAndView("questionList");
//        List<Question> questions = questionService.getRandom(1, 0, 5);
////        questionMap = new HashMap<>();
////        for (Question question : questions) {
////            questionMap.put(question.getId(), question);
////        }
//        //for (Question question : questions) {
//          //  System.out.println(question);
//        //}
//        modelAndView.addObject("questions", questions);
//        modelAndView.addObject("examId", 1);
//        return modelAndView;
//    }




    @PostMapping("/submit")
    public ModelAndView submitAnswers(@RequestParam Map<String, String> params, Authentication authentication,
                                      @RequestParam("examId") int examId) {
        // Process the submitted form data
        Integer score = 0;
        int totalQuestions = 0;
        // sort question by id

        for (String paramName : params.keySet()) {
            if (paramName.startsWith("question_")) {
                int questionId = Integer.parseInt(paramName.substring("question_".length()));
                Question question = questionService.get(questionId);
                //Question question = questionMap.get(questionId);
                String selectedOption = params.get(paramName);
                // Do something with the selected option for each question (e.g., save to database)
                if (selectedOption.equals(question.getCorrectAnswer())) {
                    score++;
                }
                //System.out.println("Question " + questionId + ": Selected Option: " + selectedOption);
                totalQuestions++;
            }
        }

        // Redirect or return a response as needed
        ModelAndView modelAndView = new ModelAndView("resultPage");
        modelAndView.addObject("score", score);
        modelAndView.addObject("totalQuestions", totalQuestions);
        //questionMap.clear();

        UserEntity user = userService.getUserByUsername(authentication.getName());
        Result result = new Result();
        result.setScore(score);
        result.setTime(new Date());
        result.setExamId(examId);
        user.addResult(result);
        userService.saveUser(user);
        return modelAndView;
    }

//    @RequestMapping("/list")
//    public ModelAndView list() {
//        ModelAndView modelAndView = new ModelAndView("questionList");
//        List<Question> questions = questionService.getRandom(1, 1, 10);
//
//        // Create a map to store the preloaded questions using their IDs as keys
//        Map<Integer, Question> questionMap = new HashMap<>();
//        for (Question question : questions) {
//            questionMap.put(question.getId(), question);
//        }
//
//        // Add the questionMap as a request attribute
//        modelAndView.addObject("questionMap", questionMap);
//        return modelAndView;
//    }
//
//
//    @PostMapping("/submit")
//    public ModelAndView submitAnswers(@RequestParam Map<String, String> params, HttpServletRequest request) {
//        // Process the submitted form data
//        int score = 0;
//        int totalQuestions = 0;
//
//        // Get the preloaded questionMap from the request attributes
//        Map<Integer, Question> questionMap = (Map<Integer, Question>) request.getAttribute("questionMap");
//
//        for (String paramName : params.keySet()) {
//            if (paramName.startsWith("question_")) {
//                int questionId = Integer.parseInt(paramName.substring("question_".length()));
//                Question question = questionMap.get(questionId);
//                String selectedOption = params.get(paramName);
//
//                // Do something with the selected option for each question (e.g., save to database)
//                if (selectedOption.equals(question.getCorrectAnswer())) {
//                    score++;
//                }
//                System.out.println("Question " + questionId + ": Selected Option: " + selectedOption);
//                totalQuestions++;
//            }
//        }
//
//        // Redirect or return a response as needed
//        ModelAndView modelAndView = new ModelAndView("resultPage");
//        modelAndView.addObject("score", score);
//        modelAndView.addObject("totalQuestions", totalQuestions);
//        return modelAndView;
//    }
}
