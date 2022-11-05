package learningtest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class DataJpaTestLearningTest {

	@Autowired
	TestEntityRepository testEntities;

	@Autowired
	TestEntityManager entityManager;

	@Test
	void 엔티티_생성확인() {
		TestEntity 테스트_엔티티 = testEntities.save(new TestEntity("필드1"));

		assertThat(테스트_엔티티.getId()).isNotNull();
	}

	void flush() {
		entityManager.flush();
		entityManager.clear();
	}
}
