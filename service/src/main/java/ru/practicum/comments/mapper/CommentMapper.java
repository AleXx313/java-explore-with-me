package ru.practicum.comments.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.comments.dto.CommentResponseDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.model.Comment;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static Comment dtoToComment(NewCommentDto dto){
        return Comment.builder()
                .status(dto.getStatus())
                .content(dto.getContent())
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentResponseDto commentToResponseDto(Comment comment){
        return CommentResponseDto.builder()
                .id(comment.getId())
                .status(comment.getStatus())
                .content(comment.getContent())
                .created(comment.getCreated())
                .userId(comment.getUser().getId())
                .eventId(comment.getEvent().getId())
                .build();
    }
}
