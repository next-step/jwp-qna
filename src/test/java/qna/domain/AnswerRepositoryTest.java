package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answers;

    @Autowired
    UserRepository users;

    @Autowired
    QuestionRepository questions;

    @Test
    @DisplayName("Answer 저장 테스트")
    void save_user_test() {
        final Answer A1 = AnswerTest.A1;
        final Answer savedAnswer = answers.save(A1);
        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(A1.getContents())
        );
    }

    @Test
    @DisplayName("Answer 생성 및 저장 테스트")
    void create_new_user_and_save_test() {
        final Answer A1 = new Answer(UserTest.SANJIGI, QuestionTest.Q2, "TEST");
        final Answer savedAnswer = answers.save(A1);
        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                ()-> assertThat(savedAnswer.getContents()).isEqualTo("TEST")
        );
    }

    @Test
    @DisplayName("QuestionId로 Answer 조회 테스트")
    void find_answer_by_question_id_test() {
        final User writer = users.save(UserTest.SANJIGI);
        final Question q1 = questions.save(QuestionTest.Q1);
        final Question q2 = questions.save(QuestionTest.Q2);
        final Answer A1 = answers.save(new Answer(writer, q1, "TEST1"));
        final Answer A2 = answers.save(new Answer(writer, q1, "TEST2"));
        final Answer A3 = answers.save(new Answer(writer, q2, "TEST3"));
        final List<Answer> list = answers.findByQuestionIdAndDeletedFalse(q1.getId());
        assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 조회 테스트")
    void find_not_deleted_answer_test() {
        final User writer = users.save(UserTest.SANJIGI);
        final Question q1 = questions.save(QuestionTest.Q1);
        final Question q2 = questions.save(QuestionTest.Q2);
        final Answer A1 = answers.save(new Answer(writer, q1, "TEST1"));
        final Answer A2 = answers.save(new Answer(writer, q2, "TEST2"));
        answers.delete(A2);
        final Optional<Answer> searchResult = answers.findByIdAndDeletedFalse(A1.getId());
        assertThat(searchResult.isPresent()).isTrue();
    }

    @Test
    @DisplayName("삭제된 Answer 조회 테스트")
    void find_deleted_answer_test() {
        final User writer = users.save(UserTest.SANJIGI);
        final Question q1 = questions.save(QuestionTest.Q1);
        final Question q2 = questions.save(QuestionTest.Q2);
        final Answer A1 = answers.save(new Answer(writer, q1, "TEST1"));
        final Answer A2 = answers.save(new Answer(writer, q2, "TEST2"));
        answers.delete(A2);
        final Optional<Answer> searchResult = answers.findByIdAndDeletedFalse(A2.getId());
        assertThat(searchResult.isPresent()).isFalse();
    }

}
