package subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
    Station findByName(String name); //Optional 고려해볼것
}
