package com.klasix12.dto.question_request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class MultipleChoiceQuestionRequestDto extends QuestionRequestDto {
    @NotNull(message = "Ответы на вопрос не должны быть пустыми")
    @Size(min = 2, max = 10, message = "Ответов на вопрос должно быть от 2 до 10")
    private List<String> options;

    @NotNull(message = "Индексы ответов на вопросы не должны быть пустыми")
    @Size(min = 1, max = 10, message = "Правильных ответов на вопросы должно быть от 1 до 10")
    private List<Integer> correctOptionIndexes;
}
