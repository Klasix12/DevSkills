package com.klasix12.model;

import com.klasix12.model.question.Question;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "match_pairs")
public class MatchPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String leftText;
    private String rightText;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
