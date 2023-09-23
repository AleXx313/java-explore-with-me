package ru.practicum.util.formatter;

import ru.practicum.util.constant.Constants;

import java.time.format.DateTimeFormatter;

public class SimpleDateFormatter {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_PATTERN);
}
