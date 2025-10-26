package com.klasix12.mapper;

import com.klasix12.dto.OptionDto;
import com.klasix12.model.Option;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionMapper {

    public static OptionDto toDto(Option option) {
        return OptionDto.builder()
                .id(option.getId())
                .text(option.getText())
                .build();
    }

    public static List<OptionDto> toDto(List<Option> options) {
        return options.stream()
                .map(OptionMapper::toDto)
                .toList();
    }
}
