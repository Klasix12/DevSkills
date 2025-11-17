package com.klasix12.dto.question_request;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.klasix12.dto.question.Difficulty;
import com.klasix12.dto.question.FreeTextQuestionDto;
import com.klasix12.dto.question.MultipleChoiceQuestionDto;
import com.klasix12.dto.question.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "questionType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleChoiceQuestionRequestDto.class, name = "SINGLE_CHOICE"),
        @JsonSubTypes.Type(value = MultipleChoiceQuestionRequestDto.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = MatchingQuestionRequestDto.class, name = "MATCHING"),
        @JsonSubTypes.Type(value = FreeTextQuestionRequestDto.class, name = "FREE_TEXT"),
})
@Getter
@Setter
public sealed abstract class QuestionRequestDto permits
        SingleChoiceQuestionRequestDto,
        MultipleChoiceQuestionRequestDto,
        MatchingQuestionRequestDto,
        FreeTextQuestionRequestDto {
    @NotBlank
    @Size(min = 12, max = 255, message = "Заголовок вопроса не должен быть пустым")
    private String title;

    @NotBlank(message = "Тело вопроса не должно быть пустым")
    private String body;

    @NotNull(message = "Сложность вопроса не должна быть пустой")
    private Difficulty difficulty;

    @NotNull(message = "Тип вопроса не может быть пустым")
    private QuestionType questionType;

    @NotNull(message = "Список с тегами не может быть пустым")
    @Size(min = 1, max = 20, message = "Количество тегов должно быть от 1 до 20")
    private List<Long> tagIds;
}
