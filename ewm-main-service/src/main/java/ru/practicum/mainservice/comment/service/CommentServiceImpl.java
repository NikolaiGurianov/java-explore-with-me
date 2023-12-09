package ru.practicum.mainservice.comment.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.comment.dto.*;
import ru.practicum.mainservice.comment.mapper.CommentMapper;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.comment.repository.CommentRepository;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.model.User;
import ru.practicum.mainservice.service.events.admin.AdministratorEventService;
import ru.practicum.mainservice.service.users.UserService;
import ru.practicum.mainservice.statuses.CommentStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.util.Constants.SORT_BY_ID_ASC;

@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final AdministratorEventService eventService;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Override
    public List<CommentShortDto> getPublishedCommentsForEventPub(Long eventId, int from, int size) {
        log.info("Public: Получен запрос на получение всех опубликованных комментариев для события с ID= {}", eventId);

        List<Comment> commentsList = commentRepository.findAllByEventAndStatus(eventId,
                PageRequest.of(from / size, size, SORT_BY_ID_ASC));
        List<CommentShortDto> commentShortDtos = new ArrayList<>();
        for (Comment comment : commentsList) {
            commentShortDtos.add(CommentMapper.toShortDto(comment));
        }
        log.info("Запрос выполнен. Получено {} комментариев", commentShortDtos.size());

        return commentShortDtos;
    }

    @Override
    public CommentFullDto updateCommentAdm(Long commentId, UpdateCommentRequest updateCommentRequest) {
        log.info("Admin: Получен запрос на модерацию комментария: {}", updateCommentRequest);
        Comment comment = commentRepository.get(commentId);
        if (comment.getStatus().equals(CommentStatus.PUBLISHED)) {
            throw new ConflictException("Комментарий уже опубликован. Изменение невозможно");
        }
        if (updateCommentRequest.getText() != null) {
            comment.setText(updateCommentRequest.getText());
        }
        if (updateCommentRequest.getStateAction().equals(UpdateCommentRequest.StateAction.PUBLISH)) {
            comment.setStatus(CommentStatus.PUBLISHED);
            comment.setPublishedOn(LocalDateTime.now());
        }
        if (updateCommentRequest.getStateAction().equals(UpdateCommentRequest.StateAction.CANCEL)) {
            comment.setStatus(CommentStatus.CANCELED);
        }
        CommentFullDto commentFullDto = CommentMapper.toFullDto(commentRepository.save(comment));

        log.info("Запрос выполнен. Комментарий обновлен: {}", commentFullDto);

        return commentFullDto;
    }

    @Override
    public List<CommentFullDto> getCommentByParamAdm(List<Long> users, String text, List<CommentStatus> statuses,
                                                     List<Long> events, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     int from, int size) {
        log.info("Admin: Получен запрос на получение всех комментариев по параметрам:" +
                        "users:{}, text:{}, statuses:{}, events:{}, rangeStart:{}, rangeEnd:{}",
                users, text, statuses, events, rangeStart, rangeEnd);

        List<Comment> comments = commentRepository.findByParameters(
                users, text, statuses, events, rangeStart, rangeEnd, PageRequest.of(from / size, size, SORT_BY_ID_ASC));
        List<CommentFullDto> commentFullDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentFullDtos.add(CommentMapper.toFullDto(comment));
        }
        log.info("Запрос выполнен. Получено {} комментариев", commentFullDtos.size());
        return commentFullDtos;
    }

    @Override
    public CommentFullDto editCommentPr(Long userId, Long eventId, Long commentId, UpdateCommentRequest updateCommentRequest) {
        log.info("Private: Получен запрос от пользователя ID= {}, для события с ID= {}, изменения комменатария ID= {} с данными: {}",
                userId, eventId, commentId, updateCommentRequest);

        User user = userService.getEntity(userId);
        Comment comment = commentRepository.get(commentId);

        if (!comment.getCommentator().equals(user)) {
            throw new ConflictException("Пользователь не является автором данного комментария. Изменение невозможно");
        }
        if (comment.getStatus().equals(CommentStatus.PUBLISHED)) {
            throw new ConflictException("Комментарий уже опубликован. Изменение невозможно");
        }
        if (updateCommentRequest.getStateAction() == UpdateCommentRequest.StateAction.CHECK) {
            comment.setStatus(CommentStatus.PENDING);
        }
        if (updateCommentRequest.getStateAction() == UpdateCommentRequest.StateAction.CANCEL) {
            comment.setStatus(CommentStatus.CANCELED);
        }
        if (updateCommentRequest.getText() != null) {
            comment.setText(updateCommentRequest.getText());
        }

        CommentFullDto commentFullDto = CommentMapper.toFullDto(commentRepository.save(comment));
        log.info("Запрос выполнен. Комментарий изменен: {}", commentFullDto);

        return commentFullDto;
    }

    @Override
    public void deleteCommentPr(Long userId, Long commentId) {
        log.info("Private: Получен запрос от пользователя ID= {}, для удаления комментария с ID= {}", userId, commentId);

        User user = userService.getEntity(userId);
        Comment comment = commentRepository.get(commentId);
        if (!comment.getCommentator().equals(user)) {
            throw new ConflictException("Пользователь не является автором данного комментария. Удаление невозможно");
        }
        commentRepository.deleteById(commentId);
        log.info("Запрос выполнен. Удален комментарий с ID= {}", commentId);
    }

    @Override
    public CommentFullDto getCommentByIdAdm(Long commentId) {
        return CommentMapper.toFullDto(commentRepository.get(commentId));
    }

    @Override
    public CommentFullDto addCommentPr(Long eventId, Long userId, NewCommentDto newCommentDto) {
        log.info("Private: Получен запрос от пользователя ID= {}, для события с ID= {} на добавление комментария:  {}", userId, eventId, newCommentDto);

        Event event = eventService.getEntity(eventId);
        User user = userService.getEntity(userId);
        Comment comment = CommentMapper.toComment(user, event, newCommentDto);
        CommentFullDto commentFullDto = CommentMapper.toFullDto(commentRepository.save(comment));
        log.info("Запрос выполнен. Добавлен комментарий: {}", commentFullDto);

        return commentFullDto;
    }
}