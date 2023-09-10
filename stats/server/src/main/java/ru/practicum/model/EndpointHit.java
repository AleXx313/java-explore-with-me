package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "endpoint_hits")
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String app;
    private String uri;
    private String ip;
    @Column(name = "hit_time")
    private LocalDateTime hitTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndpointHit that = (EndpointHit) o;
        return Objects.equals(id, that.id) && Objects.equals(app, that.app) && Objects.equals(uri, that.uri) && Objects.equals(ip, that.ip) && Objects.equals(hitTime, that.hitTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, app, uri, ip, hitTime);
    }
}
