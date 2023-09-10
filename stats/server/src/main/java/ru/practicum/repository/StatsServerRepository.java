package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dtos.ViewStatDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsServerRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.dtos.ViewStatDto(eh.app, eh.uri, count(eh.ip)) " +
            "from EndpointHit as eh " +
            "where eh.hitTime between :start AND :end " +
            "and eh.uri in :uris " +
            "group by eh.app, eh.uri " +
            "order by count(eh.ip) desc")
    List<ViewStatDto> findByHitTimeIsBetweenAndUriIsIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.dtos.ViewStatDto(eh.app, eh.uri, count(distinct eh.ip)) " +
            "from EndpointHit as eh " +
            "where eh.hitTime between :start AND :end " +
            "and eh.uri in :uris " +
            "group by eh.app, eh.uri, eh.ip " +
            "order by count(eh.ip) desc")
    List<ViewStatDto> findByHitTimeIsBetweenAndUriIsInUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.dtos.ViewStatDto(eh.app, eh.uri, count(eh.ip)) " +
            "from EndpointHit as eh " +
            "where eh.hitTime between :start AND :end " +
            "group by eh.app, eh.uri " +
            "order by count(eh.ip) desc")
    List<ViewStatDto> findByHitTimeIsBetween(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dtos.ViewStatDto(eh.app, eh.uri, count(distinct eh.ip)) " +
            "from EndpointHit as eh " +
            "where eh.hitTime between :start AND :end " +
            "group by eh.app, eh.uri, eh.ip " +
            "order by count(eh.ip) desc")
    List<ViewStatDto> findByHitTimeIsBetweenUnique(LocalDateTime start, LocalDateTime end);
}
