package ru.practicum.event.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.QEvent;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventRepositoryQDSL {

    private final JPAQueryFactory jpaQueryFactory;
    QEvent event = QEvent.event;

    //Публичная часть
    public List<Event> findByTextAndCategoryAndIsPaid(
            String text, List<Long> categories, Boolean isPaid, LocalDateTime start, LocalDateTime end, EventState state) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.state.eq(state))
                .where(event.annotation.likeIgnoreCase("%" + text + "%")
                        .or(event.description.likeIgnoreCase("%" + text + "%")))
                .where(event.category.id.in(categories))
                .where(event.paid.eq(isPaid))
                .fetch();
    }

    public List<Event> findByTextAndCategory(
            String text, List<Long> categories, LocalDateTime start, LocalDateTime end, EventState state) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.state.eq(state))
                .where(event.annotation.likeIgnoreCase("%" + text + "%")
                        .or(event.description.likeIgnoreCase("%" + text + "%")))
                .where(event.category.id.in(categories))
                .fetch();
    }

    public List<Event> findByTextAndIsPaid(
            String text, Boolean isPaid, LocalDateTime start, LocalDateTime end, EventState state) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.state.eq(state))
                .where(event.annotation.likeIgnoreCase("%" + text + "%")
                        .or(event.description.likeIgnoreCase("%" + text + "%")))
                .where(event.paid.eq(isPaid))
                .fetch();
    }

    public List<Event> findByText(
            String text, LocalDateTime start, LocalDateTime end, EventState state) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.state.eq(state))
                .where(event.annotation.likeIgnoreCase("%" + text + "%")
                        .or(event.description.likeIgnoreCase("%" + text + "%")))
                .fetch();
    }

    public List<Event> findByCategoryAndIsPaid(
            List<Long> categories, Boolean isPaid, LocalDateTime start, LocalDateTime end, EventState state) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.state.eq(state))
                .where(event.category.id.in(categories))
                .where(event.paid.eq(isPaid))
                .fetch();
    }

    public List<Event> findByCategory(
            List<Long> categories, LocalDateTime start, LocalDateTime end, EventState state) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.state.eq(state))
                .where(event.category.id.in(categories))
                .fetch();
    }

    public List<Event> findByIsPaid(
            Boolean isPaid, LocalDateTime start, LocalDateTime end, EventState state) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.state.eq(state))
                .where(event.paid.eq(isPaid))
                .fetch();
    }

    public List<Event> findByDefaultFilters(
            LocalDateTime start, LocalDateTime end, EventState state) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.state.eq(state))
                .fetch();
    }

    //Админ часть
    public List<Event> findByUsersAndCategoriesAndStates(
            List<Long> users, List<Long> categories, List<EventState> states, LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.initiator.id.in(users))
                .where(event.category.id.in(categories))
                .where(event.state.in(states))
                .fetch();
    }

    public List<Event> findByUsersAndCategories(
            List<Long> users, List<Long> categories, LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.category.id.in(categories))
                .where(event.initiator.id.in(users))
                .fetch();
    }

    public List<Event> findByUsersAndStates(
            List<Long> users, List<EventState> states, LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.initiator.id.in(users))
                .where(event.state.in(states))
                .fetch();
    }

    public List<Event> findByUsers(
            List<Long> users, LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.initiator.id.in(users))
                .fetch();
    }

    public List<Event> findByCategoriesAndStates(
            List<Long> categories, List<EventState> states, LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.category.id.in(categories))
                .where(event.state.in(states))
                .fetch();
    }

    public List<Event> findByCategories(
            List<Long> categories, LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.category.id.in(categories))
                .fetch();
    }

    public List<Event> findByStates(
            List<EventState> states, LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .where(event.state.in(states))
                .fetch();
    }

    public List<Event> findByDefaultFiltersAdmin(
            LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory.selectFrom(event)
                .where(event.eventDate.between(start, end))
                .fetch();
    }
}
