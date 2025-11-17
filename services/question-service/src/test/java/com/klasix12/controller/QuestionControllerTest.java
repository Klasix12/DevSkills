package com.klasix12.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klasix12.dto.OptionDto;
import com.klasix12.dto.TagDto;
import com.klasix12.dto.TagGroup;
import com.klasix12.dto.UserDto;
import com.klasix12.dto.question.Difficulty;
import com.klasix12.dto.question.QuestionDto;
import com.klasix12.dto.question.QuestionType;
import com.klasix12.dto.question.SingleChoiceQuestionDto;
import com.klasix12.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuestionService questionService;

    @Test
    void shouldReturnQuestionById() throws Exception {
        QuestionDto dto = new SingleChoiceQuestionDto(1L,
                "title",
                "body",
                Difficulty.EASY,
                false,
                QuestionType.SINGLE_CHOICE,
                0,
                0,
                new UserDto(1L, "username"),
                List.of(new TagDto(1L, "tagName", TagGroup.PROGRAM_LANGUAGE)),
                List.of(new OptionDto(1L, "optionText"), new OptionDto(2L, "option2Text")),
                1,
                null);
        when(questionService.getQuestionById(any(), any())).thenReturn(dto);

        mockMvc.perform(get("/question/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("title"));
    }
}
