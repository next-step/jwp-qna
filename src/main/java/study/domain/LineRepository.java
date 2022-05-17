package study.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface LineRepository extends JpaRepository<Line, Long> {
    Line findByName(String name);
}
