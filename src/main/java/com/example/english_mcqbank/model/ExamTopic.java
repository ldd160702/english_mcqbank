package com.example.english_mcqbank.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "exam_topic")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ExamTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "et_id")
    private Integer id;

    @Column(name = "ex_id")
    private Integer examId;

    @Column(name = "tp_id")
    private Integer topicId;

    @Column(name = "et_percent")
    private Integer percent;


}
