package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dtos.CompilationResponseDto;
import ru.practicum.compilation.dtos.NewCompilationRequest;
import ru.practicum.compilation.dtos.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.dtos.EventPublicResponseDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exception.ModelNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    //API
    @Transactional
    public CompilationResponseDto save(NewCompilationRequest dto) {
        Compilation compilation = CompilationMapper.dtoToCompilation(dto);
        if (dto.getEvents() != null) {
            Set<Event> events = dto.getEvents().stream()
                    .map(eventService::findById)
                    .collect(Collectors.toSet());
            compilation.setEvents(events);
        }
        log.info("Сохранена подборка событий - {}", compilation.getTitle());
        return getCompilationResponseDto(compilation);
    }

    @Transactional
    public void delete(Long compId) {
        findById(compId);
        compilationRepository.deleteById(compId);
        log.info("Удалена подборка событий с id - {}", compId);
    }

    @Transactional
    public CompilationResponseDto update(Long compId, UpdateCompilationRequest dto) {
        Compilation compilation = findById(compId);
        if (dto.getPinned() != null) compilation.setPinned(dto.getPinned());
        if (dto.getTitle() != null) compilation.setTitle(dto.getTitle());

        if (dto.getEvents() != null) {
            Set<Event> eventsToAdd = dto.getEvents().stream()
                    .map(eventService::findById)
                    .collect(Collectors.toSet());

            compilation.setEvents(eventsToAdd);
        }
        log.info("Обновлена подборка событий - {}", compilation.getTitle());
        return getCompilationResponseDto(compilation);
    }

    @Transactional(readOnly = true)
    public List<CompilationResponseDto> findAll(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, pageRequest);
        } else {
            compilations = compilationRepository.findAll(pageRequest).toList();
        }
        List<CompilationResponseDto> result = compilations.stream()
                .map(this::getCompilationResponseDto)
                .collect(Collectors.toList());
        log.info("Получен список подборок событий");
        return result;
    }

    @Transactional(readOnly = true)
    public CompilationResponseDto findOne(Long compId) {
        Compilation compilation = findById(compId);
        log.info("Получена подборка событий - {}!", compilation.getTitle());
        return getCompilationResponseDto(compilation);
    }

    //Utility
    private CompilationResponseDto getCompilationResponseDto(Compilation compilation) {
        Compilation updatedCompilation = compilationRepository.save(compilation);
        CompilationResponseDto result = CompilationMapper.compilationToDto(updatedCompilation);
        List<EventPublicResponseDto> eventsDto = updatedCompilation.getEvents().stream()
                .map(Event::getId)
                .map(eventService::getPublicDtoById)
                .collect(Collectors.toList());
        result.setEvents(eventsDto);
        return result;
    }

    private Compilation findById(Long id) {
        return compilationRepository.findById(id).orElseThrow(() -> new ModelNotFoundException("Подборка не найдена!"));
    }
}
