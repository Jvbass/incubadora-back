package com.incubadora.incubadora.dev.mapper;

import com.incubadora.incubadora.dev.dto.ProjectResponseDto;
import com.incubadora.incubadora.dev.dto.ProjectSummaryDto;
import com.incubadora.incubadora.dev.entity.project.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//componentModel= "spring" -> integra el mapeador con el ecosistema de Spring (bean, inyección de dependencias, etc.)
// uses = {TechnologyMapper.class} -> indica que delega el mapeo de las tecnologías al TechnologyMapper
@Mapper(componentModel = "spring",
        uses = {TechnologyMapper.class})
public interface ProjectMapper {
    // Mapea un proyecto a ProjectResponseDto
    // en entidad user developer.username -> en DTO developerUsername
    @Mapping(source = "developer.username", target = "developerUsername")
    //
    ProjectResponseDto toProjectResponseDto(Project project);

    @Mapping(source = "developer.username", target = "developerUsername")
    ProjectSummaryDto toProjectSummaryDto(Project project);
}
