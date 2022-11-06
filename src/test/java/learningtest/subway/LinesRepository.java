package learningtest.subway;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LinesRepository extends JpaRepository<Line, Long> {
}
