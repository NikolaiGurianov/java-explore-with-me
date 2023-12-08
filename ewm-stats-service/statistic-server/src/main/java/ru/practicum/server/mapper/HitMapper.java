package ru.practicum.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.HitDto;
import ru.practicum.server.model.EndpointHit;

import java.time.LocalDateTime;

import static ru.practicum.util.Constants.DATE_TIME_FORMATTER;

@Component
public class HitMapper {
    public EndpointHit toHit(HitDto hitDto) {
        EndpointHit hit = new EndpointHit();
        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(), DATE_TIME_FORMATTER));

        return hit;
    }

    public HitDto toHitDto(EndpointHit hit) {
        HitDto dto = new HitDto();
        dto.setApp(hit.getApp());
        dto.setUri(hit.getUri());
        dto.setIp(hit.getIp());
        dto.setTimestamp(hit.getTimestamp().toString());
        return dto;
    }
}