package com.example.english_mcqbank.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "exams")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ex_id")
    private Integer id;

    @Column(name = "ex_percent")
    private Integer percent;

    @Column(name = "tp_id")
    private Integer topicId;

    @Column(name = "ex_question_no")
    private Integer questionNo;

    @Column(name = "ex_time")
    private Date time;
}
