package com.klasix12.controller;

import com.klasix12.context.UserContext;
import com.klasix12.dto.question.QuestionDto;
import com.klasix12.dto.question_request.QuestionRequestDto;
import com.klasix12.resolver.CurrentUser;
import com.klasix12.service.QuestionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
@AllArgsConstructor
@Validated
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/{id}")
    public QuestionDto getQuestion(@NotNull @PathVariable Long id,
                                   @CurrentUser UserContext user) {
        return questionService.getQuestionById(id, user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDto addQuestion(@Valid @RequestBody QuestionRequestDto questionRequestDto,
                                   @CurrentUser UserContext user) {
        return questionService.addQuestion(questionRequestDto, user);
    }
}
