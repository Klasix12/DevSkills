package com.klasix12.model.question;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "free_text_questions")
public class FreeTextQuestion extends Question {
    private String correctAnswer;
}
