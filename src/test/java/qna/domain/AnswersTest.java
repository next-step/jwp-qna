package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class AnswersTest {

    @Autowired
    UserRepository users;

    @Autowired
    QuestionRepository questions;

    @BeforeEach
    void save() {
        users.save(UserTest.JAVAJIGI);
        users.save(UserTest.SANJIGI);
        questions.save(QuestionTest.Q1);
    }

    @Test
    @DisplayName("Answers 사이즈 반환 테스트")
    void answer_size_test() {
        Question question = questions.findByIdAndDeletedFalse(UserTest.JAVAJIGI.getId()).get();
        question.addAnswer(AnswerTest.A1);
        question.addAnswer(AnswerTest.A2);

        assertThat(question.getAnswers().getSize()).isEqualTo(2);
    }

    @Test
    @DisplayName("빈 answers 확인 테스트")
    void isEmpty_test() {
        Question question = questions.findByIdAndDeletedFalse(UserTest.JAVAJIGI.getId()).get();

        assertThat(question.getAnswers().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Answer writer가 loginUser가 아닌 경우 테스트")
    void check_answer_writer_test() {
        Question question = questions.findByIdAndDeletedFalse(UserTest.JAVAJIGI.getId()).get();
        question.addAnswer(AnswerTest.A1);
        question.addAnswer(AnswerTest.A2);

        assertThatThrownBy(
                () -> question.getAnswers().isIdenticalWriter(UserTest.JAVAJIGI)
        ).isInstanceOf(CannotDeleteException.class);

    }
}
