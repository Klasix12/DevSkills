package com.klasix12.model.answer;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "user_free_text_answers")
public class UserFreeTextAnswer extends UserAnswer {
    private String answer;
}
