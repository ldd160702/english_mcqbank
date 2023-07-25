package com.example.english_mcqbank.controller;

import com.example.english_mcqbank.model.UserEntity;
import com.example.english_mcqbank.model.test.Parent;
import com.example.english_mcqbank.service.EmailSender;
import com.example.english_mcqbank.service.LoggedInUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class MyController {
    private final EmailSender emailSender;

    @Autowired
    Parent test;

    @RequestMapping("/hello")
    public String hello() {
        test.doSomething();
        return "Hello world";
    }

    @RequestMapping("/send-email")
    public ResponseEntity<String> sendEmail() {
        emailSender.sendEmail("luongdinhduc0000@Gmail.com", "Test", "Test");
        return ResponseEntity.ok("Email sent");
    }

    @Autowired
    LoggedInUserService loggedInUserService;

    @RequestMapping("/myUser")
    public ResponseEntity<String> myUser() {
        UserEntity userA = loggedInUserService.getLoggedInUser();
        if (userA == null) {
            return ResponseEntity.ok("null");
        }
        return ResponseEntity.ok(userA.getUsername());
    }
}
