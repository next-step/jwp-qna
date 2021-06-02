package station.domains;

import org.springframework.data.jpa.repository.JpaRepository;

interface StationRepository extends JpaRepository<Station, Long> {
    Station findByName(String name); // (1)
}
