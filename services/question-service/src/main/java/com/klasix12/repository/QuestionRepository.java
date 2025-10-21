package com.klasix12.repository;

import com.klasix12.model.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // 1. Все вопросы постранично
    // 2. Вопросы по тегам
    // 3. Похожие вопросы
    // 4.
    // Хуй знает, потом подумаем
}
