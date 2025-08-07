package com.incubadora.incubadora.dev.mapper;

import com.incubadora.incubadora.dev.dto.TechnologyDto;
import com.incubadora.incubadora.dev.entity.common.Technology;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TechnologyMapper {
    TechnologyDto toDto(Technology technology);

}
