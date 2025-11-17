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
        switch (questionRequest.getQuestionType()) {
            case SINGLE_CHOICE:
                SingleChoiceQuestion singleQuestion = (SingleChoiceQuestion) QuestionFabric.createQuestionFromQuestionRequest(questionRequest, user, tags);
                int correctIndex = questionRequest instanceof SingleChoiceQuestionRequestDto q ? q.getCorrectOptionIndex() : -1;
                if (correctIndex == -1) {
                    throw new RuntimeException("Incorrect correct index");
                }
                singleQuestion.setCorrectOptionIndex(correctIndex);
                singleQuestion.setOptions(extractOptions(questionRequest, singleQuestion));
                return QuestionMapper.toDto(questionRepository.save(singleQuestion));
            case MULTIPLE_CHOICE:
                MultipleChoiceQuestion multipleQuestion = (MultipleChoiceQuestion) QuestionFabric.createQuestionFromQuestionRequest(questionRequest, user, tags);
                List<Integer> correctIndexes = questionRequest instanceof MultipleChoiceQuestionRequestDto q ? q.getCorrectOptionIndexes() : null;
                if (correctIndexes == null || correctIndexes.isEmpty()) {
                    throw new RuntimeException("Incorrect correct indexes");
                }
                multipleQuestion.setCorrectOptionIndexes(correctIndexes);
                multipleQuestion.setOptions(extractOptions(questionRequest, multipleQuestion));
                return QuestionMapper.toDto(questionRepository.save(multipleQuestion));
            case MATCHING:
                MatchingQuestion matchingQuestion = (MatchingQuestion) QuestionFabric.createQuestionFromQuestionRequest(questionRequest, user, tags);
                Map<String, String> pairs = questionRequest instanceof MatchingQuestionRequestDto q ? q.getPairs() : null;
                if (pairs == null || pairs.isEmpty()) {
                    throw new RuntimeException("Incorrect pairs");
                }
                List<MatchPair> matchPairs = createMatchPairs(pairs, matchingQuestion);
                matchingQuestion.setParis(matchPairs);
                return QuestionMapper.toDto(questionRepository.save(matchingQuestion));
            case FREE_TEXT:
                FreeTextQuestion freeTextQuestion = (FreeTextQuestion) QuestionFabric.createQuestionFromQuestionRequest(questionRequest, user, tags);
                String answer = questionRequest instanceof FreeTextQuestionRequestDto q ? q.getAnswer() : null;
                if (answer == null || answer.isEmpty()) {
                    throw new RuntimeException("Incorrect answer");
                }
                freeTextQuestion.setCorrectAnswer(answer);
                return QuestionMapper.toDto(questionRepository.save(freeTextQuestion));
            default:
                throw new RuntimeException("Unknown question type");
        }
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
