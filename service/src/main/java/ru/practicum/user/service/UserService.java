package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ModelNotFoundException;
import ru.practicum.user.dtos.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //API
    @Transactional
    public UserDto save(UserDto dto) {
        User savedUser = userRepository.save(UserMapper.dtoToUser(dto));
        log.info("Сохранен пользователь по имени - {} с id - {}!", savedUser.getName(), savedUser.getId());
        return UserMapper.userToDto(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserDto> find(List<Long> ids, Integer from, Integer size) {
        PageRequest request = PageRequest.of(from / size, size);
        if (ids.isEmpty()) {
            return UserMapper.listToDtoList(userRepository.findAll(request).toList());
        } else {
            return UserMapper.listToDtoList(userRepository.findAllByIdIsIn(request, ids));
        }
    }

    @Transactional
    public void delete(Long id) {
        userRepository.findById(id).orElseThrow(
                () -> new ModelNotFoundException(String.format("Пользователь с id %d отсутствует", id)));
        userRepository.deleteById(id);
        log.info("Пользователь с id - {} удален!", id);
    }

    //Внутреннее пользование
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ModelNotFoundException(String.format("Пользователь с id %d отсутствует", id)));
    }
}
