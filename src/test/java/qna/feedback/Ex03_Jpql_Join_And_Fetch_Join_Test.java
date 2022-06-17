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
import qna.feedback.entity.lazy.Team;
import qna.feedback.generator.MemberGenerator;
import qna.feedback.generator.TeamGenerator;
import qna.feedback.repository.TeamRepository;

/**
 * join vs join fetch EntityManager.
 */
@DataJpaTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Import({TeamGenerator.class, MemberGenerator.class})
@DisplayName("코드 리뷰 피드백 : JPQL의 Join VS Fetch Join 동작 비교하기")
public class Ex03_Jpql_Join_And_Fetch_Join_Test {

    private final EntityManager entityManager;
    private final TeamRepository teamRepository;
    private final TeamGenerator teamGenerator;
    private final MemberGenerator memberGenerator;
    private PersistenceUnitUtil persistenceUnitUtil;

    public Ex03_Jpql_Join_And_Fetch_Join_Test(
        EntityManager entityManager,
        TeamRepository teamRepository,
        TeamGenerator teamGenerator,
        MemberGenerator memberGenerator
    ) {
        this.entityManager = entityManager;
        this.teamRepository = teamRepository;
        this.teamGenerator = teamGenerator;
        this.memberGenerator = memberGenerator;
    }

    @BeforeEach
    void setUp() {
        persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
    }

    @Test
    @DisplayName("JPQL-Join : Select 절의 대상 Entity만 조회, 연관된 Entity의 프록시 객체 여부 검증")
    public void jpqlJoinTest() {
        // Given
        final Team savedTeam = teamGenerator.savedTeam();
        memberGenerator.savedMembers(savedTeam, 3);
        entityManager.clear();

        // When
        Team actual = teamRepository.getTeamByIdByJpqlJoin(savedTeam.getId());

        // Then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(Hibernate.isInitialized(actual.getMembers()))
                .as("Join을 이용한 조회 시, Select 절의 조회 대상인 `Team` Entity만 조회, 연관관계를 가지는 `Member` Entity는 프록시로 할당")
                .isFalse(),
            () -> assertThat(persistenceUnitUtil.isLoaded(actual.getMembers())).isFalse()
        );
    }

    @Test
    @DisplayName("JPQL-Fetch Join : Entity 조회 시 연관 Entity의 즉시 로딩 여부 검증")
    public void jpqlFetchJoinTest() {
        // Given
        final Team savedTeam = teamGenerator.savedTeam();
        memberGenerator.savedMembers(savedTeam, 3);
        entityManager.clear();

        // When
        Team actual = teamRepository.getTeamByIdByJpqlFetchJoin(savedTeam.getId());

        // Then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(Hibernate.isInitialized(actual.getMembers())).isTrue(),
            () -> assertThat(persistenceUnitUtil.isLoaded(actual.getMembers())).isTrue()
        );
    }
}
