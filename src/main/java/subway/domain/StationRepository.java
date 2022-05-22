package subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
    Station findByName(String name);
    //Optional<Station> findByName(String name);   // 데이터가 없는 경우 NPE 발생하므로 Optional을 붙여 줄수도 있다

    Station findById(long id);
}
