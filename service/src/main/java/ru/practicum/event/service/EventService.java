package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.service.CategoryService;
import ru.practicum.client.StatsClient;
import ru.practicum.dtos.ViewStatDto;
import ru.practicum.event.dtos.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.mapper.location.LocationMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.UpdateState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ApplicationRulesViolationException;
import ru.practicum.exception.ModelNotFoundException;
import ru.practicum.exception.CustomBadRequestException;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.service.UserService;
import ru.practicum.util.constant.Constants;
import ru.practicum.util.enums.SortType;
import ru.practicum.util.formatter.SimpleDateFormatter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    //Репозиторий сущности
    private final EventRepository eventRepository;
    //Сервисы задействованных сущностей
    private final UserService userService;
    private final CategoryService categoryService;
    private final RequestRepository requestRepository;

    private final StatsClient statsClient;

    @Transactional
    public EventBaseResponseDto save(long userId, EventRequestDto eventRequestDto) {
        if (eventRequestDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ApplicationRulesViolationException(
                    "Событие может начаться не раньше чем через 2 часа после создания публикации");
        }
        Event event = EventMapper.newDtoToEvent(eventRequestDto);
        event.setInitiator(userService.findById(userId));
        event.setCategory(categoryService.findById(eventRequestDto.getCategory()));
        event.setCreated(LocalDateTime.now());
        event.setState(EventState.PENDING);
        if (event.getPaid() == null) {
            event.setPaid(false);
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        Event savedEvent = eventRepository.save(event);
        log.info("Событие с id - {} сохранено!", event.getId());
        return EventMapper.eventToDto(savedEvent);
    }

    @Transactional
    public EventBaseResponseDto updateByUser(long userId, long eventId, EventUpdateRequestDto dto) {
        userService.findById(userId);
        Event event = findById(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new ModelNotFoundException("Событие может редактировать только его создатель!");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new ApplicationRulesViolationException("Событие уже опубликовано! Редактирование не доступно!");
        }
        if (dto.getEventDate() != null && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ApplicationRulesViolationException(
                    "Событие должно начаться не раньше чем через 2 часа после создания заявки!");
        }
        if (dto.getStateAction() != null) {
            if (dto.getStateAction() == UpdateState.SEND_TO_REVIEW) event.setState(EventState.PENDING);
            if (dto.getStateAction() == UpdateState.CANCEL_REVIEW) event.setState(EventState.CANCELED);
        }
        updateEvent(dto, event);
        log.info("Событие с id - {} обновлено пользователем!", event.getId());
        return EventMapper.eventToDto(eventRepository.save(event));
    }

    @Transactional
    public EventBaseResponseDto updateByAdmin(Long eventId, EventUpdateRequestDto dto) {
        Event event = findById(eventId);
        if (event.getState() == EventState.PUBLISHED) {
            throw new ApplicationRulesViolationException("Событие уже опубликовано! Редактирование не доступно!");
        }
        if (dto.getStateAction() != null) {
            if (dto.getStateAction() == UpdateState.PUBLISH_EVENT && event.getState() == EventState.CANCELED) {
                throw new ApplicationRulesViolationException(
                        "Опубликовать можно только событие ожидающее публикации!");
            }
        }
        if (dto.getEventDate() != null && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ApplicationRulesViolationException(
                    "Событие должно начаться не раньше чем через час после публикации!");
        }
        if (dto.getStateAction() != null) {
            if (dto.getStateAction() == UpdateState.PUBLISH_EVENT) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            if (dto.getStateAction() == UpdateState.REJECT_EVENT) event.setState(EventState.CANCELED);
        }
        updateEvent(dto, event);
        log.info("Событие с id - {} обновлено администратором!", event.getId());
        return EventMapper.eventToDto(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    public List<EventPublicResponseDto> getAllByPublic(String text,
                                                       List<Long> categories,
                                                       Boolean paid, LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd,
                                                       Boolean onlyAvailable,
                                                       SortType sort,
                                                       Integer from,
                                                       Integer size) {
        //Проверить даты null и на соответствие условиям
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = Constants.MAX_DATE;
        }
        if (rangeEnd == null) {
            rangeEnd = Constants.MAX_DATE;
        }
        if (rangeStart.isAfter(rangeEnd)) {
            throw new CustomBadRequestException("Дата начало поиска не может быть после даты конца!");
        }
        List<Event> filteredList;
        //Чтобы не грузить сразу всю базу сначала фильтруем что можем.
        if (text != null && !text.isBlank()) {
            //Здесь есть поиск по тексту
            if (!categories.isEmpty()) {
                //Есть поиск по категориям
                if (paid != null) {
                    //Есть поиск по наличию оплаты
                    filteredList = eventRepository
                            .findAllByAnnotationContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndCategoryIdIsInAndPaidAndEventDateIsBetweenAndState(
                                    text, text, categories, paid, rangeStart, rangeEnd, EventState.PUBLISHED);
                } else {
                    //Нет поиска по наличию оплаты
                    filteredList = eventRepository
                            .findAllByAnnotationContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndCategoryIdIsInAndEventDateIsBetweenAndState(
                                    text, text, categories, rangeStart, rangeEnd, EventState.PUBLISHED);
                }
            } else {
                //Нет поиска по категории
                if (paid != null) {
                    //Есть поиск по наличию оплаты
                    filteredList = eventRepository
                            .findAllByAnnotationContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndPaidAndEventDateIsBetweenAndState(
                                    text, text, paid, rangeStart, rangeEnd, EventState.PUBLISHED);
                } else {
                    //Нет поиска по наличию оплаты
                    filteredList = eventRepository
                            .findAllByAnnotationContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndEventDateIsBetweenAndState(
                                    text, text, rangeStart, rangeEnd, EventState.PUBLISHED);
                }
            }
        } else {
            //Здесь нет поиска по тексту
            if (!categories.isEmpty()) {
                //Есть поиск по категориям
                if (paid != null) {
                    //Есть поиск по наличию оплаты
                    filteredList = eventRepository
                            .findAllByCategoryIdIsInAndPaidAndEventDateIsBetweenAndState(
                                    categories, paid, rangeStart, rangeEnd, EventState.PUBLISHED);
                } else {
                    //Нет поиска по наличию оплаты
                    filteredList = eventRepository
                            .findAllByCategoryIdIsInAndEventDateIsBetweenAndState(
                                    categories, rangeStart, rangeEnd, EventState.PUBLISHED);
                }
            } else {
                //Нет поиска по категории
                if (paid != null) {
                    //Есть поиск по наличию оплаты
                    filteredList = eventRepository.findAllByPaidAndEventDateIsBetweenAndState(
                            paid, rangeStart, rangeEnd, EventState.PUBLISHED);
                } else {
                    //Нет поиска по наличию оплаты
                    filteredList = eventRepository.findAllByEventDateIsBetweenAndState(
                            rangeStart, rangeEnd, EventState.PUBLISHED);
                }
            }
        }
        //Проверим условие доступности
        if (onlyAvailable) {
            filteredList = filteredList.stream()
                    .filter(event -> event.getParticipantLimit() > getNumOfTakenPlaces(event.getId())
                            || event.getParticipantLimit() == 0)
                    .collect(Collectors.toList());
        }
        //Если ничего не нашли, то ничего и не найдем
        if (filteredList.isEmpty()) return new ArrayList<>();
        //Если нашли, подготовим основу для ответа
        List<EventPublicResponseDto> result = filteredList.stream()
                .map(EventMapper::eventToPublicDto)
                .collect(Collectors.toList());
        //Собираем просмотры
        Map<Long, Long> viewsById = getViews(filteredList);
        result.forEach(e -> {
            if (viewsById.get(e.getId()) != null) e.setViews(viewsById.get(e.getId()));
        });
        //Собираем подтвержденных участников
        result.forEach(e -> e.setConfirmedRequests(getNumOfTakenPlaces(e.getId())));
        //Сортируем
        if (sort != null) {
            switch (sort) {
                case EVENT_DATE:
                    result.sort(Comparator.comparing(EventPublicResponseDto::getEventDate));
                    break;
                case VIEWS:
                    result.sort(Comparator.comparing(EventPublicResponseDto::getViews));
                    break;
            }
        }
        //Делаем пагинацию
        int upperBound = (Math.min((from + size), result.size()));
        result = result.subList(from, upperBound);
        log.info("Получен публичный запрос списка событий!");
        return result;
    }

    @Transactional(readOnly = true)
    public EventFullResponseDto getByIdPublic(Long eventId) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ModelNotFoundException("Событие не найдено!"));
        EventFullResponseDto result = EventMapper.eventToFullDto(event);
        result.setViews(getViews(event));
        result.setConfirmedRequests(getNumOfTakenPlaces(eventId));
        log.info("Получен публичный запрос сведений о событии по id!");
        return result;
    }

    @Transactional(readOnly = true)
    public List<EventFullResponseDto> getAllByAdmin(List<Long> users,
                                                    List<String> states,
                                                    List<Long> categories,
                                                    LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd,
                                                    Integer from,
                                                    Integer size) {

        //Проверить даты null и на соответствие условиям
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = Constants.MAX_DATE;
        }
        if (rangeEnd == null) {
            rangeEnd = Constants.MAX_DATE;
        }
        if (rangeStart.isAfter(rangeEnd)) {
            throw new CustomBadRequestException("Дата начало поиска не может быть после даты конца!");
        }

        states.forEach(a -> EventState.from(a).orElseThrow(() -> new CustomBadRequestException("UnknownState:" + a)));
        List<EventState> enumStates = states.stream().map(EventState::from)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        List<Event> filteredList;
        //Пользователи
        if (!users.isEmpty()) {
            //Состояния
            if (!states.isEmpty()) {
                //Категории
                if (!categories.isEmpty()) {
                    filteredList = eventRepository
                            .findAllByInitiatorIdIsInAndStateIsInAndCategoryIdIsInAndEventDateIsBetween(
                                    users, enumStates, categories, rangeStart, rangeEnd);
                } else {
                    filteredList = eventRepository
                            .findAllByInitiatorIdIsInAndStateIsInAndEventDateIsBetween(
                                    users, enumStates, rangeStart, rangeEnd);
                }
            } else {
                if (!categories.isEmpty()) {
                    filteredList = eventRepository
                            .findAllByInitiatorIdIsInAndCategoryIdIsInAndEventDateIsBetween(
                                    users, categories, rangeStart, rangeEnd);
                } else {
                    filteredList = eventRepository
                            .findAllByInitiatorIdIsInAndEventDateIsBetween(
                                    users, rangeStart, rangeEnd);
                }
            }
        } else {
            if (!states.isEmpty()) {
                if (!categories.isEmpty()) {
                    filteredList = eventRepository
                            .findAllByStateIsInAndCategoryIdIsInAndEventDateIsBetween(
                                    enumStates, categories, rangeStart, rangeEnd);
                } else {
                    filteredList = eventRepository
                            .findAllByStateIsInAndEventDateIsBetween(
                                    enumStates, rangeStart, rangeEnd);
                }
            } else {
                if (!categories.isEmpty()) {
                    filteredList = eventRepository
                            .findAllByCategoryIdIsInAndEventDateIsBetween(
                                    categories, rangeStart, rangeEnd);
                } else {
                    filteredList = eventRepository
                            .findAllByEventDateIsBetween(
                                    rangeStart, rangeEnd);
                }
            }
        }
        if (filteredList.isEmpty()) return new ArrayList<>();
        //Если нашли, подготовим основу для ответа
        List<EventFullResponseDto> result = filteredList.stream()
                .map(EventMapper::eventToFullDto)
                .collect(Collectors.toList());
        //Собираем просмотры
        Map<Long, Long> viewsById = getViews(filteredList);

        result.forEach(e -> {
            if (viewsById.get(e.getId()) != null) e.setViews(viewsById.get(e.getId()));
        });

        //Собираем подтвержденных участников
        result.forEach(e -> e.setConfirmedRequests(getNumOfTakenPlaces(e.getId())));

        //
        int upperBound = (Math.min((from + size), result.size()));
        result = result.subList(from, upperBound);
        log.info("Получен запрос списка событий от администратора!");
        return result;
    }

    @Transactional(readOnly = true)
    public List<EventPublicResponseDto> getAllByUser(Long userId, Integer from, Integer size) {
        userService.findById(userId);
        PageRequest pageRequest = PageRequest.of(from / size, size);

        List<Event> filteredList = eventRepository.findAllByInitiatorId(userId, pageRequest);

        List<EventPublicResponseDto> result = filteredList.stream()
                .map(EventMapper::eventToPublicDto)
                .collect(Collectors.toList());

        Map<Long, Long> viewsById = getViews(filteredList);
        result.forEach(e -> {
            if (viewsById.get(e.getId()) != null) e.setViews(viewsById.get(e.getId()));
        });
        result.forEach(e -> e.setConfirmedRequests(getNumOfTakenPlaces(e.getId())));
        log.info("Получен запрос списка событий пользователя с id - {}!", userId);
        return result;
    }

    @Transactional(readOnly = true)
    public EventFullResponseDto getByIdByUser(Long userId, Long eventId) {
        userService.findById(userId);
        Event event = findById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ModelNotFoundException("Полную информацию о событии может просматривать только его создатель!");
        }
        EventFullResponseDto result = EventMapper.eventToFullDto(event);
        result.setViews(getViews(event));
        result.setConfirmedRequests(getNumOfTakenPlaces(eventId));
        log.info("Получен запрос события с id - {} пользователя с id - {}!", eventId, userId);
        return result;
    }

    //Внутреннее пользование
    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new ModelNotFoundException("Событие не найдено!"));
    }

    private void updateEvent(EventUpdateRequestDto dto, Event event) {
        if (dto.getLocation() != null) event.setLocation(LocationMapper.dtoToLocation(dto.getLocation()));
        if (dto.getCategory() != null) event.setCategory(categoryService.findById(dto.getCategory()));
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
    }

    public EventPublicResponseDto getPublicDtoById(Long eventId) {
        Event event = findById(eventId);
        EventPublicResponseDto result = EventMapper.eventToPublicDto(event);
        result.setViews(getViews(event));
        result.setConfirmedRequests(getNumOfTakenPlaces(eventId));
        return result;
    }

    public Integer getNumOfTakenPlaces(Long eventId) {
        return requestRepository.countAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
    }

    private Map<Long, Long> getViews(List<Event> filteredList) {
        Map<Long, Long> viewsById = new HashMap<>();
        Optional<LocalDateTime> minPublishedOnOpt = filteredList.stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);

        LocalDateTime minPublishedOn;
        if (minPublishedOnOpt.isPresent()) {
            minPublishedOn = minPublishedOnOpt.get();
        } else {
            return viewsById;
        }
        String startDate = URLEncoder.encode(minPublishedOn
                .format(SimpleDateFormatter.FORMATTER), StandardCharsets.UTF_8);
        String endDate = URLEncoder.encode(LocalDateTime.now()
                .format(SimpleDateFormatter.FORMATTER), StandardCharsets.UTF_8);
        List<String> uris = filteredList.stream().map(event -> "/events/" + event.getId()).collect(Collectors.toList());

        List<ViewStatDto> statDtos = statsClient.getStats(startDate, endDate, uris, true);

        statDtos.forEach(a -> {
            Long id = Long.valueOf(a.getUri().substring(a.getUri().length() - 1));
            viewsById.put(id, a.getHits());
        });
        return viewsById;
    }

    private Long getViews(Event event) {
        if (event.getPublishedOn() == null) {
            return 0L;
        }
        String startDate = URLEncoder.encode(event.getPublishedOn()
                .format(SimpleDateFormatter.FORMATTER), StandardCharsets.UTF_8);
        String endDate = URLEncoder.encode(LocalDateTime.now()
                .format(SimpleDateFormatter.FORMATTER), StandardCharsets.UTF_8);
        List<ViewStatDto> stats = statsClient.getStats(
                startDate, endDate, List.of("/events/" + event.getId()), true);
        return !stats.isEmpty() ? stats.get(0).getHits() : 0L;
    }
}
