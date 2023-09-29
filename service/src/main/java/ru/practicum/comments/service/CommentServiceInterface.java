package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentResponseDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;

import java.util.List;

public interface CommentServiceInterface {

    CommentResponseDto save(Long userId, Long eventId, NewCommentDto dto);

    CommentResponseDto update(Long userId, Long commentId, UpdateCommentDto dto);

    void deleteByUser(Long userId, Long commentId);

    void deleteByAdmin(Long commentId);

    List<CommentResponseDto> findAllByUser(Long userId);

    List<CommentResponseDto> findAllByEvent(Long userId, Long eventId);

    void doReaction(Long userId, Long commentId, Boolean positive);

}
