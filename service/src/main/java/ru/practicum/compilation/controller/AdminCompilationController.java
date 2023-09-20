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

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationResponseDto> save(@RequestBody @Valid NewCompilationRequest dto) {

        return new ResponseEntity<>(compilationService.save(dto), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{compId}")
    public ResponseEntity<CompilationResponseDto> patch(@PathVariable(value = "compId") Long compId,
                                                        @RequestBody @Valid UpdateCompilationRequest dto) {
        return new ResponseEntity<>(compilationService.update(compId, dto), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{compId}")
    public ResponseEntity<?> delete(@PathVariable(value = "compId") Long compId) {
        compilationService.delete(compId);
        return ResponseEntity.noContent().build();
    }
}
