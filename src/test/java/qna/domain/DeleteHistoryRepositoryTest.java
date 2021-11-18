package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @Autowired
    private UserRepository users;

    @Test
    void save() {
        final User javajigi = users.save(JAVAJIGI);
        final DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, javajigi, LocalDateTime.now());
        expected.setDeletedBy(javajigi);
        final DeleteHistory actual = deleteHistories.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContentId()).isEqualTo(expected.getContentId())
        );
    }

    @Test
    void findByContentsContainingTest() {
        final Long expected = 1L;
        deleteHistories.save(new DeleteHistory(ContentType.QUESTION, expected, users.save(JAVAJIGI), LocalDateTime.now()));
        final Long actual = deleteHistories.findByContentId(expected).getContentId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void test() {
    }

}