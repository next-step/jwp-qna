package qna.feedback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.domain.BaseEntity;
import qna.feedback.entity.lazy.Member;
import qna.feedback.repository.MemberRepository;

@DataJpaTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@DisplayName("Test:JPA Auditing")
class Ex01_AuditingTest {

    private final MemberRepository memberRepository;
    private final EntityManager entityManager;

    public Ex01_AuditingTest(
        MemberRepository memberRepository,
        EntityManager entityManager
    ) {
        this.memberRepository = memberRepository;
        this.entityManager = entityManager;
    }

    private List<Member> given;

    @BeforeEach
    void setUp() {
        given = Arrays.asList(
            new Member("name1", 10),
            new Member("name2", 15),
            new Member("name3", 20),
            new Member("name4", 30),
            new Member("name5", 40)
        );
        memberRepository.saveAll(given); // save 시점에 @Id 채번을 위한 flush 발생
    }

    @Test
    @DisplayName("하나의 트랜잭션 내에서 여러개의 엔티티 저장 시 생성일시 값 할당에 대한 학습 테스트")
    public void createDateTest() {
        // When
        Set<LocalDateTime> actual = given.stream()
            .map(BaseEntity::getCreatedAt)
            .collect(Collectors.toSet());

        // Then
        assertThat(actual).as("각 Entity의 @PrePersist 콜백 메서드 발생 시점이 다름을 확인")
            .size().isNotEqualTo(1);
    }

    @Test
    @DisplayName("하나의 트랜잭션 내에서 여러개의 엔티티 수정 시 수정일시 값 할당에 대한 학습 테스트")
    public void lastModifiedDateTest() {
        // When
        given.forEach(member -> member.changeName("changedName"));
        entityManager.flush(); // 쓰기 지연 저장소에 저장된 update 쿼리를 DB에 반영
        Set<LocalDateTime> actual = given.stream()
            .map(BaseEntity::getUpdatedAt)
            .collect(Collectors.toSet());

        // Then
        assertThat(actual).as("각 Entity의 @PreUpdate 콜백 메서드 발생 시점이 다름을 확인")
            .size().isNotEqualTo(1);
    }

    @Test
    @DisplayName("신규 Entity 여부를 판별하는 `@Id-Property inspection` 기본 전략 Test")
    public void PK_isNewTest() {
        // Given
        EntityInformation<Member, Long> entityInformation = new JpaMetamodelEntityInformation<>(Member.class,
            entityManager.getMetamodel());
        final Member given = new Member("최용석", 32);

        // When & Then
        assertThat(entityInformation.isNew(given)).as("@Id 필드가 Null인 경우 isNew True 여부")
            .isTrue();

        Member actual = memberRepository.save(given);
        assertThat(entityInformation.isNew(actual)).as("IDENTITY전략에 의해 @Id 필드가 db에서 채번된경우 isNew False 여부")
            .isFalse();
    }

    @Test
    @DisplayName("@Modifying를 이용해 JPQL로 작성된 쿼리 동작 시 JPA Audit 동작 확인을 위한 TC")
    public void increaseAgeOfAllMembersOverTest() {
        // Given
        entityManager.clear(); // 운영과 비슷한 환경을 구성하기 위해 영속성 켄텍스트를 초기화
        assertAll(
            () -> assertThat(given).extracting("age").containsExactly(10, 15, 20, 30, 40),
            () -> assertThat(given).allMatch(member -> member.getUpdatedAt() == null)
        );
        final int updateCriteriaAge = 20;

        // When
        // 영속성 컨텍스트는 비어있는 상태이므로 PreUpdate 콜백 메서드의 isNew 상태값에 따른 수정일시 설정이 수행되지 않음
        // JPQL로 작성된 Bulk Update 쿼리는 영속성 컨텍스트에 의한 Entity 변경감지 의해 수행되는 것이 아니므로 쿼리는 정상 동작
        int updatedRowCount = memberRepository.increaseAgeOfAllMembersOver(updateCriteriaAge);

        // Then
        List<Member> actual = memberRepository.findAll();
        assertAll(
            () -> assertThat(updatedRowCount).isEqualTo(3),
            () -> assertThat(actual).extracting("age")
                .as("영속성 켄텍스트와 별개로 동작하는 JPQL 쿼리 반영 여부 확인")
                .containsExactly(10, 15, 21, 31, 41),
            () -> assertThat(actual)
                .as("영속성 컨텍스트에 의해 관리되는 엔티티가 아닌 경우 JPA Audit 미적용 여부 확인")
                .allMatch(member -> member.getUpdatedAt() == null)
        );
    }
}
