package ru.practicum.comments.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reactions")
public class Reaction {
    @Id
    private ReactionId reactionId;
    @Column(name = "positive")
    private Boolean positive;

}
