package qna.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.NotFoundException;

/**
 * UserRepository 인터페이스 테스트 케이스 작성
 * JpaRepository가 제공하는 기본 기능 테스트
 */
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    private User user;
    private User javajigiUser;
    private User sanjigiUser;
    private List<User> users;

    @BeforeEach
    public void beforeEach() {
        this.user = User.copy(UserTest.HAGI);
        this.javajigiUser = User.copy(UserTest.JAVAJIGI);
        this.sanjigiUser = User.copy(UserTest.SANJIGI);
        this.users = Arrays.asList(this.user, this.javajigiUser, this.sanjigiUser)
                .stream()
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("User 저장")
    void save() {
        // when
        User resultUser = repository.save(this.user);

        // then
        assertAll(
                () -> assertThat(resultUser).isNotNull(),
                () -> assertThat(resultUser.getId()).isEqualTo(this.user.getId()),
                () -> assertThat(resultUser.getEmail()).isEqualTo(this.user.getEmail()),
                () -> assertThat(resultUser.getUserId()).isEqualTo(this.user.getUserId())
        );
    }

    @Test
    @DisplayName("일괄 저장 처리")
    void save_all() {
        // when
        List<User> resultUsers = repository.saveAll(this.users);

        // then
        assertThat(resultUsers.size()).isEqualTo(users.size());
    }

    @Test
    @DisplayName("PK로 User 찾기")
    void findById() {
        // given
        repository.saveAll(this.users);

        // when
        User findUser = repository.findById(this.user.getId()).get();

        // then
        assertThat(findUser).isSameAs(this.user);
    }

    @Test
    @DisplayName("USER_ID로 User 찾기")
    void findByUserId() {
        // given
        repository.saveAll(this.users);

        // when
        User findUser = repository.findByUserId(this.user.getUserId()).get();

        // then
        assertThat(findUser).isSameAs(this.user);
    }

    @Test
    @DisplayName("사용자 찾기 실패 시 NotFoundException 예외처리")
    void notFoundException() {
        // then
        assertThatThrownBy(() -> repository.findById(10L).orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("사용자 전체 찾기")
    void select_all() {
        // given
        List<User> saveUsers = repository.saveAll(this.users);
        List<Long> saveUserIds = saveUsers
                .stream()
                .map(user -> user.getId())
                .collect(Collectors.toList());

        // when
        List<User> findAllUser = repository.findAll();
        List<Long> findAllUserIds = findAllUser.stream()
                .map(user -> user.getId())
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(findAllUserIds.containsAll(saveUserIds)).isTrue(),
                () -> assertThat(saveUsers.size()).isEqualTo(findAllUser.size())
        );
    }

    @Test
    @DisplayName("사용자 정보 수정")
    void update() {
        // given
        User saveUser = repository.save(this.user);
        User targetUser = new User(this.user.getId(), this.user.getUserId(), this.user.getPassword(),
                "JANG", "JANG@gmail.com");

        // when
        saveUser.update(saveUser, targetUser);
        User updateUser = repository.findByUserId(saveUser.getUserId()).get();

        // then
        assertAll(
                () -> assertThat(updateUser).isNotNull(),
                () -> assertThat(updateUser.getName()).isEqualTo(targetUser.getName()),
                () -> assertThat(updateUser.getEmail()).isEqualTo(targetUser.getEmail())
        );
    }

    @Test
    @DisplayName("사용자 삭제처리")
    void delete() {
        // given
        repository.saveAll(this.users);

        // when
        repository.delete(this.user);

        // then
        assertAll(
                () -> assertThatThrownBy(() -> repository.findByUserId(this.user.getUserId())
                        .orElseThrow(NotFoundException::new))
                        .isInstanceOf(NotFoundException.class),
                () -> assertThat(repository.count()).isEqualTo(users.size() - 1)
        );
    }

    @Test
    @DisplayName("사용자 전체 삭제")
    void delete_all() {
        // given
        repository.saveAll(this.users);

        // when
        repository.deleteAll();

        // then
        assertThat(repository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("사용자 다건 삭제")
    void delete_list() {
        // given
        List<User> resultUsers = repository.saveAll(this.users);
        List<User> deleteUsers = Arrays.asList(this.user, this.javajigiUser)
                .stream()
                .collect(Collectors.toList());

        // when
        repository.deleteAll(deleteUsers);
        Optional<User> findDeletedUser = repository.findByUserId(this.user.getUserId());

        // then
        assertAll(
                () -> assertThat(repository.count()).isEqualTo(users.size() - deleteUsers.size()),
                () -> assertThatThrownBy(() -> findDeletedUser.orElseThrow(NotFoundException::new))
                        .isInstanceOf(NotFoundException.class)
        );
    }

    @Test
    @DisplayName("PK 기준 사용자 삭제")
    void delete_by_id() {
        // given
        List<User> resultUsers = repository.saveAll(this.users);

        // when
        repository.deleteById(this.user.getId());

        assertAll(
                () -> assertThat(repository.count()).isEqualTo(2),
                () -> assertThatThrownBy(() -> repository.findById(this.user.getId())
                        .orElseThrow(NotFoundException::new)).isInstanceOf(NotFoundException.class)
        );
    }

    @Test
    @DisplayName("PK기준 데이터 존재유무 확인")
    void exists_by_id() {
        // given
        List<User> resultUsers = repository.saveAll(this.users);

        // when
        boolean exists = repository.existsById(this.javajigiUser.getId());

        // then
        assertThat(exists).isTrue();
    }
}
