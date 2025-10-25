package com.klasix12.service;

import com.klasix12.context.UserContext;
import com.klasix12.dto.question.Difficulty;
import com.klasix12.dto.question.QuestionType;
import com.klasix12.dto.TagGroup;
import com.klasix12.dto.question.QuestionDto;
import com.klasix12.exception.NotFoundException;
import com.klasix12.mapper.QuestionMapper;
import com.klasix12.model.Tag;
import com.klasix12.model.User;
import com.klasix12.model.question.FreeTextQuestion;
import com.klasix12.model.question.Question;
import com.klasix12.repository.QuestionRepository;
import com.klasix12.repository.TagsRepository;
import com.klasix12.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final TagsRepository tagsRepository;
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

    @Transactional
    public Question getQuestionById(Long id, UserContext user) {
        /*
        1. Получаем вопрос
        2. Проверяем, отвечал ли на него пользователь
        3. Если отвечаел, возвращаем ответы + ответы пользователя
        4. Если не отвечал, возвращаем просто ответы
        5. Добавляем просмотр
         */
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Question not found"));

        return question;
    }

    @Transactional
    public QuestionDto addQuestion(UserContext user) {
        User newUser = new User();
        newUser.setId(user.getUserId());
        newUser.setUsername(user.getUsername());
        newUser.setIsConfirmed(true);
        userRepository.save(newUser);

        Tag newTag = new Tag();
        newTag.setTagGroup(TagGroup.PROGRAM_LANGUAGE);
        newTag.setName("JAVA");
        tagsRepository.save(newTag);
        FreeTextQuestion question = new FreeTextQuestion();
        question.setCorrectAnswer("correctAnswer");
        question.setTitle("title");
        question.setBody("body");
        question.setDifficulty(Difficulty.EASY);
        question.setApproved(true);
        question.setType(QuestionType.FREE_TEXT);
        question.setRating(100);
        question.setViews(123);
        question.setCreatedAt(LocalDateTime.now());
        question.setApprovedAt(LocalDateTime.now());
        question.setAuthor(newUser);
        question.setTags(List.of(newTag));
        questionRepository.save(question);

        return QuestionMapper.toDto(question, "penis");
    }
}
