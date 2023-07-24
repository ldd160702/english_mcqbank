package com.example.english_mcqbank.service;

import com.example.english_mcqbank.model.Exam;
import com.example.english_mcqbank.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {
    @Autowired
    private ExamRepository examRepository;

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public void saveExam(Exam exam) {
        examRepository.save(exam);
    }

    public Exam getExamById(int id) {
        return examRepository.findById(id).orElse(null);
    }

}
