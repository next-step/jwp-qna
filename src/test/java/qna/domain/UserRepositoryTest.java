package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.NotFoundException;
import qna.generator.UserGenerator;
import qna.repository.UserRepository;

@DataJpaTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Import(UserGenerator.class)
@DisplayName("Repository:User")
class UserRepositoryTest {

    private final UserRepository userRepository;
    private final UserGenerator userGenerator;
    private final EntityManager entityManager;

    public UserRepositoryTest(
        UserRepository userRepository,
        UserGenerator userGenerator,
        EntityManager entityManager
    ) {
        this.userRepository = userRepository;
        this.userGenerator = userGenerator;
        this.entityManager = entityManager;
    }

    @Test
    @DisplayName("회원 저장")
    public void saveUserTest() {
        // Given & When
        User given = userRepository.save(UserGenerator.generateQuestionWriter());

        // Then
        assertAll(
            () -> assertThat(given.getUserId()).as("IDENTITY 전략에 따라 DB에서 부여된 PK값 생성 여부").isNotNull(),
            () -> assertThat(given.getCreatedAt()).as("JPA Audit에 의해 할당되는 생성일시 정보의 할당 여부").isNotNull(),
            () -> assertThat(given.getUpdatedAt()).as("JPA Audit의 modifyOnCreate 설정에 의한 수정일시 정보 Null 여부").isNull()
        );
    }

    @Test
    @DisplayName("회원 번호를 이용한 회원 조회")
    public void findByIdTest() {
        // Given
        User given = userGenerator.savedUser();

        // When
        User actual = userRepository.findById(given.getId())
            .orElseThrow(NotFoundException::new);

        // Then
        assertThat(actual).as("동일 트랜잭션 내 객체 동일성 보장 여부").isSameAs(given);
    }

    @Test
    @DisplayName("회원 아이디를 이용한 회원 조회")
    public void findByUserIdTest() {
        // Given
        User given = userGenerator.savedUser();

        // When
        User actual = userRepository.findByUserId(given.getUserId())
            .orElseThrow(NotFoundException::new);

        // Then
        assertThat(actual).as("동일 트랜잭션 내 객체 동일성 보장 여부").isSameAs(given);
    }

    @Test
    @DisplayName("변경 감지에 의한 회원 정보 수정")
    public void updateTest() {
        // Given
        final User given = userGenerator.savedUser();
        final String newName = "new Name";
        final String newEmail = "new email";
        User target = new User(given.getUserId(), given.getPassword(), newName, newEmail);

        // When
        given.update(given, target);
        entityManager.flush();

        // Then
        assertAll(
            () -> assertThat(given.getName()).isEqualTo(newName),
            () -> assertThat(given.getEmail()).isEqualTo(newEmail),
            () -> assertThat(given.getUpdatedAt()).as("flush 시점에 @PreUpdate 콜백 메서드에 의한 생성일시 값 할당 여부")
                .isNotNull()
        );
    }
}
