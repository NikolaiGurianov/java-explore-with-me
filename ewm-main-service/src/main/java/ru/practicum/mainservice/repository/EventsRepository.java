package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.statuses.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<Event, Long> {
    default Event get(long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Событие с ID= {} не зарегистрировано!", id));
    }

    List<Event> findAllByCategoryId(long catId);

    List<Event> findAllByInitiatorId(long initiatorId, Pageable pageable);

    List<Event> findByIdIn(List<Long> eventsIds);

    Optional<Event> findByIdAndState(long eventId, State state);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:users) IS NULL OR e.initiator.id IN (:users)) " +
            "AND ((:states) IS NULL OR e.state IN (:states)) " +
            "AND ((:categories) IS NULL OR e.category.id IN (:categories)) " +
            "AND (cast(:rangeStart AS timestamp) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (cast(:rangeEnd AS timestamp) IS NULL OR e.eventDate <= :rangeEnd)")
    List<Event> findAllByParameters(@Param("users") List<Long> users,
                                    @Param("states") List<State> states,
                                    @Param("categories") List<Long> categories,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd,
                                    Pageable pageable);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE (:text IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND ((:categories) IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (cast(:rangeStart AS timestamp) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (cast(:rangeEnd AS timestamp) IS NULL OR e.eventDate <= :rangeEnd) " +
            "AND e.state = 'PUBLISHED' " +
            "AND (:onlyAvailable IS NOT FALSE OR e.confirmedRequests < e.participantLimit)")
    List<Event> findAllByParametersPub(@Param("text") String text,
                                       @Param("categories") List<Long> categories,
                                       @Param("paid") Boolean paid,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       @Param("onlyAvailable") Boolean onlyAvailable,
                                       Pageable pageable);
}