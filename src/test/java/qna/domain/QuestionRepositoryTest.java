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

    @Test
    @DisplayName("Question 저장 테스트")
    void save_question_test() {
        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));

        assertAll(
                () -> assertThat(question1.getId()).isNotNull(),
                () -> assertThat(question1.getTitle()).isEqualTo(QuestionTest.Q1.getTitle())
        );
    }

    @Test
    @DisplayName("Question 생성 및 저장 테스트")
    void create_new_question_and_save_test() {
        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "사과의 색깔은?", "아래 항목에서 알맞은 사과의 색을 고르시오.\n 1. 파랑 2. 빨강").writeBy(user1));

        assertAll(
                () -> assertThat(question1.getId()).isNotNull(),
                () -> assertThat(question1.getTitle()).isEqualTo("사과의 색깔은?")
        );
    }

    @Test
    @DisplayName("Question ID로 조회 테스트")
    void find_by_id_and_deleted_false_test() {

        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));

        final Optional<Question> searchResult = questions.findByIdAndDeletedFalse(question1.getId());

        assertAll(
                () -> assertThat(searchResult).isPresent(),
                () -> assertThat(searchResult.get().getId()).isEqualTo(question1.getId())
        );
    }

    @Test
    @DisplayName("Question ID로 삭제된 항목 조회 테스트")
    void find_by_id_and_deleted_true_test() {

        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));

        questions.delete(question1);

        Optional<Question> searchResult = questions.findByIdAndDeletedFalse(question1.getId());

        assertThat(searchResult).isNotPresent();
    }

    @Test
    @DisplayName("삭제되지 않은 Question 조회 테스트")
    void find_by_deleted_false_test() {

        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));

        assertThat(questions.findByDeletedFalse()).hasSize(1);
    }

    @Test
    @DisplayName("답변 조회 테스트")
    void get_answers_test() {
        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));

        question1.addAnswer(answers.save(new Answer(user1, question1, "test1")));
        question1.addAnswer(answers.save(new Answer(user1, question1, "test2")));

        assertThat(question1.getAnswers().getSize()).isEqualTo(2);

    }

    @Test
    @DisplayName("로그인 유저와 question 작성자 일치 확인 테스트")
    void check_question_writer_test() {
        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));

        assertThatThrownBy(
                () -> question1.checkWriter(UserTest.SANJIGI)
        ).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("답변이 없는 경우 질문 정상 삭제 테스트")
    void delete_question_with_empty_answer_test() throws CannotDeleteException {
        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));

        question1.delete(user1);

        assertThat(question1.isDeleted()).isTrue();
    }
}
