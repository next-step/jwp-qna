package qna.study;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;

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
@DisplayName("N + 1 관련 학습 테스트")
class Study3_N_PlusOneProblemTest {

	private final EntityManager entityManager;
	private final TeamRepository teamRepository;
	private final TeamGenerator teamGenerator;
	private final MemberGenerator memberGenerator;
	private final ClassRepository classRepository;
	private final ClassGenerator classGenerator;
	private final StudentGenerator studentGenerator;
	private PersistenceUnitUtil persistenceUnitUtil;

	public Study3_N_PlusOneProblemTest(EntityManager entityManager, TeamRepository teamRepository,
		TeamGenerator teamGenerator, MemberGenerator memberGenerator, ClassRepository classRepository,
		ClassGenerator classGenerator, StudentGenerator studentGenerator) {
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
	@DisplayName("EAGER N+1 문제 발생")
	void eagerNPlusOneProblem() {
		// Given
		final Class firstClass = classGenerator.savedClass();
		final Class secondClass = classGenerator.savedClass();
		final Class thirdClass = classGenerator.savedClass();
		studentGenerator.savedStudents(firstClass, 1);
		studentGenerator.savedStudents(secondClass, 2);
		studentGenerator.savedStudents(thirdClass, 3);
		entityManager.clear();

		/**
		 * N+1 문제가 발생하는 쿼리를 확인
		 * Eager 전략으로 인해 Class 를 조회할 때, Student 를 함께 조회하게 된다.
		 * 따라서 Class 를 조회 할 때 의도치 않게 Student 의 수 만큼 추가 쿼리 발생
		 */
		classRepository.findAll();
	}

	@Test
	@DisplayName("LAZY N+1 문제 발생")
	void lazyNPlusOneProblem() {
		// Given
		Team firstTeam = teamGenerator.savedTeam();
		Team secondTeam = teamGenerator.savedTeam();
		Team thirdTeam = teamGenerator.savedTeam();
		memberGenerator.savedMembers(firstTeam, 3);
		memberGenerator.savedMembers(secondTeam, 2);
		memberGenerator.savedMembers(thirdTeam, 3);
		entityManager.clear();

		/**
		 * N+1 문제가 발생하는 쿼리를 확인
		 * Lazy 전략으로 인해 Team 을 조회할 때, Members 를 함께 조회하지 않는다.
		 * Members 는 프록시 객체로 조회된다.
		 * 하지만 Team 의 Members 에 접근 하는 경우 프록시 객체를 초기화 하기 위해 추가 쿼리 발생
		 * -> Team 의 Members 를 조회하기 위해 Team 의 개수만큼 추가 쿼리 발생
		 */
		List<Team> actual = teamRepository.findAll();

		// Then
		long plusN_QueryCount = actual.stream()
			.map(team -> persistenceUnitUtil.isLoaded(team.getMembers()))
			.count();

		assertThat(plusN_QueryCount).isEqualTo(3);
	}

	@Test
	@DisplayName("@EntityGraph 를 통한 N+1 문제 해결")
	void solveNPlusOneProblemByUsingEntityGraph() {
		// Given
		final Class firstClass = classGenerator.savedClass();
		final Class secondClass = classGenerator.savedClass();
		final Class thirdClass = classGenerator.savedClass();
		studentGenerator.savedStudents(firstClass, 1);
		studentGenerator.savedStudents(secondClass, 2);
		studentGenerator.savedStudents(thirdClass, 3);
		entityManager.clear();

		List<Class> actual = classRepository.findAll();

		// Then
		Set<Boolean> isInitialized = actual.stream()
			.map(clazz -> Hibernate.isInitialized(clazz.getStudents()))
			.collect(Collectors.toSet());
		assertThat(isInitialized).containsExactly(true);
	}

	@Test
	@DisplayName("fetchJoin 을 통한 N+1 문제 해결")
	void solveNPlusOneProblemByUsingFetchJoin() {
		// Given
		final Class firstClass = classGenerator.savedClass();
		final Class secondClass = classGenerator.savedClass();
		final Class thirdClass = classGenerator.savedClass();
		studentGenerator.savedStudents(firstClass, 1);
		studentGenerator.savedStudents(secondClass, 2);
		studentGenerator.savedStudents(thirdClass, 3);
		entityManager.clear();

		List<Class> actual = classRepository.findAllWithJpqlFetchJoin();

		// Then
		Set<Boolean> isInitialized = actual.stream()
			.map(clazz -> Hibernate.isInitialized(clazz.getStudents()))
			.collect(Collectors.toSet());
		assertThat(isInitialized).containsExactly(true);
	}
}
