package subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

//상속을 통해 bean으로 등록
public interface StationRepository extends JpaRepository<Station, Long> {

    Station findByName(String name);
}
