package com.klasix12.dto.question;

import com.klasix12.dto.OptionDto;
import com.klasix12.dto.TagDto;
import com.klasix12.dto.UserDto;
import lombok.Builder;

import java.util.List;

@Builder
public record MultipleChoiceQuestionDto(
        Long id,
        String title,
        String body,
        Difficulty difficulty,
        Boolean approved,
        QuestionType questionType,
        Integer rating,
        Integer views,
        UserDto author,
        List<TagDto> tags,
        List<OptionDto> options,
        List<Integer> correctOptionIndexes,
        List<OptionDto> userOptions
) implements QuestionDto {
}
