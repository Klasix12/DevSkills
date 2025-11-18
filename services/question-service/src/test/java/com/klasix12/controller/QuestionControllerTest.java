package com.klasix12.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klasix12.dto.*;
import com.klasix12.dto.question.*;
import com.klasix12.dto.question_request.FreeTextQuestionRequestDto;
import com.klasix12.dto.question_request.MatchingQuestionRequestDto;
import com.klasix12.dto.question_request.MultipleChoiceQuestionRequestDto;
import com.klasix12.dto.question_request.SingleChoiceQuestionRequestDto;
import com.klasix12.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuestionService questionService;

    @Autowired
    private ObjectMapper mapper;

    private SingleChoiceQuestionDto singleChoiceQuestionResponse;
    private SingleChoiceQuestionRequestDto singleChoiceQuestionRequest;
    private MultipleChoiceQuestionDto multipleChoiceQuestionResponse;
    private MultipleChoiceQuestionRequestDto multipleChoiceQuestionRequest;
    private MatchingQuestionDto matchingQuestionResponse;
    private MatchingQuestionRequestDto matchingQuestionRequest;
    private FreeTextQuestionDto freeTextQuestionResponse;
    private FreeTextQuestionRequestDto freeTextQuestionRequest;

    @BeforeEach
    void setUp() {
        singleChoiceQuestionResponse = new SingleChoiceQuestionDto(1L,
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

        singleChoiceQuestionRequest = new SingleChoiceQuestionRequestDto(List.of("1", "2", "3"), 3);
        singleChoiceQuestionRequest.setTitle("single choice title");
        singleChoiceQuestionRequest.setBody("single choice body");
        singleChoiceQuestionRequest.setDifficulty(Difficulty.EASY);
        singleChoiceQuestionRequest.setTagIds(List.of(1L, 2L, 3L));
        singleChoiceQuestionRequest.setQuestionType(QuestionType.SINGLE_CHOICE);

        multipleChoiceQuestionResponse = new MultipleChoiceQuestionDto(1L,
                "title",
                "body",
                Difficulty.EASY,
                false,
                QuestionType.MULTIPLE_CHOICE,
                0,
                0,
                new UserDto(1L, "username"),
                List.of(new TagDto(1L, "tagName", TagGroup.PROGRAM_LANGUAGE)),
                List.of(new OptionDto(1L, "optionText"), new OptionDto(2L, "option2Text")),
                List.of(1, 2),
                null);

        multipleChoiceQuestionRequest = new MultipleChoiceQuestionRequestDto(List.of("1", "2", "3"), List.of(1, 2));
        multipleChoiceQuestionRequest.setTitle("multiple choice title");
        multipleChoiceQuestionRequest.setBody("multiple choice body");
        multipleChoiceQuestionRequest.setDifficulty(Difficulty.EASY);
        multipleChoiceQuestionRequest.setTagIds(List.of(1L, 2L, 3L));
        multipleChoiceQuestionRequest.setQuestionType(QuestionType.MULTIPLE_CHOICE);

        matchingQuestionResponse = new MatchingQuestionDto(1L,
                "title",
                "body",
                Difficulty.EASY,
                false,
                QuestionType.MULTIPLE_CHOICE,
                0,
                0,
                new UserDto(1L, "username"),
                List.of(new TagDto(1L, "tagName", TagGroup.PROGRAM_LANGUAGE)),
                List.of(new MatchPairDto(1L, "left1", "right1"),
                        new MatchPairDto(2L, "left2", "right2"),
                        new MatchPairDto(3L, "left3", "right3")),
                null);

        matchingQuestionRequest = new MatchingQuestionRequestDto(Map.of("1", "1", "2", "2", "3", "3"));
        matchingQuestionRequest.setTitle("matching choice title");
        matchingQuestionRequest.setBody("matching choice body");
        matchingQuestionRequest.setDifficulty(Difficulty.EASY);
        matchingQuestionRequest.setTagIds(List.of(1L, 2L, 3L));
        matchingQuestionRequest.setQuestionType(QuestionType.MATCHING);

        freeTextQuestionResponse = new FreeTextQuestionDto(1L,
                "title",
                "body",
                Difficulty.EASY,
                false,
                QuestionType.MULTIPLE_CHOICE,
                0,
                0,
                new UserDto(1L, "username"),
                List.of(new TagDto(1L, "tagName", TagGroup.PROGRAM_LANGUAGE)),
                "correct",
                null);
        freeTextQuestionRequest = new FreeTextQuestionRequestDto("answer");
        freeTextQuestionRequest.setTitle("free text title");
        freeTextQuestionRequest.setBody("free text body");
        freeTextQuestionRequest.setDifficulty(Difficulty.EASY);
        freeTextQuestionRequest.setTagIds(List.of(1L, 2L, 3L));
        freeTextQuestionRequest.setQuestionType(QuestionType.FREE_TEXT);
    }

    @Nested
    @DisplayName("GET /question - get question tests")
    class GetQuestionTests {

        @Test
        void shouldReturnQuestionById() throws Exception {
            when(questionService.getQuestionById(any(), any())).thenReturn(singleChoiceQuestionResponse);

            mockMvc.perform(get("/question/1")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.title").value("title"));
        }
    }

    @Nested
    @DisplayName("POST /question - create question tests")
    class CreateQuestionTests {

        @Test
        void shouldReturnSingleChoiceQuestionResponse_whenSingleQuestionRequestIsValid() throws Exception {
            when(questionService.addQuestion(any(), any())).thenReturn(singleChoiceQuestionResponse);

            mockMvc.perform(post("/question")
                    .content(mapper.writeValueAsString(singleChoiceQuestionRequest))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L));
        }

        @Test
        void shouldReturnBadRequest_whenSingleChoiceQuestionRequestIsInvalid() throws Exception {
            // с пустым списком ответов валидация не пройдет
            singleChoiceQuestionRequest.setOptions(List.of());
            mockMvc.perform(post("/question")
                            .content(mapper.writeValueAsString(singleChoiceQuestionRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnMultipleChoiceQuestionResponse_whenMultipleChoiceQuestionRequestIsValid() throws Exception {
            when(questionService.addQuestion(any(), any())).thenReturn(multipleChoiceQuestionResponse);

            mockMvc.perform(post("/question")
                    .content(mapper.writeValueAsString(multipleChoiceQuestionRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L));
        }

        @Test
        void shouldReturnBadRequest_whenMultipleChoiceQuestionRequestIsInvalid() throws Exception {
            multipleChoiceQuestionRequest.setOptions(List.of());

            mockMvc.perform(post("/question").content(mapper.writeValueAsString(multipleChoiceQuestionRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnMatchingQuestionResponse_whenMatchingQuestionRequestIsValid() throws Exception {
            when(questionService.addQuestion(any(), any())).thenReturn(matchingQuestionResponse);

            mockMvc.perform(post("/question").content(mapper.writeValueAsString(matchingQuestionRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L));
        }

        @Test
        void shouldReturnBadRequest_whenMatchingQuestionRequestIsInvalid() throws Exception {
            matchingQuestionRequest.setPairs(Map.of());

            mockMvc.perform(post("/question").content(mapper.writeValueAsString(matchingQuestionRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnFreeQuestionResponse_whenFreeQuestionRequestIsValid() throws Exception {
            when(questionService.addQuestion(any(), any())).thenReturn(freeTextQuestionResponse);

            mockMvc.perform(post("/question").content(mapper.writeValueAsString(freeTextQuestionRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L));
        }

        @Test
        void shouldReturnBadRequest_whenFreeQuestionRequestIsInvalid() throws Exception {
            freeTextQuestionRequest.setAnswer("   ");

            mockMvc.perform(post("/question").content(mapper.writeValueAsString(freeTextQuestionRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
}
