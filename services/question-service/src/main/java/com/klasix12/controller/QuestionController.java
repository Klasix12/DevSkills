package com.klasix12.controller;

import com.klasix12.context.UserContext;
import com.klasix12.dto.Constants;
import com.klasix12.resolver.CurrentUser;
import com.klasix12.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
@AllArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/{id}")
    public UserContext getQuestion(@PathVariable int id, @CurrentUser UserContext user) {
        System.out.println(user);
        return user;
    }
}
