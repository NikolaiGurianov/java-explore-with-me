package ru.practicum.mainservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.client.StatsClient;

@Configuration
public class StatisticConfigure {
    @Value("${statistic-client.url}")
    private String statsClientUrl;

    @Bean
    public StatsClient init() {
        return new StatsClient(statsClientUrl, new RestTemplateBuilder());
    }
}