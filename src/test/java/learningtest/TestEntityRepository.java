package learningtest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TestEntityRepository extends JpaRepository<TestEntity, Long> {
}
