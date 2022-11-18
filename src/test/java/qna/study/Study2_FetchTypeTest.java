package qna.study;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;

import qna.NotFoundException;
import qna.study.domain.eager.Class;
import qna.study.domain.lazy.Team;
import qna.study.generator.ClassGenerator;
import qna.study.generator.MemberGenerator;
import qna.study.generator.StudentGenerator;
import qna.study.generator.TeamGenerator;
import qna.study.repository.ClassRepository;
import qna.study.repository.TeamRepository;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import({TeamGenerator.class, MemberGenerator.class, ClassGenerator.class, StudentGenerator.class})
@DisplayName("Fetch 타입에 따른 동작 비교 테스트")
class Study2_FetchTypeTest {

	private final EntityManager entityManager;
	private final TeamRepository teamRepository;
	private final TeamGenerator teamGenerator;
	private final MemberGenerator memberGenerator;
	private final ClassRepository classRepository;
	private final ClassGenerator classGenerator;
	private final StudentGenerator studentGenerator;
	private PersistenceUnitUtil persistenceUnitUtil;

	public Study2_FetchTypeTest(
		EntityManager entityManager,
		TeamRepository teamRepository,
		TeamGenerator teamGenerator,
		MemberGenerator memberGenerator,
		ClassRepository classRepository,
		ClassGenerator classGenerator,
		StudentGenerator studentGenerator
	) {
		this.entityManager = entityManager;
		this.teamRepository = teamRepository;
		this.teamGenerator = teamGenerator;
		this.memberGenerator = memberGenerator;
		this.classRepository = classRepository;
		this.classGenerator = classGenerator;
		this.studentGenerator = studentGenerator;
	}

	@BeforeEach
	void setUp() {
		persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
	}

	@Test
	@DisplayName("EAGER 전략으로 인한 연관 즉시 로딩 여부 검증")
	void fetchTypeEagerTest() {
		// Given
		final Class savedClass = classGenerator.savedClass();
		studentGenerator.savedStudents(savedClass, 3);
		entityManager.clear();

		// When
		Class actual = classRepository.findById(savedClass.getId())
			.orElseThrow(NotFoundException::new);

		// Then
		assertAll(
			() -> assertThat(Hibernate.isInitialized(actual.getStudents())).isTrue(),
			() -> assertThat(persistenceUnitUtil.isLoaded(actual.getStudents())).isTrue()
		);
	}


	@Test
	@DisplayName("LAZY 전략으로 인한 연관 즉시 로딩 여부 검증")
	void fetchTypeLazyTest() {
		// Given
		final Team team = teamGenerator.savedTeam();
		memberGenerator.savedMembers(team, 3);
		entityManager.clear();

		// When
		Team actual = teamRepository.findById(team.getId())
			.orElseThrow(NotFoundException::new);

		// Then
		assertAll(
			() -> assertThat(Hibernate.isInitialized(actual.getMembers())).isFalse(),
			() -> assertThat(persistenceUnitUtil.isLoaded(actual.getMembers())).isFalse()
		);
	}

}
