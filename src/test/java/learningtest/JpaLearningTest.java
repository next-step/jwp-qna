package learningtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.persistence.PersistenceException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@Import(TestEntityDao.class)
class JpaLearningTest {

	@Autowired
	TestEntityRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	TestEntityDao testEntityDao;

	@BeforeEach
	void setUp() {
		repository.deleteAll();
		flush();
	}

	@Test
	void 엔티티_생성확인() {
		TestEntity 테스트_엔티티 = repository.save(new TestEntity("필드1"));

		assertThat(테스트_엔티티.getId()).isNotNull();
	}

	@Test
	void 영속성_컨텍스트에는_존재하지만_DB에는_없는_경우_DB에_반영된다() {
		TestEntity 테스트_엔티티 = new TestEntity("필드1");
		entityManager.merge(테스트_엔티티);
		assertThat(testEntityDao.getCount()).isEqualTo(1);
	}

	@Test
	void merge되지_않은_엔티티를_persist할_수_없다() {
		TestEntity 엔티티 = 저장후_캐시비움("필드1");
		엔티티.update("필드1-수정");
		assertThatThrownBy(() -> entityManager.persist(엔티티))
			.isInstanceOf(PersistenceException.class);
	}

	@Test
	void 영속화되지_않은_경우_변경감지가_불가능하다() {
		TestEntity 엔티티 = entityManager.merge(new TestEntity("필드1"));
		flush();

		entityManager.detach(엔티티);
		엔티티.update("필드1-수정");
		flush();

		TestEntity 실제_엔티티 = testEntityDao.findById(엔티티.getId());
		assertThat(실제_엔티티.getColumn()).isNotEqualTo(엔티티.getColumn());
	}

	private TestEntity 저장후_캐시비움(String fieldName) {
		TestEntity testEntity = repository.save(new TestEntity(fieldName));
		flush();
		return testEntity;
	}

	@AfterEach
	void clean() {
		repository.deleteAll();
		flush();
	}

	void flush() {
		entityManager.flush();
		entityManager.clear();
	}
}
