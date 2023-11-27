package ru.practicum.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.HitDto;
import ru.practicum.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class HitMapper {
    public EndpointHit toHit(HitDto hitDto) {
        EndpointHit hit = new EndpointHit();
        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

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