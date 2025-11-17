package com.klasix12.dto.question_request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class MultipleChoiceQuestionRequestDto extends QuestionRequestDto {
    private List<String> options;
    private List<Integer> correctOptionIndexes;
}
