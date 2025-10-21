package com.klasix12.model.question;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "free_text_questions")
public class FreeTextQuestion extends Question {
    private String correctAnswer;
}
