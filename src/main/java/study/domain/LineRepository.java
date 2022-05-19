package study.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, String> {
    Line findByName(String name);
}
