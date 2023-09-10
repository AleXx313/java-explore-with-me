package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dtos.EndpointHitDto;
import ru.practicum.dtos.ViewStatDto;
import ru.practicum.service.StatsServerService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsServerController {

    private final StatsServerService service;

    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> save(@RequestBody @Valid EndpointHitDto dto) {
        return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatDto>> getStats(@RequestParam String start,
                                                      @RequestParam String end,
                                                      @RequestParam(required = false, defaultValue = "") List<String> uris,
                                                      @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return new ResponseEntity<>(service.getStats(start, end, uris, unique), HttpStatus.OK);
    }
}
