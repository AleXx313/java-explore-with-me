package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.dtos.EndpointHitDto;
import ru.practicum.service.StatsServerService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsServerController.class)
class StatsServerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StatsServerService service;


    @Test
    void save_whenDtoIsValid_thenStatusIsCreated() throws Exception {
        LocalDateTime hitTime = LocalDateTime.of(2022, 3, 3, 16, 20, 0);
        EndpointHitDto dto = EndpointHitDto.builder()
                .id(1L)
                .app("some-app")
                .uri("uri/1")
                .ip("111.111.0.11")
                .timestamp(hitTime)
                .build();
        when(service.save(dto)).thenReturn(dto);

        mockMvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void save_whenDtoIsNotValid_thenStatusIsBadRequest() throws Exception {
        LocalDateTime hitTime = LocalDateTime.of(2022, 3, 3, 16, 20, 0);
        EndpointHitDto dto = EndpointHitDto.builder()
                .id(1L)
                .app("")
                .uri("")
                .ip("111.111.0.1122222222222222")
                .timestamp(hitTime)
                .build();

        mockMvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, never()).save(dto);
    }

    @Test
    void getStats_whenParamsIsOk_thenInvokeServiceGetStat() throws Exception {
        mockMvc.perform(get("/stats")
                        .param("start", "some start time")
                        .param("end", "some start time"))
                .andExpect(status().isOk());
        verify(service, times(1))
                .getStats("some start time", "some start time", Collections.emptyList(), false);
    }

    @Test
    void getStats_whenParamsIsNotOk_thenStatusIsBadRequest() throws Exception {
        mockMvc.perform(get("/stats"))
                .andExpect(status().isBadRequest());
        verify(service, never()).getStats(any(), any(), any(), any());
    }


}