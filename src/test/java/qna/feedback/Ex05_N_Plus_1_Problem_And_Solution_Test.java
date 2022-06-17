package qna.feedback;


import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.feedback.entity.eager.Class;
import qna.feedback.entity.lazy.Team;
import qna.feedback.generator.ClassGenerator;
import qna.feedback.generator.MemberGenerator;
import qna.feedback.generator.StudentGenerator;
import qna.feedback.generator.TeamGenerator;
import qna.feedback.repository.ClassRepository;
import qna.feedback.repository.TeamRepository;

@DataJpaTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Import({TeamGenerator.class, MemberGenerator.class, ClassGenerator.class, StudentGenerator.class})
@DisplayName("코드 리뷰 피드백 : N+1이 문제와 N+1에 대한 해결 방안 알아보기")
public class Ex05_N_Plus_1_Problem_And_Solution_Test {

    private final EntityManager entityManager;
    private final TeamRepository teamRepository;
    private final TeamGenerator teamGenerator;
    private final MemberGenerator memberGenerator;
    private final ClassRepository classRepository;
    private final ClassGenerator classGenerator;
    private final StudentGenerator studentGenerator;
    private PersistenceUnitUtil persistenceUnitUtil;

    public Ex05_N_Plus_1_Problem_And_Solution_Test(
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
    @DisplayName("FetchType.EAGER의 N+1 발생 여부 검증")
    public void N_Plus_1_ProblemWithFetchTypeEager() {
        // Given
        final Class firstClass = classGenerator.savedClass();
        final Class secondClass = classGenerator.savedClass();
        final Class thirdClass = classGenerator.savedClass();
        studentGenerator.savedStudents(firstClass, 1);
        studentGenerator.savedStudents(secondClass, 2);
        studentGenerator.savedStudents(thirdClass, 3);
        entityManager.clear();

        /**
         * When
         * - FetchType.EAGER 설정으로 인해, `Class`조회 시 연관된 `Student`에 대한 조회 쿼리가 발생
         * - 따라서 `Class`목록 조회 시 의도하지 않은 경우에도, `Class`숫자만큼 `Student`가 조회되는 N+1 문제 발생
         *   - 최초 `Class` 목록 조회 시 1개 쿼리 발생, 최초 쿼리에 조회된 `Class`의 수 만큼 각 `Class`의 `Student` 조회 쿼리 N개 발생
         */
        classRepository.findAll();

        // Then : TODO FetchType.EAGER의 N+1 발생에 대한 검증 방법을 찾지 못했음
    }

    @Test
    @DisplayName("FetchType.Lazy의 N+1 발생 여부 검증")
    public void N_Plus_1_ProblemWithFetchTypeLazy() {
        // Given
        Team firstTeam = teamGenerator.savedTeam();
        Team secondTeam = teamGenerator.savedTeam();
        Team thirdTeam = teamGenerator.savedTeam();
        memberGenerator.savedMembers(firstTeam, 1);
        memberGenerator.savedMembers(secondTeam, 2);
        memberGenerator.savedMembers(thirdTeam, 3);
        entityManager.clear();

        /**
         * When
         * - FetchType.LAZY 설정으로 인해, 'Team'조회 시 연관 Entity인 `Member`는 프록시 객체로 할당되므로 N+1 문제는 발생하지 않음
         * - 그러나 각 `Team`의 `Member`에 접근하는 경우, 프록시 객체로 채워진 `Member`에 대한 초기화 과정으로 인한 조회 쿼리가 발생
         *   - 최초 Team 목록 조회 시 1개 쿼리 발생, 최초 쿼리에 조회된 각 `Team`의 `Member`에 접근 하는 경우 프록시 초기화 과정으로 인해
         *     `Team`의 수만큼 `Member` 조회 쿼리 N개 발생
         */
        List<Team> actual = teamRepository.findAll();

        // Then
        long plusN_QueryCount = actual.stream()
            .map(team -> persistenceUnitUtil.isLoaded(team.getMembers()))
            .count();
        assertThat(plusN_QueryCount)
            .as("member 프록시 객체 초기화로 인해 조회된 `Team`의 개수 만큼 추가 쿼리가 발생")
            .isEqualTo(3);
    }

    @Test
    @DisplayName("@EntityGraph의 attributePaths 속성을 이용한 N+1 문제 해결")
    public void solutionN_Plus_1_WithEntityGraph() {
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
        assertThat(isInitialized)
            .as("Class와 연관관계를 가지는 Student가 프록시가 아닌 로딩된 객체인지 여부 확인")
            .containsExactly(true);
    }

    @Test
    @DisplayName("Fetch Join을 이용한 N+1 문제 해결")
    public void solutionN_Plus_1_WithFetchJoin() {
        // Given
        final Class firstClass = classGenerator.savedClass();
        final Class secondClass = classGenerator.savedClass();
        final Class thirdClass = classGenerator.savedClass();
        studentGenerator.savedStudents(firstClass, 1);
        studentGenerator.savedStudents(secondClass, 2);
        studentGenerator.savedStudents(thirdClass, 3);
        entityManager.clear();

        /**
         * When : Fetch Join을 이용한 조회 시, 주의 사항
         * - 카테시안 곱 연산으로 인해 연관관계를 가지는 객체의 조회 수 만큼 조회 객체의 중복이 발생함에 주의
         * - 카테시안 곱 연산으로 인한 중복 문제는 `distinct` 혹은 일대다 필드의 타입을 Set(LinkedHashSet)으로 선언하여 해결 가능
         * - 단, Fetch Join은 페이징 처리 미적용에 대한 한계를 가짐에 주의
         * - batch size 설정을 이용하여 N+1 문제를 1+1로 해결
         *   - Global 레벨에서 default_batch_fetch_size 설정을 통해 해결 가능
         *   - Class 레벨에서 @BatchSzie를 명시하여 해결 가능
         *   - Field 레벨에서 @BatchSize를 명시하여 해결 가능
         */
        List<Class> actual = classRepository.findAllWithJpqlFetchJoin();

        // Then
        Set<Boolean> isInitialized = actual.stream()
            .map(clazz -> Hibernate.isInitialized(clazz.getStudents()))
            .collect(Collectors.toSet());
        assertThat(isInitialized)
            .as("Class와 연관관계를 가지는 Student가 프록시가 아닌 로딩된 객체인지 여부 확인")
            .containsExactly(true);
    }
}
