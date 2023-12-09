package ru.practicum.mainservice.comment.model;

import lombok.*;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.statuses.CommentStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_comments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false, length = 7000)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentator_id", nullable = false)
    private User commentator;

    @Column(name = "created", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "published")
    private LocalDateTime publishedOn;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;
}
