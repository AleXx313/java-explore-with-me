package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentResponseDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.Reaction;
import ru.practicum.comments.model.ReactionId;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.comments.repository.ReactionRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.CustomBadRequestException;
import ru.practicum.exception.ModelNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;

    private final UserService userService;
    private final EventRepository eventRepository;

    public CommentResponseDto save(Long userId, Long eventId, NewCommentDto dto) {
        User user = userService.findById(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ModelNotFoundException(
                "Событие с id - " + eventId + " отсутствует"));
        if (event.getInitiator().getId().equals(userId)) {
            throw new CustomBadRequestException("Нельзя комментировать свое событие!");
        }
        if (commentRepository.findAllByEventIdAndUserId(eventId, userId).isPresent()) {
            throw new CustomBadRequestException("Можно оставить только 1 комментарий!");
        }
        Comment comment = CommentMapper.dtoToComment(dto);
        comment.setUser(user);
        comment.setEvent(event);
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.commentToResponseDto(savedComment);
    }

    public CommentResponseDto update(Long userId, Long commentId, UpdateCommentDto dto) {
        User user = userService.findById(userId);
        Comment comment = findById(commentId);
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomBadRequestException("Редактировать можно только свой комментарий!");
        }
        if (dto.getStatus() != null && !comment.getStatus().equals(dto.getStatus())) {
            comment.setStatus(dto.getStatus());
        }
        if (dto.getContent() != null && !comment.getContent().equals(dto.getContent())) {
            comment.setContent(dto.getContent());
        }
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.commentToResponseDto(savedComment);
    }

    public void deleteByUser(Long userId, Long commentId) {
        User user = userService.findById(userId);
        Comment comment = findById(commentId);
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomBadRequestException("Нельзя удалить чужой комментарий!");
        }
        commentRepository.deleteById(commentId);
    }

    public void deleteByAdmin(Long commentId) {
        findById(commentId);
        commentRepository.deleteById(commentId);
    }

    public List<CommentResponseDto> findAllByUser(Long userId) {
        userService.findById(userId);
        return commentRepository.findAllByUserId(userId)
                .stream()
                .map(CommentMapper::commentToResponseDto)
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> findAllByEvent(Long userId, Long eventId) {
        try {
            userService.findById(userId);
        } catch (ModelNotFoundException e) {
            throw new ModelNotFoundException("Комментарии могут просматривать только зарегистрированные пользователи!");
        }
        eventRepository.findById(eventId).orElseThrow(() -> new ModelNotFoundException(
                "Событие с id - " + eventId + " отсутствует"));

        return commentRepository.findAllByEventId(eventId)
                .stream()
                .map(CommentMapper::commentToResponseDto)
                .collect(Collectors.toList());
    }

    public void doReaction(Long userId, Long commentId, Boolean positive) {
        User user = userService.findById(userId);
        Comment comment = findById(commentId);
        if (comment.getUser().getId().equals(userId)) {
            throw new CustomBadRequestException("Нельзя лайкать свой комментарий!");
        }
        ReactionId reactionId = new ReactionId(comment, user);
        Optional<Reaction> reactionOptional = reactionRepository.findById(reactionId);
        Reaction reaction;
        if (reactionOptional.isPresent()) {
            reaction = reactionOptional.get();
            if (reaction.getPositive().equals(positive)) {
                reactionRepository.deleteById(reactionId);
            } else {
                reaction.setPositive(positive);
                reactionRepository.save(reaction);
            }
        } else {
            reaction = Reaction.builder().reactionId(reactionId).positive(positive).build();
            reactionRepository.save(reaction);
        }
    }

    //Utility
    private Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new ModelNotFoundException(
                "Комментарий с id " + commentId + " не найден!"));
    }
}
