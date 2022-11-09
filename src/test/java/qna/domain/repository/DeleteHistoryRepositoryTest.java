package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.user.User;
import qna.domain.user.email.Email;
import qna.domain.user.name.Name;
import qna.domain.user.password.Password;
import qna.domain.user.userid.UserId;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository repository;

    @Autowired
    UserRepository users;

    @Autowired
    TestEntityManager em;

    @Test
    @DisplayName("@Enumerated 애너테이션으로 ContentType 을 String 값으로 저장한다")
    void 엔티티_저장() {
        User loginUser = users.save(getUser(1L, "writer", "1111", "작성자", "writer@slipp.net"));
        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, 1L, loginUser, LocalDateTime.now());
        DeleteHistory actual = repository.save(expected);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual == expected).isTrue();
    }

    @Test
    @DisplayName("준영속 엔티티 비교시 equals() hashCode() 활용")
    void equals_hashCode() {
        User loginUser = users.save(getUser(null, "writer", "1111", "작성자", "writer@slipp.net"));
        DeleteHistory expected = repository
                .save(new DeleteHistory(ContentType.ANSWER, 1L, loginUser, LocalDateTime.now()));
        flushAndClear();
        DeleteHistory actual = repository.findById(expected.getId()).get();
        assertThat(actual).isEqualTo(expected);
        assertThat(actual == expected).isFalse();
    }

    @Test
    void Id로_삭제() {
        User loginUser = users.save(getUser(null, "writer", "1111", "작성자", "writer@slipp.net"));
        DeleteHistory actual = repository
                .save(new DeleteHistory(ContentType.ANSWER, 1L, loginUser, LocalDateTime.now()));
        repository.delete(actual);
        repository.flush();
    }

    @Test
    @DisplayName("(지연로딩)DeleteHistory 에서 User 으로 참조할 수 있다")
    void deleteHistory_to_user_lazy() {
        User loginUser = users.save(getUser(null, "writer", "1111", "작성자", "writer@slipp.net"));
        DeleteHistory deletehistory = repository
                .save(new DeleteHistory(ContentType.ANSWER, 1L, loginUser, LocalDateTime.now()));
        flushAndClear();
        DeleteHistory findDeleteHistory = repository.findById(deletehistory.getId()).get();
        assertThat(findDeleteHistory.getDeletedBy().getId()).isEqualTo(loginUser.getId());
    }

    private User getUser(Long id, String userId, String password, String name, String email) {
        return new User(id, new UserId(userId), new Password(password), new Name(name), new Email(email));
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}
