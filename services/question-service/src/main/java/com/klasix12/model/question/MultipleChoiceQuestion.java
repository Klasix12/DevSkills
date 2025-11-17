package com.klasix12.model.question;

import com.klasix12.model.Option;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "multiple_choice_questions")
public class MultipleChoiceQuestion extends Question {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Option> options;

    @ElementCollection
    @CollectionTable(
            name = "multiple_choice_correct_indexes",
            joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "correct_option_index")
    private List<Integer> correctOptionIndexes;
}
