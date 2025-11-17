package com.klasix12.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MatchPairDto {
    private Long id;
    private String leftText;
    private String rightText;
}
