package ru.practicum.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatDto;
import ru.practicum.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.practicum.util.Constants.DATE_TIME_FORMATTER;

@WebMvcTest(controllers = StatsController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StatsControllerTest {
    @MockBean
    private StatsService statsService;

    private MockMvc mvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new StatsController(statsService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddHit() throws Exception {
        // Arrange
        HitDto hitDto = new HitDto();
        hitDto.setApp("TestApp");
        hitDto.setUri("TestUri");
        hitDto.setIp("127.0.0.1");
        hitDto.setTimestamp("2023-11-23 15:30:00");

        when(statsService.add(any(HitDto.class))).thenReturn(hitDto);

        mvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(hitDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        verify(statsService, times(1)).add(any(HitDto.class));
    }

    @Test
    void testGetStatistics() throws Exception {
        String start = "2023-11-23 15:00:00";
        String end = "2023-11-23 16:00:00";
        List<String> uris = List.of("TestUri1", "TestUri2");
        Boolean unique = false;

        List<ViewStatDto> viewStatsList = List.of(
                new ViewStatDto("TestApp1", "TestUri1", 10L),
                new ViewStatDto("TestApp2", "TestUri2", 8L)
        );

        when(statsService.getStatistic(
                eq(LocalDateTime.parse(start, DATE_TIME_FORMATTER)),
                eq(LocalDateTime.parse(end, DATE_TIME_FORMATTER)),
                eq(uris), eq(unique))).thenReturn(viewStatsList);

        mvc.perform(get("/stats")
                        .param("start", start)
                        .param("end", end)
                        .param("uris", uris.get(0), uris.get(1))
                        .param("unique", unique.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].app").value("TestApp1"))
                .andExpect(jsonPath("$[0].uri").value("TestUri1"))
                .andExpect(jsonPath("$[1].app").value("TestApp2"))
                .andExpect(jsonPath("$[1].uri").value("TestUri2"));

        verify(statsService, times(1)).getStatistic(
                eq(LocalDateTime.parse(start, DATE_TIME_FORMATTER)),
                eq(LocalDateTime.parse(end, DATE_TIME_FORMATTER)),
                eq(uris), eq(unique));
    }
}