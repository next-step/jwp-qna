package qna.domain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.entity.Question;
import qna.domain.entity.QuestionTest;
import qna.domain.entity.User;
import qna.domain.entity.UserTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    @BeforeEach
    void setUp() {
        Question question = QuestionTest.Q1;

        questions.save(question);
    }

    @Test
    @DisplayName("question테이블 save 테스트")
    void save() {
        Question expected = QuestionTest.Q2;
        Question actual = questions.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("question테이블 select 테스트")
    void findById() {
        Question expected = questions.findByTitle("title1").get();
        Question actual = new Question("title1", "contents1");

        assertAll(
                () -> assertThat(expected.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("question테이블 update 테스트")
    void updateDeletedById() {
        Question expected = questions.findByTitle("title1")
                .get();
        expected.setTitle("바꾼타이틀");
        Question actual = questions.findByTitle("바꾼타이틀")
                .get();

        assertThat(actual.getTitle()).isEqualTo("바꾼타이틀");
    }

    @Test
    @DisplayName("question테이블 delete 테스트")
    void delete() {
        Question expected = questions.findByTitle("title1")
                .get();
        questions.delete(expected);

        assertThat(questions.findByTitle("title1").isPresent()).isFalse();
    }

    @Test
    @DisplayName("user연관관계 매핑 테스트")
    void getUserTest() {
        User actual = users.save(UserTest.JAVAJIGI);
        Question question = questions.findByTitle("title1")
                .get();

        Question savedQuestion = saveQuestionInfo(actual, question);
        User expected = savedQuestion.getUser();

        assertAll(
                () -> assertThat(expected.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    private Question saveQuestionInfo(User actual, Question question) {
        question.setUser(actual);
        Question savedQuestion = questions.save(question);
        questions.flush();
        return savedQuestion;
    }

    @Test
    @DisplayName("toString 테스트")
    void toStringTest() {
        Question question = questions.findByTitle("title1")
                .get();

        assertThatNoException().isThrownBy(() -> question.toString());
    }
}