package ru.practicum.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatDto;

import java.util.*;


@Slf4j
@Service
public class StatsClient extends BaseClient {
    @Autowired
    public StatsClient(@Value("${stats.client.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public List<ViewStatDto> getStatistics(String start, String end, String[] uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );

        log.info("Request to receive statistics on visits from {} to {}", start, end);

        ResponseEntity<Object> objectResponseEntity = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        return new ObjectMapper().convertValue(objectResponseEntity.getBody(), new TypeReference<List<ViewStatDto>>() {
        });
    }

    public void postHit(HitDto hitDto) {
        log.info("Request to save endpoint");
        post("/hit", hitDto);
    }
}