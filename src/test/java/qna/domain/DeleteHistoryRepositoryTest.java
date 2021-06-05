package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository users;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("mwkwon", "password", "권민욱", "mwkwon0110@gmail.com");
        users.save(user);
    }

    @Test
    @DisplayName("회원 정보 테이블 정상 저장")
    void save() {
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, user);
        DeleteHistory actual = deleteHistories.save(expected);
        assertThat(actual.equals(expected)).isTrue();
    }

    @Test
    @DisplayName("유저 아이디 기준 조회")
    void findById() {
        DeleteHistory expected = setUpTestDeleteHistory();
        entityManager.flush();
        entityManager.clear();

        Optional<DeleteHistory> actual = deleteHistories.findById(expected.id());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().id()).isEqualTo(expected.id());
    }

    @Test
    @DisplayName("회원 정보 테이블 정상 수정")
    void update() {
        DeleteHistory expected = setUpTestDeleteHistory();
        expected.changeContentType(ContentType.ANSWER);
        entityManager.flush();
        entityManager.clear();
        Optional<DeleteHistory> actual = deleteHistories.findById(expected.id());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().isSameContentType(ContentType.ANSWER)).isTrue();
    }

    @Test
    @DisplayName("회원 정보 테이블 정상 삭제")
    void delete() {
        DeleteHistory expected = setUpTestDeleteHistory();
        deleteHistories.delete(expected);
        entityManager.flush();
        entityManager.clear();
        Optional<DeleteHistory> actual = deleteHistories.findById(expected.id());
        assertThat(actual.isPresent()).isFalse();
    }

    private DeleteHistory setUpTestDeleteHistory() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, user);
        return deleteHistories.save(deleteHistory);
    }

}
