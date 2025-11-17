package com.klasix12.model.question;

import com.klasix12.model.MatchPair;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "matching_questions")
public class MatchingQuestion extends Question {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<MatchPair> paris;
}
