package com.klasix12.model.question;

import com.klasix12.model.Option;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "multiple_choice_questions")
public class MultipleChoiceQuestion extends Question {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Option> options;

    @ElementCollection
    private List<Integer> correctOptionIndexes;
}
