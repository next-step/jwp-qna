package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Test
    void save() {
        final User javajigi = users.save(UserTest.JAVAJIGI);
        final Question expected = QuestionTest.Q1.writeBy(javajigi);
        final Question actual = questions.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    void findByContentsContainingTest() {
        final User javajigi = users.save(UserTest.JAVAJIGI);
        final String expected = questions.save(QuestionTest.Q1.writeBy(javajigi)).getContents();
        ;

        final String actual = questions.findByContentsContaining(expected).getContents();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void test() {
    }
}
