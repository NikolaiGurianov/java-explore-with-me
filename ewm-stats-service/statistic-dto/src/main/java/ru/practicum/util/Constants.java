package ru.practicum.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class Constants {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    public static final Sort SORT_BY_ID_ASC = Sort.by(Sort.Direction.ASC, "id");
    public static final String MESSAGE_DATE_NOT_VALID = "Недопустимые параметры";
    public static final String MESSAGE_REASON_ERROR_NOT_FOUND = "Запрошенный объект не найден";
    public static final String MESSAGE_REASON_DB_CONSTRAINT_VIOLATION = "Нарушение ограничения БД";
}