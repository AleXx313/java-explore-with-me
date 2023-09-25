package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentResponseDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping(path = "/{eventId}")
    public ResponseEntity<CommentResponseDto> save(@PathVariable(value = "userId") Long userId,
                                                   @PathVariable(value = "eventId") Long eventId,
                                                   @RequestBody @Valid NewCommentDto dto) {
        return new ResponseEntity<>(commentService.save(userId, eventId, dto), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{commentId}")
    public ResponseEntity<CommentResponseDto> update(@PathVariable(value = "userId") Long userId,
                                                     @PathVariable(value = "commentId") Long commentId,
                                                     @RequestBody @Valid UpdateCommentDto dto) {
        return new ResponseEntity<>(commentService.update(userId, commentId, dto), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{commentId}")
    public ResponseEntity<?> delete(@PathVariable(value = "userId") Long userId,
                                    @PathVariable(value = "commentId") Long commentId) {
        commentService.deleteByUser(userId, commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<List<CommentResponseDto>> findAllByEvent(@PathVariable(value = "userId") Long userId,
                                                                   @PathVariable(value = "eventId") Long eventId) {
        return new ResponseEntity<>(commentService.findAllByEvent(userId, eventId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> findAllByUser(@PathVariable(value = "userId") Long userId) {
        return new ResponseEntity<>(commentService.findAllByUser(userId), HttpStatus.OK);
    }

    @PostMapping(path = "/{commentId}/reaction")
    public ResponseEntity<?> doReaction(@PathVariable(value = "userId") Long userId,
                                        @PathVariable(value = "commentId") Long commentId,
                                        @RequestParam(defaultValue = "true") Boolean positive) {
        commentService.doReaction(userId, commentId, positive);
        return ResponseEntity.ok().build();
    }
}
