package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitDto;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStatistics(String start, String end, List<String> uriList, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uriList", uriList,
                "unique", unique
        );
        log.info("Request to receive statistics on visits from {} to {}", start, end);
        return get("?start={start}&end={end}&uriList={uriList}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> postHit(HitDto hitDto) {
        log.info("Request to save endpoint");
        return post("/hit", hitDto);
    }
}