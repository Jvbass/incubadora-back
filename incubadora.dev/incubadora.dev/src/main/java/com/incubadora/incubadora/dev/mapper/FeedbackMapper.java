package com.incubadora.incubadora.dev.mapper;

import com.incubadora.incubadora.dev.dto.FeedbackResponseDto;
import com.incubadora.incubadora.dev.entity.feedback.FeedbackProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    @Mapping(source = "author.username", target = "author")
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "project.id", target = "projectId")
    FeedbackResponseDto toFeedbackResponseDto(FeedbackProject feedback);

    List<FeedbackResponseDto> toFeedbackResponseDtoList(List<FeedbackProject> feedbacks);
}
