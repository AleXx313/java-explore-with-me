package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dtos.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody @Valid UserDto dto){
        return new ResponseEntity<>(userService.save(dto), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<UserDto>> find(
            @RequestParam (required = false, value = "ids", defaultValue = "") List<Long> ids,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size){
        return new ResponseEntity<>(userService.find(ids, from, size), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity delete(@PositiveOrZero @PathVariable (value = "id") Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
