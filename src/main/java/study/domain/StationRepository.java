package study.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, String> {
    Station findByName(String name);

    Station findById(long id);
}
