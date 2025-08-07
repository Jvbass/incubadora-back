package com.incubadora.incubadora.dev.mapper;

import com.incubadora.incubadora.dev.dto.RegisterRequest;
import com.incubadora.incubadora.dev.entity.core.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "developedProjects", ignore = true)
    @Mapping(target = "createdTeamProjects", ignore = true)
    @Mapping(target = "teamMemberships", ignore = true)
    @Mapping(target = "givenProjectFeedbacks", ignore = true)
    @Mapping(target = "givenTeamFeedbacks", ignore = true)
    @Mapping(target = "receivedTeamFeedbacks", ignore = true)
    @Mapping(target = "mentorServices", ignore = true)
    @Mapping(target = "menteeSessions", ignore = true)
    @Mapping(target = "mentorSessions", ignore = true)
    @Mapping(target = "givenMentorFeedbacks", ignore = true)
    @Mapping(target = "receivedMentorFeedbacks", ignore = true)
    @Mapping(target = "jobOffers", ignore = true)
    @Mapping(target = "newsAuthored", ignore = true)
    User toUser(RegisterRequest request);
}
