package com.klasix12.dto.question;

import com.klasix12.dto.TagDto;
import com.klasix12.dto.UserDto;

import java.util.List;

public sealed interface QuestionDto permits
        SingleChoiceQuestionDto,
        MultipleChoiceQuestionDto,
        MatchingQuestionDto,
        FreeTextQuestionDto {
    Long id();
    String title();
    String body();
    Difficulty difficulty();
    QuestionType questionType();
    Integer rating();
    Integer views();
    UserDto author();
    List<TagDto> tags();
}
