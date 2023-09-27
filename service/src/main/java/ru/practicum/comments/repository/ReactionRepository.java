package ru.practicum.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comments.model.Reaction;
import ru.practicum.comments.model.ReactionId;

public interface ReactionRepository extends JpaRepository<Reaction, ReactionId> {

    Long countAllByReactionIdCommentIdAndPositive(Long commentId, Boolean positive);
}
