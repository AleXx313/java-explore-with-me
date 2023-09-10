package ru.practicum.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {

    private Long id;
    @NotBlank
    @Size(max = 255)
    private String app;
    @NotBlank
    @Size(max = 255)
    private String uri;
    @NotBlank
    @Size(max = 16)
    private String ip;
    @NotBlank
    private String timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndpointHitDto dto = (EndpointHitDto) o;
        return Objects.equals(id, dto.id) && Objects.equals(app, dto.app) && Objects.equals(uri, dto.uri) && Objects.equals(ip, dto.ip) && Objects.equals(timestamp, dto.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, app, uri, ip, timestamp);
    }
}