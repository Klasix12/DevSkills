package com.klasix12.mapper;

import com.klasix12.dto.question.*;
import com.klasix12.model.answer.*;
import com.klasix12.model.question.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionMapper {

    public static QuestionDto toDto(Question question, UserAnswer answer) {
        if (question instanceof FreeTextQuestion q) {
            return freeTextQuestionDto(q, answer);
        } else if (question instanceof SingleChoiceQuestion q) {
            return singleChoiceQuestionDto(q, answer);
        } else if (question instanceof MultipleChoiceQuestion q) {
            return multipleChoiceQuestionDto(q, answer);
        } else if (question instanceof MatchingQuestion q) {
            return matchingQuestionDto(q, answer);
        }
        throw new IllegalArgumentException("Unknown question type: " + question.getClass().getName());
    }

    public static QuestionDto toDto(Question question) {
        return toDto(question, null);
    }

    private static MatchingQuestionDto matchingQuestionDto(MatchingQuestion question, UserAnswer answer) {
        return MatchingQuestionDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .body(question.getBody())
                .difficulty(question.getDifficulty())
                .questionType(question.getType())
                .rating(question.getRating())
                .views(question.getViews())
                .author(UserMapper.toDto(question.getAuthor()))
                .tags(TagMapper.toDto(question.getTags()))
                .pairs(MatchPairMapper.toDto(question.getParis()))
                .userPairs(answer instanceof UserMatchingAnswer a ? MatchPairMapper.fromUserPairToDto(a.getPairs()) : null)
                .build();
    }

    private static SingleChoiceQuestionDto singleChoiceQuestionDto(SingleChoiceQuestion question, UserAnswer answer) {
        return SingleChoiceQuestionDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .body(question.getBody())
                .difficulty(question.getDifficulty())
                .questionType(question.getType())
                .rating(question.getRating())
                .views(question.getViews())
                .author(UserMapper.toDto(question.getAuthor()))
                .tags(TagMapper.toDto(question.getTags()))
                .options(OptionMapper.toDto(question.getOptions()))
                .correctOptionIndex(question.getCorrectOptionIndex())
                .userOption(answer instanceof UserSingleChoiceAnswer a ? OptionMapper.toDto(a.getSelectedOption()) : null)
                .build();
    }

    private static MultipleChoiceQuestionDto multipleChoiceQuestionDto(MultipleChoiceQuestion question, UserAnswer answer) {
        return MultipleChoiceQuestionDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .body(question.getBody())
                .difficulty(question.getDifficulty())
                .questionType(question.getType())
                .rating(question.getRating())
                .views(question.getViews())
                .author(UserMapper.toDto(question.getAuthor()))
                .tags(TagMapper.toDto(question.getTags()))
                .options(OptionMapper.toDto(question.getOptions()))
                .correctOptionIndexes(question.getCorrectOptionIndexes())
                .userOptions(answer instanceof UserMultipleChoiceAnswer a ? OptionMapper.toDto(a.getSelectedOptions()) : null)
                .build();
    }

    private static FreeTextQuestionDto freeTextQuestionDto(FreeTextQuestion question, UserAnswer answer) {
        return FreeTextQuestionDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .body(question.getBody())
                .difficulty(question.getDifficulty())
                .questionType(question.getType())
                .rating(question.getRating())
                .views(question.getViews())
                .author(UserMapper.toDto(question.getAuthor()))
                .tags(TagMapper.toDto(question.getTags()))
                .correctAnswer(question.getCorrectAnswer())
                .userAnswer(answer instanceof UserFreeTextAnswer a ? a.getAnswer() : null)
                .build();
    }


}
