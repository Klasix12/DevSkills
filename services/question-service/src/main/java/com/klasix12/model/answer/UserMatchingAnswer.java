package com.klasix12.model.answer;


import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "user_matching_answers")
public class UserMatchingAnswer extends UserAnswer {
    @OneToMany(mappedBy = "userMatchingAnswer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserMatchPair> pairs = new ArrayList<>();
}
