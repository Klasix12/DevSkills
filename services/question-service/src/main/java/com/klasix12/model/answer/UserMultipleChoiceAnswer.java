package com.klasix12.model.answer;

import com.klasix12.model.Option;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name = "user_multiple_choice_answers")
public class UserMultipleChoiceAnswer extends UserAnswer {

    @ManyToMany
    @JoinTable(
            name = "user_multiple_choice_selected_options",
            joinColumns = @JoinColumn(name = "user_answer_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private List<Option> selectedOptions;
}
