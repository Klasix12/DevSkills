package com.klasix12.service;

import com.klasix12.context.UserContext;
import com.klasix12.dto.question.QuestionDto;
import com.klasix12.dto.question_request.*;
import com.klasix12.exception.NotFoundException;
import com.klasix12.mapper.QuestionMapper;
import com.klasix12.mapper.UserMapper;
import com.klasix12.model.MatchPair;
import com.klasix12.model.Option;
import com.klasix12.model.Tag;
import com.klasix12.model.User;
import com.klasix12.model.answer.UserAnswer;
import com.klasix12.model.question.*;
import com.klasix12.repository.QuestionRepository;
import com.klasix12.repository.TagsRepository;
import com.klasix12.repository.UserAnswerRepository;
import com.klasix12.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final TagsRepository tagsRepository;
    private final UserAnswerRepository userAnswerRepository;


    @Transactional
    public QuestionDto getQuestionById(Long id, UserContext userContext) {
        log.trace("Getting question by id: {}, user: {}", id, userContext);
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Question not found"));
        Optional<UserAnswer> answer = Optional.empty();

        if (userContext == null || !userContext.isPresent()) {
            incrementViews(question);
            return QuestionMapper.toDto(question);
        }
        Optional<User> user = userRepository.findById(userContext.getUserId());
        if (user.isPresent()) {
            log.trace("Getting user answer. userId: {}, questionId: {}", userContext.getUserId(), id);
            answer = userAnswerRepository.findByQuestionAndUser(question, user.get());
        }
        incrementViews(question);
        if (answer.isPresent()) {
            return QuestionMapper.toDto(question, answer.get());
        }
        return QuestionMapper.toDto(question);
    }

    private void incrementViews(Question question) {
        log.info("Incrementing views for question with id: {}", question.getId());
        question.setViews(question.getViews() + 1);
        questionRepository.save(question);
    }

    @Transactional
    public QuestionDto addQuestion(QuestionRequestDto questionRequest, UserContext userContext) {
        log.info("Saving question: {}, user: {}", questionRequest, userContext);
        User user = userRepository.findById(userContext.getUserId())
                .orElseGet(() -> userRepository.save(UserMapper.toEntity(userContext)));
        List<Tag> tags = tagsRepository.findAllById(questionRequest.getTagIds());
        return switch (questionRequest.getQuestionType()) {
            case SINGLE_CHOICE -> QuestionMapper.toDto(saveSingleChoiceQuestion(questionRequest, user, tags));
            case MULTIPLE_CHOICE -> QuestionMapper.toDto(saveMultipleChoiceQuestion(questionRequest, user, tags));
            case MATCHING -> QuestionMapper.toDto(saveMatchingQuestion(questionRequest, user, tags));
            case FREE_TEXT -> QuestionMapper.toDto(saveFreeTextQuestion(questionRequest, user, tags));
        };
    }

    private FreeTextQuestion saveFreeTextQuestion(QuestionRequestDto questionRequest, User user, List<Tag> tags) {
        FreeTextQuestion question = (FreeTextQuestion) QuestionFabric.createQuestionFromQuestionRequest(questionRequest, user, tags);
        String answer = questionRequest instanceof FreeTextQuestionRequestDto q ? q.getAnswer() : null;
        if (answer == null || answer.isEmpty()) {
            log.warn("Not correct answer for request: {}", questionRequest);
            throw new RuntimeException("Incorrect answer");
        }
        question.setCorrectAnswer(answer);
        return saveAndLog(question);
    }

    private MatchingQuestion saveMatchingQuestion(QuestionRequestDto questionRequest, User user, List<Tag> tags) {
        MatchingQuestion question = (MatchingQuestion) QuestionFabric.createQuestionFromQuestionRequest(questionRequest, user, tags);
        Map<String, String> pairs = questionRequest instanceof MatchingQuestionRequestDto q ? q.getPairs() : null;
        if (pairs == null || pairs.isEmpty()) {
            log.warn("Not correct pairs for request: {}", questionRequest);
            throw new RuntimeException("Incorrect pairs");
        }
        List<MatchPair> matchPairs = createMatchPairs(pairs, question);
        question.setParis(matchPairs);
        return saveAndLog(question);
    }

    private MultipleChoiceQuestion saveMultipleChoiceQuestion(QuestionRequestDto questionRequest, User user, List<Tag> tags) {
        MultipleChoiceQuestion question = (MultipleChoiceQuestion) QuestionFabric.createQuestionFromQuestionRequest(questionRequest, user, tags);
        List<Integer> correctIndexes = questionRequest instanceof MultipleChoiceQuestionRequestDto q ? q.getCorrectOptionIndexes() : null;
        if (correctIndexes == null || correctIndexes.isEmpty()) {
            log.warn("Not correct indexes for question request: {}", questionRequest);
            throw new RuntimeException("Incorrect correct indexes");
        }
        question.setCorrectOptionIndexes(correctIndexes);
        question.setOptions(extractOptions(questionRequest, question));
        return saveAndLog(question);
    }

    private SingleChoiceQuestion saveSingleChoiceQuestion(QuestionRequestDto questionRequest, User user, List<Tag> tags) {
        SingleChoiceQuestion question = (SingleChoiceQuestion) QuestionFabric.createQuestionFromQuestionRequest(questionRequest, user, tags);
        int correctIndex = questionRequest instanceof SingleChoiceQuestionRequestDto q ? q.getCorrectOptionIndex() : -1;
        if (correctIndex == -1) {
            log.warn("Not correct index for question request: {}", questionRequest);
            throw new RuntimeException("Incorrect correct index");
        }
        question.setCorrectOptionIndex(correctIndex);
        question.setOptions(extractOptions(questionRequest, question));
        return saveAndLog(question);
    }

    private <T extends Question> T saveAndLog(T question) {
        T savedQuestion = questionRepository.save(question);
        log.info("Saved question: {}", savedQuestion);
        return savedQuestion;
    }


    private List<MatchPair> createMatchPairs(Map<String, String> pairs, Question question) {
        return pairs.entrySet().stream()
                .map(entry -> MatchPair.builder()
                        .leftText(entry.getKey())
                        .rightText(entry.getValue())
                        .question(question)
                        .build())
                .toList();
    }

    private List<Option> extractOptions(QuestionRequestDto questionRequest, Question question) {
        List<String> optionStrings = switch (questionRequest) {
            case SingleChoiceQuestionRequestDto q -> q.getOptions();
            case MultipleChoiceQuestionRequestDto q -> q.getOptions();
            default -> throw new RuntimeException("Incorrect question type");
        };

        return optionStrings.stream()
                .map(o -> Option.builder()
                        .text(o)
                        .question(question)
                        .build())
                .toList();
    }
}
