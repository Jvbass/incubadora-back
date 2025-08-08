package com.incubadora.incubadora.dev.mapper;

import com.incubadora.incubadora.dev.dto.AuthorDto;
import com.incubadora.incubadora.dev.dto.CommentResponseDto;
import com.incubadora.incubadora.dev.dto.CreateCommentRequestDto;
import com.incubadora.incubadora.dev.entity.feedback.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;


@Mapper(componentModel = "spring", imports = {Collectors.class, Set.class})
public interface CommentMapper {
    @Mapping(source = "author", target = "author")
    @Mapping(target = "replies", expression = "java(comment.getReplies().stream()" +
            ".map(this::toResponseDto).collect(Collectors.toList()))")
    CommentResponseDto toResponseDto(Comment comment);

    AuthorDto mapAuthor(com.incubadora.incubadora.dev.entity.core.User user); // helper es opcional, pero clarificar cómo se mapea
    // la entidad User a AuthorDto. MapStruct lo generará automáticamente.

    List<CommentResponseDto> toResponseDtoList(List<Comment> comments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "feedbackProject", ignore = true)// Ignorar el mapeo ya que se maneja en toResponseDto
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "parentComment", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment toEntity(CreateCommentRequestDto dto);
}
