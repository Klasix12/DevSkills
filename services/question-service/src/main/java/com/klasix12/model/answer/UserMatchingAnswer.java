package com.klasix12.model.answer;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
