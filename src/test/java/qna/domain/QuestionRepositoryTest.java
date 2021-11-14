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

    @Test
    void save() {
        final Question expected = QuestionTest.Q1;
        final Question actual = questions.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    void findByContentsContainingTest() {
        String expected = "contents1";
        questions.save(new Question("title1", expected).writeBy(UserTest.JAVAJIGI));
        String actual = questions.findByContentsContaining(expected).getContents();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void test() {
    }
}
