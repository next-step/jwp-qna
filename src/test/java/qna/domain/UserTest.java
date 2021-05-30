package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("객체 저장 테스트")
    @Test
    void save() {
        // given
        final User expected = userRepository.save(JAVAJIGI);

        // when
        final Optional<User> optActual = userRepository.findByUserId(JAVAJIGI.getUserId());
        final User actual = optActual.orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("userId 필드를 조건으로 검색 테스트")
    @Test
    void findByUserId() {
        // given
        userRepository.save(JAVAJIGI);

        // when
        final Optional<User> optUser = userRepository.findByUserId(JAVAJIGI.getUserId());

        // then
        final User user = optUser.orElseThrow(IllegalArgumentException::new);
        assertAll(
            () -> assertThat(user).isNotNull(),
            () -> assertThat(user).isEqualTo(JAVAJIGI),
            () -> assertThat(user.getId()).isNotNull()
        );
    }

    @DisplayName("deleteAllInBatch 실행하여 데이터가 정상적으로 삭제되는지 확인")
    @Test
    void deleteAllInBatch() {
        // given
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
        userRepository.deleteAllInBatch();

        // when
        final List<User> actual = userRepository.findAll();

        // then
        assertThat(actual.size()).isEqualTo(0);
    }

    @DisplayName("ID를 기준으로 내림차순 정렬하여 검색 테스트")
    @Test
    void findAllWithSortId() {
        // given
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);

        // when
        final List<User> actual = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        // then
        assertAll(
            () -> assertThat(actual.size()).isEqualTo(2),
            () -> assertThat(actual).containsExactly(SANJIGI, JAVAJIGI)
        );
    }

    @DisplayName("페이징 옵션을 사용해서 전체 검색 테스트")
    @Test
    void name() {
        // given
        for (int i = 0; i < 20; i++) {
            userRepository.save(new User("id" + i, "password", "name", "email"));
        }

        // when
        final Page<User> actual = userRepository.findAll(PageRequest.of(1, 10));

        // then
        assertAll(
            () -> assertThat(actual.getTotalElements()).isEqualTo(20),
            () -> assertThat(actual.getTotalPages()).isEqualTo(2),
            () -> assertThat(actual.getContent().size()).isEqualTo(10),
            () -> assertThat(actual.getContent().get(0).getId()).isEqualTo(11L)
        );
    }

    @DisplayName("persist 와 merge 후 객체 비교")
    @Test
    void persistVsMerge() {
        // given
        final User user = new User(1L, "id", "password", "name", "email");
        final User unmanagedUser = new User(1L, "id", "password", "name", "email");
        final User managedUser = userRepository.save(user);
        final EntityManager entityManager = this.entityManager.getEntityManager();
        final User mergedUser = entityManager.merge(user);

        // when
        final boolean userActual = entityManager.contains(user);
        final boolean managedUserActual = entityManager.contains(managedUser);
        final boolean unmanagedUserActual = entityManager.contains(unmanagedUser);
        final boolean mergedUserActual = entityManager.contains(mergedUser);

        // then
        assertAll(
            () -> assertThat(userActual).as("persist 에 argument 로 전달한 객체").isEqualTo(false),
            () -> assertThat(managedUserActual).as("persist 후 반환된 객체").isEqualTo(true),
            () -> assertThat(unmanagedUserActual).as("persist 하지 않은 객체").isEqualTo(false),
            () -> assertThat(mergedUserActual).as("merge 실행 후 반환된 객체").isEqualTo(true),
            () -> assertThat(user).as("persist 전후 객체 비교").isNotEqualTo(managedUser),
            () -> assertThat(managedUser).as("persist 후 반환된 객체와 merge 후 반환된 객체 비교").isEqualTo(mergedUser)
        );
    }
}
