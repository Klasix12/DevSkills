package com.klasix12.dto.question;

import com.klasix12.dto.*;
import lombok.Builder;

import java.util.List;

@Builder
public record SingleChoiceQuestionDto(
        Long id,
        String title,
        String body,
        Difficulty difficulty,
        QuestionType questionType,
        Integer rating,
        Integer views,
        UserDto author,
        List<TagDto> tags,
        List<OptionDto> options,
        Integer correctOptionIndex,
        OptionDto userOption
) implements QuestionDto {}
