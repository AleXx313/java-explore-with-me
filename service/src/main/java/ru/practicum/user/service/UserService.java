package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    public UserDto save(UserDto dto) {
        User savedUser = userRepository.save(UserMapper.dtoToUser(dto));
        log.info("Сохранен пользователь по имени - {} с id - {}!", savedUser.getName(), savedUser.getId());
        return UserMapper.userToDto(savedUser);
    }

    public List<UserDto> find(List<Long> ids, Integer from, Integer size) {
        return null;
    }

    public void delete(Long id) {

    }
}
