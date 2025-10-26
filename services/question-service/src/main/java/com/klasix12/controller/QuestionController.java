package com.klasix12.controller;

import com.klasix12.context.UserContext;
import com.klasix12.dto.question.QuestionDto;
import com.klasix12.model.question.Question;
import com.klasix12.resolver.CurrentUser;
import com.klasix12.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
@AllArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/{id}")
    public QuestionDto getQuestion(@PathVariable Long id,
                                @CurrentUser UserContext user) {
        return questionService.getQuestionById(id, user);
    }

    @PostMapping
    public QuestionDto addQuestion(@CurrentUser UserContext user) {
        return questionService.addQuestion(user);
    }
}
