package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dtos.CompilationResponseDto;
import ru.practicum.compilation.dtos.NewCompilationRequest;
import ru.practicum.compilation.dtos.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompilationController {

    private final CompilationService compilationService;
    //admin
    private static final String ADMIN_PATH = "/admin/compilations";

    @PostMapping(path = ADMIN_PATH)
    public ResponseEntity<CompilationResponseDto> save(@RequestBody @Valid NewCompilationRequest dto) {

        return new ResponseEntity<>(compilationService.save(dto), HttpStatus.CREATED);
    }

    @PatchMapping(path = ADMIN_PATH + "/{compId}")
    public ResponseEntity<CompilationResponseDto> patch(@PathVariable(value = "compId") Long compId,
                                                        @RequestBody @Valid UpdateCompilationRequest dto) {
        return new ResponseEntity<>(compilationService.update(compId, dto), HttpStatus.OK);
    }

    @DeleteMapping(path = ADMIN_PATH + "/{compId}")
    public ResponseEntity<?> delete(@PathVariable(value = "compId") Long compId) {
        compilationService.delete(compId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/compilations")
    public ResponseEntity<List<CompilationResponseDto>> findAll(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(compilationService.findAll(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping(path = "/compilations/{compId}")
    public ResponseEntity<CompilationResponseDto> findById(@PathVariable Long compId) {
        return new ResponseEntity<>(compilationService.findOne(compId), HttpStatus.OK);
    }
}
