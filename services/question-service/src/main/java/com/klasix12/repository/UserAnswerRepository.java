package com.klasix12.repository;

import com.klasix12.model.User;
import com.klasix12.model.answer.UserAnswer;
import com.klasix12.model.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    Optional<UserAnswer> findByQuestionAndUser(Question question, User user);
}
