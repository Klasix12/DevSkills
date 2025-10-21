package com.klasix12.service;

import com.klasix12.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    /*
    такс такс такс что тут у нас
    Вопросы:
    получение всех вопросов постранично
    получение одного вопроса
    получение ответов на вопрос

    получение всех тегов
    получение вопросов по тегам (одному и нескольким)

    с получением больше одного вопроса прикрутить ко всему сортировку (даты, рейтинг, просмотры)
     */
}
