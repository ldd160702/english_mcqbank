package com.example.english_mcqbank.repository;

import com.example.english_mcqbank.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.*;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @Query(value = "SELECT * FROM " +
            "(SELECT * FROM QUESTIONS WHERE TP_ID = :topicId ORDER BY DBMS_RANDOM.VALUE()) " +
            "where ROWNUM <= :sampleSize",
             nativeQuery = true)
    List<Question> findRandomQuestions(int topicId, int sampleSize);

    List<Question> findByTopicId(int topicId);

    List<Question> findByTopicIdAndLevel(int topicId, int level);
}
