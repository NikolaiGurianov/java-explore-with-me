package ru.practicum.mainservice.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.statuses.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    default Comment get(long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Комментарий с идентификатором #" +
                id + " не зарегистрирован!"));
    }

    @Query("SELECT c FROM Comment c " +
            "WHERE c.event.id = :eventId " +
            "AND c.status = 'PUBLISHED'")
    List<Comment> findAllByEventAndStatus(Long eventId, Pageable pageable);

    @Query("SELECT c FROM Comment c " +
            "WHERE (:users IS NULL OR c.commentator.id IN (:users)) " +
            "AND (:text IS NULL OR LOWER(c.text) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND (:states IS NULL OR c.status IN (:states)) " +
            "AND (:events IS NULL OR c.event.id IN (:events)) " +
            "AND (cast(:rangeStart AS timestamp) IS NULL OR c.publishedOn >= :rangeStart) " +
            "AND (cast(:rangeEnd AS timestamp) IS NULL OR c.publishedOn <= :rangeEnd)")
    List<Comment> findByParameters(List<Long> users, String text, List<CommentStatus> states, List<Long> events,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
