package ru.practicum.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event>
    findAllByAnnotationContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndCategoryIdIsInAndPaidAndEventDateIsBetweenAndState(
            String text,
            String text2,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            EventState state);

    List<Event>
    findAllByAnnotationContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndCategoryIdIsInAndEventDateIsBetweenAndState(
            String text, String text1, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            EventState state);


    List<Event>
    findAllByAnnotationContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndPaidAndEventDateIsBetweenAndState(
            String text, String text1, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            EventState state);

    List<Event>
    findAllByAnnotationContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndEventDateIsBetweenAndState(
            String text, String text1, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            EventState state);

    List<Event> findAllByCategoryIdIsInAndPaidAndEventDateIsBetweenAndState(
            List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            EventState state);

    List<Event> findAllByCategoryIdIsInAndEventDateIsBetweenAndState(
            List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            EventState state);

    List<Event> findAllByPaidAndEventDateIsBetweenAndState(
            Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            EventState state);

    List<Event> findAllByEventDateIsBetweenAndState(LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                    EventState state);

    Optional<Event> findByIdAndState(Long eventId, EventState eventState);

    List<Event> findAllByInitiatorIdIsInAndStateIsInAndCategoryIdIsInAndEventDateIsBetween(
            List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime start, LocalDateTime end);

    List<Event> findAllByInitiatorIdIsInAndStateIsInAndEventDateIsBetween(
            List<Long> users, List<EventState> enumStates, LocalDateTime rangeStart, LocalDateTime rangeEnd);

    List<Event> findAllByInitiatorIdIsInAndCategoryIdIsInAndEventDateIsBetween(
            List<Long> users, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd);

    List<Event> findAllByInitiatorIdIsInAndEventDateIsBetween(
            List<Long> users, LocalDateTime rangeStart, LocalDateTime rangeEnd);


    List<Event> findAllByStateIsInAndCategoryIdIsInAndEventDateIsBetween(
            List<EventState> enumStates, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd);

    List<Event> findAllByStateIsInAndEventDateIsBetween(
            List<EventState> enumStates, LocalDateTime rangeStart, LocalDateTime rangeEnd);

    List<Event> findAllByCategoryIdIsInAndEventDateIsBetween(
            List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd);

    List<Event> findAllByEventDateIsBetween(LocalDateTime rangeStart, LocalDateTime rangeEnd);

    List<Event> findAllByInitiatorId(Long userId, PageRequest pageRequest);
}
