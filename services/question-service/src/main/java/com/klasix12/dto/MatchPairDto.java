package com.klasix12.dto;

import lombok.Builder;

@Builder
public class MatchPairDto {
    private Long id;
    private String leftText;
    private String rightText;
}
