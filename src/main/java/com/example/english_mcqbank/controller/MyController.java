package com.example.english_mcqbank.controller;

import com.example.english_mcqbank.service.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class MyController {
    private final EmailSender emailSender;

    @RequestMapping("/send-email")
    public ResponseEntity<String> sendEmail() {
        emailSender.sendEmail("luongdinhduc0000@Gmail.com", "Test", "Test");
        return ResponseEntity.ok("Email sent");
    }
}
