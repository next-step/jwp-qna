package qna.feedback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
import qna.NotFoundException;
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
@DisplayName("코드 리뷰 피드백 : FetchType.LAZY VS FetchType.EAGER 동작 비교하기")
public class Ex02_Lazy_Type_And_Eager_Type_Test {

    private final EntityManager entityManager;
    private final TeamRepository teamRepository;
    private final TeamGenerator teamGenerator;
    private final MemberGenerator memberGenerator;
    private final ClassRepository classRepository;
    private final ClassGenerator classGenerator;
    private final StudentGenerator studentGenerator;
    private PersistenceUnitUtil persistenceUnitUtil;

    public Ex02_Lazy_Type_And_Eager_Type_Test(
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
    @DisplayName("FetchType.EAGER 전략으로 인한 연관 Entity의 즉시 로딩 여부 검증")
    public void fetchTypeEagerTest() {
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
    @DisplayName("FetchType.LAZY 전략으로 인한 연관 Entity의 프록시 객체 여부 검증")
    public void fetchTypeLazyTest() {
        // Given
        final Team savedTeam = teamGenerator.savedTeam();
        memberGenerator.savedMembers(savedTeam, 3);
        entityManager.clear();

        // When
        Team actual = teamRepository.findById(savedTeam.getId())
            .orElseThrow(NotFoundException::new);

        // Then
        assertAll(
            () -> assertThat(Hibernate.isInitialized(actual.getMembers())).isFalse(),
            () -> assertThat(persistenceUnitUtil.isLoaded(actual.getMembers())).isFalse()
        );
    }
}
