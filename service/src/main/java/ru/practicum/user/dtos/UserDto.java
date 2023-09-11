package ru.practicum.user.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDto {

    private Long id;
    @NotBlank()
    @Size(max = 255)
    private String name;
    @Email()
    @NotEmpty()
    @Size(max = 255)
    private String email;
}
