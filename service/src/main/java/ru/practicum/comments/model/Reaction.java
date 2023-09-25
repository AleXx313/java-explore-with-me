package ru.practicum.comments.model;

import lombok.*;
import ru.practicum.user.model.User;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reactions")
public class Reaction{
    @Id
    private ReactionId reactionId;
    @Column(name = "positive")
    private Boolean positive;

//    @ManyToOne
//    @MapsId("commentId")
//    private Comment comment;
//
//    @ManyToOne
//    @MapsId("userId")
//    private User user;
}
