package com.klasix12.model.answer;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "user_match_pairs")
public class UserMatchPair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String leftText;
    private String rightText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_answer_id")
    private UserMatchingAnswer userMatchingAnswer;
}
