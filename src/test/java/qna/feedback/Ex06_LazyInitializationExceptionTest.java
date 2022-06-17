package qna.feedback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.feedback.entity.lazy.Team;
import qna.feedback.generator.MemberGenerator;
import qna.feedback.generator.TeamGenerator;
import qna.feedback.service.TeamService;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Import({TeamGenerator.class, MemberGenerator.class})
@DisplayName("코드 리뷰 피드백 : 다른 트랜잭션에서 프록시 객체 초기화 시 발생하는 LazyInitializationException 알아보기")
public class Ex06_LazyInitializationExceptionTest {

    private final EntityManager entityManager;
    private final TeamGenerator teamGenerator;
    private final TeamService teamService;
    private final MemberGenerator memberGenerator;

    public Ex06_LazyInitializationExceptionTest(
        EntityManager entityManager,
        TeamGenerator teamGenerator,
        TeamService teamService,
        MemberGenerator memberGenerator
    ) {
        this.entityManager = entityManager;
        this.teamGenerator = teamGenerator;
        this.teamService = teamService;
        this.memberGenerator = memberGenerator;
    }

    private PersistenceUnitUtil persistenceUnitUtil;

    @BeforeEach
    void setUp() {
        persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
    }

    @Test
    @DisplayName("트랜잭션 범위를 벗어난 프록시 객체 초기화 시도 시 LazyInitializationException 발생 여부 검증")
    public void lazyInitializationExceptionTest() {
        // Given
        final Team savedTeam = teamGenerator.savedTeam();
        memberGenerator.savedMembers(savedTeam, 3);
        entityManager.clear();

        // When
        Team actual = teamService.getTeamWithJpqlJoin(savedTeam.getId());

        // Then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(persistenceUnitUtil.isLoaded(actual.getMembers()))
                .as("join을 이용해 연관 Entity가 조회 되지 않고 프록시 객체로 채워짐 검증")
                .isFalse(),
            () -> assertThatExceptionOfType(LazyInitializationException.class)
                .isThrownBy(() -> System.out.println(actual.getMembers()))
                .as("트랜잭션 범위 밖에서 프록시 객체 초기화 시도 시 LazyInitializationException 발생")
        );
    }
}
