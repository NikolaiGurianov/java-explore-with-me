package ru.practicum.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.model.ParticipationRequest;
import ru.practicum.mainservice.statuses.RequestStatus;

import java.util.List;

public interface RequestsRepository extends JpaRepository<ParticipationRequest, Long> {
    Long countByEventIdAndStatus(Long eventId, RequestStatus status);

    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    List<ParticipationRequest> findByRequester_Id(Long requesterId);

    List<ParticipationRequest> findByIdInAndStatus(List<Long> requestIds, RequestStatus status);
}