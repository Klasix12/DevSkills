package com.klasix12.dto.question_request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public final class MatchingQuestionRequestDto extends QuestionRequestDto {
    private Map<String, String> pairs;
}
