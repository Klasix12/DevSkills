package com.klasix12.service;

import com.klasix12.dto.question_request.QuestionRequestDto;
import com.klasix12.model.Tag;
import com.klasix12.model.User;
import com.klasix12.model.question.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionFabric {

    public static Question createQuestionFromQuestionRequest(QuestionRequestDto questionRequest, User author, List<Tag> tags) {
        Question question;
        switch (questionRequest.getQuestionType()) {
            case SINGLE_CHOICE -> question = new SingleChoiceQuestion();
            case MULTIPLE_CHOICE -> question = new MultipleChoiceQuestion();
            case MATCHING -> question = new MatchingQuestion();
            case FREE_TEXT -> question = new FreeTextQuestion();
            default -> throw new RuntimeException("Unsupported question type");
        }
        question.setTitle(questionRequest.getTitle());
        question.setBody(questionRequest.getBody());
        question.setDifficulty(questionRequest.getDifficulty());
        question.setApproved(false);
        question.setType(questionRequest.getQuestionType());
        question.setCreatedAt(LocalDateTime.now());
        question.setAuthor(author);
        question.setTags(tags);
        System.out.println(question);
        return question;
    }
}
