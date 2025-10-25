package com.klasix12.mapper;

import com.klasix12.dto.TagDto;
import com.klasix12.model.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagMapper {

    public static TagDto toDto(Tag tag) {
        return new TagDto(tag.getId(), tag.getName(), tag.getTagGroup());
    }

    public static List<TagDto> toDto(List<Tag> tags) {
        return tags.stream()
                .map(TagMapper::toDto)
                .toList();
    }
}
