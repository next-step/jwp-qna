package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Autowired
    private AnswerRepository answers;

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

        final String actual = questions.findByContentsContaining(expected).getContents();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void test() {
    }

    @Test
    @DisplayName("삭제시 작성자가 다르면 예외를 출력한다")
    void cannotDeleteExceptionTest() {
        final User javajigi = users.save(UserTest.JAVAJIGI);
        final Question expected = QuestionTest.Q1.writeBy(javajigi);
        final Question actual = questions.save(expected);

        assertThatThrownBy(() -> actual.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문을 삭제한다")
    void deleteTest() {
        final User javajigi = users.save(UserTest.JAVAJIGI);
        final Question q1 = questions.save(QuestionTest.Q1.writeBy(javajigi));
        // given
        q1.delete(javajigi);

        Question deleted = questions.save(q1);

        // then
        assertThat(q1.getId()).isEqualTo(deleted.getId());
        assertThat(q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("삭제시 작성자와 다른 답변자가 있으면 예외를 출력한다")
    void differentAnswerWriterTest() {
        final User javajigi = users.save(UserTest.JAVAJIGI);
        final User sanjigi = users.save(UserTest.SANJIGI);
        Question q1 = QuestionTest.Q1;
        q1.setWriter(javajigi);
        Question q2 = questions.save(q1);
        Answer a2 = AnswerTest.A2;
        a2.setWriter(sanjigi);
        q2.addAnswer(a2);
        questions.save(q2);

        // then
        assertThatThrownBy(() -> q2.delete(javajigi))
                .isInstanceOf(ForbiddenException.class);
    }
}
