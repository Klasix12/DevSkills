package com.klasix12.dto.question_request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public final class FreeTextQuestionRequestDto extends QuestionRequestDto {
    private String answer;
}
