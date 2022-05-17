package study.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface StationRepository extends JpaRepository<Station, Long> {
    Station findByName(String expected);
}
