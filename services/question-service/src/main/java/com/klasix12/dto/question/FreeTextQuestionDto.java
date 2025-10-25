package com.klasix12.dto.question;

import com.klasix12.dto.TagDto;
import com.klasix12.dto.UserDto;

import java.util.List;

public record FreeTextQuestionDto(
        Long id,
        String title,
        String body,
        Difficulty difficulty,
        QuestionType questionType,
        Integer rating,
        Integer views,
        UserDto author,
        List<TagDto> tags,
        String correctAnswer,
        String userAnswer
) implements QuestionDto {}
