package com.klasix12.dto.question;

import com.klasix12.dto.*;

import java.util.List;

public record MatchingQuestionDto(
        Long id,
        String title,
        String body,
        Difficulty difficulty,
        QuestionType questionType,
        Integer rating,
        Integer views,
        UserDto author,
        List<TagDto> tags,
        List<MatchPairDto> pairs,
        List<MatchPairDto> userPairs
) implements QuestionDto {}
