package com.klasix12.dto.question_request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class SingleChoiceQuestionRequestDto extends QuestionRequestDto {
    @NotNull(message = "Ответы на вопрос не должны быть пустыми")
    @Size(min = 2, max = 10, message = "Ответов на вопрос должно быть от 2 до 10")
    private List<String> options;

    @NotNull(message = "Индекс ответа на вопрос не должен быть пустым")
    private Integer correctOptionIndex;
}
