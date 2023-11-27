package ru.practicum.server.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.dto.HitDto;
import ru.practicum.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class HitMapperTest {
    private final HitMapper hitMapper = new HitMapper();

    @Test
    void testToHit() {
        HitDto hitDto = new HitDto();
        hitDto.setApp("TestApp");
        hitDto.setUri("TestUri");
        hitDto.setIp("127.0.0.1");
        hitDto.setTimestamp("2023-11-23 15:30:00");

        EndpointHit result = hitMapper.toHit(hitDto);

        assertNotNull(result);
        assertEquals(hitDto.getApp(), result.getApp());
        assertEquals(hitDto.getUri(), result.getUri());
        assertEquals(hitDto.getIp(), result.getIp());
        assertEquals(LocalDateTime.parse(hitDto.getTimestamp(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), result.getTimestamp());
    }

    @Test
    void testToHitDto() {
        EndpointHit hit = new EndpointHit();
        hit.setApp("TestApp");
        hit.setUri("TestUri");
        hit.setIp("127.0.0.1");
        hit.setTimestamp(LocalDateTime.parse("2023-11-23 15:30:00",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        HitDto result = hitMapper.toHitDto(hit);

        assertNotNull(result);
        assertEquals(hit.getApp(), result.getApp());
        assertEquals(hit.getUri(), result.getUri());
        assertEquals(hit.getIp(), result.getIp());
        assertEquals(hit.getTimestamp().toString(), result.getTimestamp());
    }
}