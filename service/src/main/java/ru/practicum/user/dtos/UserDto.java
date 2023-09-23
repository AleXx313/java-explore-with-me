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
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
    @Email
    @NotEmpty
    @Size(min = 6, max = 254)
    private String email;
}
