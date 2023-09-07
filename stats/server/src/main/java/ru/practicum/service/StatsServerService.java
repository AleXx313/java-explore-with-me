package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.UriEncoder;
import ru.practicum.dtos.EndpointHitDto;
import ru.practicum.dtos.ViewStatDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsServerRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServerService {
    private final StatsServerRepository repository;

    public EndpointHitDto save(EndpointHitDto dto) {
        EndpointHit saved = repository.save(EndpointHitMapper.dtoToEntity(dto));
        log.info("EndpointHit сервис - {}, uri - {} сохранен!", saved.getApp(), saved.getUri());
        return EndpointHitMapper.entityToDto(saved);
    }

    public List<ViewStatDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(UriEncoder.decode(start), formatter);
        LocalDateTime endDate = LocalDateTime.parse(UriEncoder.decode(end), formatter);
        log.info("Запрошена статистика за период с {} по {}!", start, end);
        if (unique) {
            if (uris.isEmpty()) {
                return repository.findByHitTimeIsBetweenUnique(startDate, endDate);
            } else {
                return repository.findByHitTimeIsBetweenAndUriIsInUnique(startDate, endDate, uris);
            }
        } else {
            if (uris.isEmpty()) {
                return repository.findByHitTimeIsBetween(startDate, endDate);
            } else {
                return repository.findByHitTimeIsBetweenAndUriIsIn(startDate, endDate, uris);
            }
        }
    }
}
