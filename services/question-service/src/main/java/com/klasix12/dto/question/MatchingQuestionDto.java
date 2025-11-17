package com.klasix12.dto.question;

import com.klasix12.dto.MatchPairDto;
import com.klasix12.dto.TagDto;
import com.klasix12.dto.UserDto;
import lombok.Builder;

import java.util.List;

@Builder
public record MatchingQuestionDto(
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
        List<MatchPairDto> pairs,
        List<MatchPairDto> userPairs
) implements QuestionDto {
}
