package ru.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.dtos.EndpointHitDto;
import ru.practicum.dtos.ViewStatDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsServerRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServerServiceTest {
    @InjectMocks
    private StatsServerService service;
    @Mock
    private StatsServerRepository repository;

    @Test
    void save_whenDtoIsOk_thenReturnDto() {
        EndpointHitDto dto = new EndpointHitDto(
                1L, "some-app", "uri/1", "111.111.0.11", "2022-03-03 16:20:00");

        EndpointHit model = EndpointHitMapper.dtoToEntity(dto);
        when(repository.save(model)).thenReturn(model);

        EndpointHitDto saved = service.save(dto);

        assertThat(saved.getUri()).isEqualTo(dto.getUri());
        verify(repository, times(1)).save(any(EndpointHit.class));

    }

    @Test
    void getStats_whenUniqueFalseAndListEmpty_thenInvokeRepositoryFindByHitTimeIsBetween() {
        LocalDateTime start = LocalDateTime.of(2022,3,3,16,20,0);
        LocalDateTime end = LocalDateTime.of(2022,4,3,16,20,0);
        service.getStats(
                "2022-03-03%2016%3A20%3A00",
                "2022-04-03%2016%3A20%3A00",
                Collections.emptyList(),
                false);
        verify(repository, only()).findByHitTimeIsBetween(start, end);
    }
    @Test
    void getStats_whenUniqueFalseAndListNotEmpty_thenInvokeRepositoryFindByHitTimeIsBetweenAndUriIsIn() {
        LocalDateTime start = LocalDateTime.of(2022,3,3,16,20,0);
        LocalDateTime end = LocalDateTime.of(2022,4,3,16,20,0);
        List<String> uris = List.of("someUri1", "someUri2");
        service.getStats(
                "2022-03-03%2016%3A20%3A00",
                "2022-04-03%2016%3A20%3A00",
                uris,
                false);
        verify(repository, only()).findByHitTimeIsBetweenAndUriIsIn(start, end, uris);
    }
    @Test
    void getStats_whenUniqueTrueAndListEmpty_thenInvokeRepositoryFindByHitTimeIsBetweenUnique() {
        LocalDateTime start = LocalDateTime.of(2022,3,3,16,20,0);
        LocalDateTime end = LocalDateTime.of(2022,4,3,16,20,0);
        service.getStats(
                "2022-03-03%2016%3A20%3A00",
                "2022-04-03%2016%3A20%3A00",
                Collections.emptyList(),
                true);
        verify(repository, only()).findByHitTimeIsBetweenUnique(start, end);
    }
    @Test
    void getStats_whenUniqueTrueAndListNotEmpty_thenInvokeRepositoryFindByHitTimeIsBetweenAndUriIsInUnique() {
        LocalDateTime start = LocalDateTime.of(2022,3,3,16,20,0);
        LocalDateTime end = LocalDateTime.of(2022,4,3,16,20,0);
        List<String> uris = List.of("someUri1", "someUri2");
        service.getStats(
                "2022-03-03%2016%3A20%3A00",
                "2022-04-03%2016%3A20%3A00",
                uris,
                true);
        verify(repository, only()).findByHitTimeIsBetweenAndUriIsInUnique(start, end, uris);
    }
}