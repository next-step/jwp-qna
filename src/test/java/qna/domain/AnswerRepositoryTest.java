package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Test
    void test() {
    }

    @Test
    void save() {
        final User javajigi = users.save(UserTest.JAVAJIGI);
        final Question q1 = questions.save(QuestionTest.Q1.writeBy(javajigi));
        final Answer expected = AnswerTest.A1;

        expected.setQuestion(q1);
        expected.setWriter(javajigi);
        final Answer actual = answers.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    void findByContentsContainingTest() {
        // given
        final User javajigi = users.save(UserTest.JAVAJIGI);
        final Question q1 = questions.save(QuestionTest.Q1.writeBy(javajigi));
        final Answer a1 = AnswerTest.A1;
        a1.setQuestion(q1);
        a1.setWriter(javajigi);

        final Answer expected = answers.save(a1);

        final String expectedSContents = a1.getContents();

        // when
        String actual = answers.findByContentsContaining(expectedSContents).getContents();

        // then
        assertThat(actual).isEqualTo(expected.getContents());
    }

    @Test
    void getQuestionTest() {
        // given
        final User javajigi = users.save(UserTest.JAVAJIGI);
        final Question q1 = questions.save(QuestionTest.Q1.writeBy(javajigi));
        AnswerTest.A1.setWriter(javajigi);
        AnswerTest.A1.setQuestion(q1);
        final Answer a1 = answers.save(AnswerTest.A1);

        // when
        Answer answer = answers.findByContentsContaining(a1.getContents());

        // then
        assertThat(answer.getQuestion()).isEqualTo(q1);
    }
}
