package ru.practicum.util.validation;

import ru.practicum.event.dtos.EventRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateValidator implements ConstraintValidator<StartAfterTwoHours, EventRequestDto> {


    @Override
    public void initialize(StartAfterTwoHours constraintAnnotation) {
    }

    @Override
    public boolean isValid(EventRequestDto eventRequestDto, ConstraintValidatorContext constraintValidatorContext) {
        if (eventRequestDto.getEventDate() == null) return false;
        return !eventRequestDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2L));
    }
}
