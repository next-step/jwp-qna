package subway.domain;


import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository를 상속받는 Interface 생성
 *  - JpaRepository나 CRUD 관련 인터페이스를 상속받으면
 *  - @Repository를 사용하지 않아도 기본적으로 Bean이 됨.
 */
public interface StationRepository extends JpaRepository<Station, Long> {
	Station findByName(String name);
}
