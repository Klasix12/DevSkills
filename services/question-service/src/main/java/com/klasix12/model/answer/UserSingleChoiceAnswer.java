package com.klasix12.model.answer;

import com.klasix12.model.Option;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "user_single_choice_answers")
public class UserSingleChoiceAnswer extends UserAnswer {

    @ManyToOne
    @JoinColumn(name = "selected_option_id")
    private Option selectedOption;
}
