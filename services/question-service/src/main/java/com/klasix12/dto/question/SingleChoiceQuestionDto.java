package com.klasix12.dto.question;

import com.klasix12.dto.OptionDto;
import com.klasix12.dto.TagDto;
import com.klasix12.dto.UserDto;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
public record SingleChoiceQuestionDto(
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
        Integer correctOptionIndex,
        OptionDto userOption
) implements QuestionDto {
}
