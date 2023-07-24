package com.example.english_mcqbank.service;

import com.example.english_mcqbank.model.Result;
import com.example.english_mcqbank.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;

    public List<Result> listAll() {
        return resultRepository.findAll();
    }

    public void save(Result result) {
        resultRepository.save(result);
    }
}
