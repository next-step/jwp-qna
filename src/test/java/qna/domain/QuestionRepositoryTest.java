package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    UserRepository users;
    @Autowired
    QuestionRepository questions;

    @Autowired
    AnswerRepository answers;

    @BeforeEach
    void save() {
        users.save(UserTest.JAVAJIGI);
        questions.save(QuestionTest.Q1);
    }

    @Test
    @DisplayName("Question 저장 테스트")
    void save_question_test() {

        final Question question1 = questions.findByIdAndDeletedFalse(QuestionTest.Q1.getId()).get();

        assertAll(
                () -> assertThat(question1.getId()).isNotNull(),
                () -> assertThat(question1.getTitle()).isEqualTo(QuestionTest.Q1.getTitle())
        );
    }

    @Test
    @DisplayName("Question 생성 및 저장 테스트")
    void create_new_question_and_save_test() {
        final Question question1 = new Question("사과의 색깔은?", "아래 항목에서 알맞은 사과의 색을 고르시오.\n 1. 파랑 2. 빨강");
        final Question savedQuestion = questions.save(question1);

        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getTitle()).isEqualTo("사과의 색깔은?")
        );
    }

    @Test
    @DisplayName("Question ID로 조회 테스트")
    void find_by_id_and_deleted_false_test() {

        Optional<Question> searchResult = questions.findByIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertAll(
                () -> assertThat(searchResult).isPresent(),
                () -> assertThat(searchResult.get().getId()).isEqualTo(QuestionTest.Q1.getId())
        );
    }

    @Test
    @DisplayName("Question ID로 삭제된 항목 조회 테스트")
    void find_by_id_and_deleted_true_test() {

        final Question question1 = questions.findByIdAndDeletedFalse(QuestionTest.Q1.getId()).get();
        questions.delete(question1);

        Optional<Question> searchResult = questions.findByIdAndDeletedFalse(question1.getId());

        assertThat(searchResult).isNotPresent();
    }

    @Test
    @DisplayName("삭제되지 않은 Question 조회 테스트")
    void find_by_deleted_false_test() {

        assertThat(questions.findByDeletedFalse()).hasSize(1);
    }

    @Test
    @DisplayName("답변 조회 테스트")
    void get_answers_test() {

        Question question = questions.findByIdAndDeletedFalse(QuestionTest.Q1.getId()).get();
        question.addAnswer(answers.save(AnswerTest.A1));
        question.addAnswer(answers.save(AnswerTest.A2.writeBy(UserTest.JAVAJIGI)));

        assertThat(question.getAnswers().getSize()).isEqualTo(2);

    }

    @Test
    @DisplayName("로그인 유저와 question 작성자 일치 확인 테스트")
    void check_question_writer_test() {
        Question question = questions.findByIdAndDeletedFalse(QuestionTest.Q1.getId()).get();

        assertThatThrownBy(
                () -> question.checkWriter(UserTest.SANJIGI)
        ).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("답변이 없는 경우 질문 정상 삭제 테스트")
    void delete_question_with_empty_answer_test() throws CannotDeleteException {
        Question question = questions.findByIdAndDeletedFalse(QuestionTest.Q1.getId()).get();

        question.delete(UserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();
    }
}
