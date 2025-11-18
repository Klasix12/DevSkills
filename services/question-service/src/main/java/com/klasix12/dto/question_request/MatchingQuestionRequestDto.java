package com.klasix12.dto.question_request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class MatchingQuestionRequestDto extends QuestionRequestDto {
    @NotNull(message = "Пары ответов должны быть указаны")
    @Size(min = 2, max = 10, message = "Должно быть как минимум 2 пары ответов")
    private Map<String, String> pairs;
}
