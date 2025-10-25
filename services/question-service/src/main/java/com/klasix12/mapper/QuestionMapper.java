package com.klasix12.mapper;

import com.klasix12.dto.question.FreeTextQuestionDto;
import com.klasix12.dto.question.QuestionDto;
import com.klasix12.model.question.FreeTextQuestion;
import com.klasix12.model.question.Question;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionMapper {

    public static QuestionDto toDto(Question question, String userAnswer) {
        if (question instanceof FreeTextQuestion q) {
            return new FreeTextQuestionDto(
                    q.getId(),
                    q.getTitle(),
                    q.getBody(),
                    q.getDifficulty(),
                    q.getType(),
                    q.getRating(),
                    q.getViews(),
                    UserMapper.toDto(q.getAuthor()),
                    TagMapper.toDto(q.getTags()),
                    q.getCorrectAnswer(),
                    userAnswer
            );
        }
        return null;
    }
}
