package com.klasix12.mapper;

import com.klasix12.dto.MatchPairDto;
import com.klasix12.model.MatchPair;
import com.klasix12.model.answer.UserMatchPair;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchPairMapper {

    public static MatchPairDto toDto(MatchPair pair) {
        return MatchPairDto.builder()
                .id(pair.getId())
                .leftText(pair.getLeftText())
                .rightText(pair.getRightText())
                .build();
    }

    public static List<MatchPairDto> toDto(List<MatchPair> pairs) {
        return pairs.stream()
                .map(MatchPairMapper::toDto)
                .toList();
    }

    public static MatchPairDto toDto(UserMatchPair pair) {
        return MatchPairDto.builder()
                .id(pair.getId())
                .leftText(pair.getLeftText())
                .rightText(pair.getRightText())
                .build();
    }

    public static List<MatchPairDto> fromUserPairToDto(List<UserMatchPair> pairs) {
        return pairs.stream()
                .map(MatchPairMapper::toDto)
                .toList();
    }
}
