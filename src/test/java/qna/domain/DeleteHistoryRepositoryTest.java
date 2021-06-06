package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private UserRepository users;

    @Autowired
    private DeleteHistoryRepository deleteHistories;

    private User u1;

    private Question q1;

    @BeforeEach
    void setUp() {
        u1 = new User("seungyeol", "password", "name", "beck33333@naver.com");
        q1 = new Question("제목 이에요", "본문 입니다.");
        users.save(u1);
    }

    @Test
    void save() {

        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, u1, now());
        DeleteHistory actual = deleteHistories.save(expected);

        assertThat(expected).isEqualTo(actual);
        assertThat(actual.getUser()).isEqualTo(u1);
    }

    @Test
    void update() {

        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, u1, now());
        DeleteHistory actual = deleteHistories.save(expected);

    }


}
