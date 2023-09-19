package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.practicum.dtos.EndpointHitDto;
import ru.practicum.dtos.ViewStatDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsClient {
    private final WebClient webClient;

    public List<ViewStatDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        String urisAsString = String.join(",", uris);

        return webClient.get()
                .uri("/stats?start={start}&end={end}&uris={uris}&unique={unique}", start, end, urisAsString, unique)
                .retrieve()
                .bodyToFlux(ViewStatDto.class)
                .collectList()
                .block();
    }

    public void saveHit(EndpointHitDto endpointHitDto) {
        webClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(endpointHitDto))
                .exchange()
                .block();
    }
}
