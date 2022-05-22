package subway.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, Long> {
    public Optional<Line> findByName(String name);

}
