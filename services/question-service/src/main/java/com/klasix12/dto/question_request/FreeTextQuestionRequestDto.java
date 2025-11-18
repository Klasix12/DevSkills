package com.klasix12.dto.question_request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class FreeTextQuestionRequestDto extends QuestionRequestDto {
    @NotBlank(message = "Ответ не должен быть пустым")
    private String answer;
}
