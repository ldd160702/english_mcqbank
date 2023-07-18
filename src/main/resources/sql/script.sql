create table USERS
(
    US_ID            NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) not null
        constraint TABLE1_PK
            primary key,
    US_USERNAME      VARCHAR2(45)                                                      not null
        unique,
    US_PASSWORD      VARCHAR2(120)                                                     not null,
    US_FULLNAME      VARCHAR2(45)                                                      not null,
    US_ADDRESS       VARCHAR2(45),
    US_MOBILE_NUMBER VARCHAR2(10) unique,
    US_EMAIL         VARCHAR2(45) unique,
    US_CREATED_DATE  DATE,
    US_STATUS        NUMBER(1),
    US_GROUP         NUMBER
)
/

create table LOGS
(
    LG_ID       NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) not null
        constraint LOGS_PK
            primary key,
    LG_NAME     VARCHAR2(120)                                                     not null,
    LG_STATUS   NUMBER(1),
    LG_DATETIME DATE,
    US_ID       NUMBER
        constraint "LOGS_USERS_US_ID_fk"
            references USERS
)
/

create table TOPICS
(
    TP_ID          NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) not null
        constraint TOPICS_PK
            primary key,
    TP_NAME        VARCHAR2(120)                                                     not null,
    TP_DESCRIPTION VARCHAR2(200)
)
/

create table EXAMS
(
    EX_ID          NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) not null
        constraint EXAMS_PK
            primary key,
    EX_PERCENT     NUMBER                                                            not null,
    TP_ID          NUMBER                                                            not null,
    EX_QUESTION_NO NUMBER,
    EX_TIME        DATE
)
/

create table RESULTS
(
    RS_ID      NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) not null
        constraint RESULTS_PK
            primary key,
    RS_SCORE   NUMBER                                                            not null,
    RS_TIME    DATE,
    USER_US_ID NUMBER                                                            not null
        constraint "RESULTS_USERS_US_ID_fk"
            references USERS,
    EXAM_EX_ID NUMBER                                                            not null
        constraint "RESULTS_EXAMS_EX_ID_fk"
            references EXAMS
)
/

create table QUESTIONS
(
    QU_ID      NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) not null
        constraint QUESTIONS_PK
            primary key,
    QU_CONTENT VARCHAR2(500)                                                     not null,
    QU_OPTION1 VARCHAR2(200),
    QU_OPTION2 VARCHAR2(200),
    QU_OPTION3 VARCHAR2(200),
    QU_OPTION4 VARCHAR2(200),
    QU_ANSWER  NUMBER(1)                                                         not null
        check (qu_answer IN (1, 2, 3, 4)),
    QU_EXPLAIN CLOB,
    QU_TYPE    NUMBER(1),
    TP_ID      NUMBER                                                            not null
        constraint "QUESTIONS_TOPICS_TP_ID_fk"
            references TOPICS,
    LV_ID      NUMBER                                                            not null
)
/

create table EXAM_TOPIC
(
    ET_ID      NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) not null
        constraint EXAM_TOPIC_PK
            primary key,
    EX_ID      NUMBER                                                            not null
        constraint "EXAM_TOPIC_EXAMS_EX_ID_fk"
            references EXAMS,
    TP_ID      NUMBER                                                            not null
        constraint "EXAM_TOPIC_TOPICS_TP_ID_fk"
            references TOPICS,
    ET_PERCENT NUMBER
)
/

truncate table USERS;
INSERT INTO USERS(us_username, us_password, us_fullname, us_address, us_mobile_number, us_email, us_created_date,
                  us_status, US_GROUP)
values ('root', '$2a$10$w3iJtF51PJWsTW.WR/reKu5d3GSoX6dPBfyE2M7KZVCILJFDxxPDW', 'My Admin', 'MyAddress', '0123456789',
        'myemail@gmail.com', SYSDATE, 1, 0);
INSERT INTO USERS(us_username, us_password, us_fullname, us_address, us_mobile_number, us_email, us_created_date,
                  us_status, US_GROUP)
values ('user1', '$2a$10$Zx0CwehPR9vMnjyLMHuALu3PSqjTUHHaLU60PxaTjtU.8DSwZZixW', 'My User', 'MyAddress', '0123456798',
        'myuser@gmail.com', SYSDATE, 1, 1);
commit;