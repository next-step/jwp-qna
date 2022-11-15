package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @DisplayName("Answer 생성 및 저장 테스트")
    void create_new_user_and_save_test() {

        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));
        final Answer answer1 = answers.save(new Answer(1L, user1, question1, "Answer1"));

        assertAll(
                () -> assertThat(answer1.getId()).isNotNull()
        );
    }

    @Test
    @DisplayName("QuestionId로 Answer 조회 테스트")
    void find_answer_by_question_id_test() {

        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));

        final Answer answer1 = answers.save(new Answer(user1, question1, "TEST1"));
        final Answer answer2 = answers.save(new Answer(user1, question1, "TEST2"));

        final List<Answer> list = answers.findByQuestionIdAndDeletedFalse(question1.getId());

        assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 조회 테스트")
    void find_not_deleted_answer_test() {

        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));

        final Answer answer1 = answers.save(new Answer(user1, question1, "TEST1"));
        final Answer answer2 = answers.save(new Answer(user1, question1, "TEST2"));
        answers.delete(answer2);

        final Optional<Answer> searchResult = answers.findByIdAndDeletedFalse(answer1.getId());

        assertThat(searchResult).isPresent();
    }

    @Test
    @DisplayName("삭제된 Answer 조회 테스트")
    void find_deleted_answer_test() {

        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));

        final Answer answer1 = answers.save(new Answer(user1, question1, "TEST1"));
        final Answer answer2 = answers.save(new Answer(user1, question1, "TEST2"));
        answers.delete(answer2);

        final Optional<Answer> searchResult = answers.findByIdAndDeletedFalse(answer2.getId());

        assertThat(searchResult).isNotPresent();
    }

    @Test
    @DisplayName("유효한 Question 조회")
    void get_valid_Question_test() {

        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));
        final Answer answer1 = answers.save(new Answer(user1, question1, "TEST1"));

        final Question question = questions.findByIdAndDeletedFalse(answer1.getQuestion().getId()).get();

        assertThat(question).isEqualTo(answer1.getQuestion());
    }

    @Test
    @DisplayName("유효한 Writer 조회")
    void get_valid_Writer_test() {

        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));
        final Answer answer1 = answers.save(new Answer(user1, question1, "TEST1"));

        final User writer = users.findByUserId(answer1.getWriter().getUserId()).get();

        assertThat(writer).isEqualTo(answer1.getWriter());
    }

    @Test
    @DisplayName("Answer 삭제 시 상태 값 정상 변경 테스트")
    void delete_answer_test() {
        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));
        final Answer answer1 = answers.save(new Answer(user1, question1, "TEST1"));

        answer1.delete(user1);

        assertThat(answer1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Answer 삭제 시 상태 값 정상 변경 테스트")
    void delete_answer_with_invalid_user_test() {
        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));
        final Answer answer1 = answers.save(new Answer(user1, question1, "TEST1"));

        assertThatThrownBy(
                () -> answer1.delete(UserTest.SANJIGI)
        ).isInstanceOf(CannotDeleteException.class);

    }
}
