package qna.domain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.entity.DeleteHistory;
import qna.domain.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private UserRepository users;

    @Autowired
    private DeleteHistoryRepository deleteHistorys;

    @BeforeEach
    void setUp() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, Long.valueOf(1));

        deleteHistorys.save(deleteHistory);
    }

    @Test
    @DisplayName("delete_history테이블 save 테스트")
    void save() {
        User user = new User("diqksrk123", "diqksrk123", "강민준", "diqksrk123@naver.com");
        DeleteHistory expected =  new DeleteHistory(ContentType.ANSWER, Long.valueOf(2), user);
        DeleteHistory actual = deleteHistorys.save(expected);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("delete_history테이블 select 테스트")
    void findById() {
        DeleteHistory expected = deleteHistorys.findByContentId(Long.valueOf(1)).get();

        assertThat(expected).isNotNull();
    }

    @Test
    @DisplayName("delete_history테이블 update 테스트")
    void updateDeletedById() {
        User user = users.save(new User("diqksrk123", "diqksrk123", "강민준", "diqksrk123@naver.com"));
        DeleteHistory expected = deleteHistorys.findByContentId(Long.valueOf(1))
                .get();
        expected.setUser(user);
        DeleteHistory actual = deleteHistorys.findByContentId(Long.valueOf(1))
                .get();

        assertThat(actual.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("delete_history테이블 delete 테스트")
    void delete() {
        DeleteHistory expected = deleteHistorys.findByContentId(Long.valueOf(1))
                .get();
        deleteHistorys.delete(expected);

        assertThat(deleteHistorys.findByContentId(Long.valueOf(1)).isPresent()).isFalse();
    }

    @Test
    @DisplayName("deleteHistory연관관계 매핑 테스트")
    void getUserTest() {
        User actual = users.save(new User("diqksrk", "diqksrk", "강민준", "diqksrk123@naver.com"));
        DeleteHistory deleteHistory = deleteHistorys.findByContentId(Long.valueOf(1)).get();

        DeleteHistory savedQuestion = savedUserInfo(deleteHistory, actual);
        User expected = deleteHistory.getUser();

        assertAll(
                () -> assertThat(expected.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    private DeleteHistory savedUserInfo(DeleteHistory deleteHistory, User actual) {
        deleteHistory.setUser(actual);
        DeleteHistory savedDeleteHistory = deleteHistorys.save(deleteHistory);
        deleteHistorys.flush();
        return savedDeleteHistory;
    }

    @Test
    @DisplayName("toString 테스트")
    void toStringTest() {
        DeleteHistory deleteHistory = deleteHistorys.findByContentId(Long.valueOf(1))
                .get();

        assertThatNoException().isThrownBy(() -> deleteHistory.toString());
    }
}