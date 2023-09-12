package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.service.CategoryService;
import ru.practicum.category.dtos.CategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private static final String ADMIN = "admin";

    //АДМИН ЧАСТЬ
    @PostMapping(path = "/" + ADMIN + "/categories")
    public ResponseEntity<CategoryDto> save(@RequestBody @Valid CategoryDto dto){
        return new ResponseEntity<>(categoryService.save(dto), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/" + ADMIN + "/categories/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable(value = "id") Long id,
                                              @RequestBody @Valid CategoryDto dto){
        return new ResponseEntity<>(categoryService.update(id, dto), HttpStatus.OK);
    }

    @DeleteMapping(path = "/" + ADMIN + "/categories/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") Long id){
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //ПУБЛИЧНАЯ ЧАСТЬ
    @GetMapping(path = "/categories")
    public ResponseEntity<List<CategoryDto>> findAll(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size){
        return new ResponseEntity<>(categoryService.findAll(from, size), HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable (value = "id") Long id){
        return new ResponseEntity<>(categoryService.getById(id), HttpStatus.OK);
    }

}
