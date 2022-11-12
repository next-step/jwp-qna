package qna.domain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private AnswerRepository answers;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        User savedUser = users.save(UserTest.JAVAJIGI);

        Question question = QuestionTest.Q1;
        question.setUser(savedUser);
        Question savedQuestion = questions.save(question);

        Answer savedAnswer = AnswerTest.A1;
        savedAnswer.setUser(savedUser);
        savedAnswer.setQuestion(savedQuestion);
        answers.save(savedAnswer);

        answers.flush();
        users.flush();
        questions.flush();
    }

    @Test
    @DisplayName("question테이블 save 테스트")
    void save() {
        User savedUser = users.save(UserTest.SANJIGI);
        Question expected = QuestionTest.Q2;
        expected.setUser(savedUser);
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

        answers.delete(AnswerTest.A1);
        entityManager.flush();
        questions.delete(expected);

        assertThat(questions.findByTitle("title1").isPresent()).isFalse();
    }

    @Test
    @DisplayName("user연관관계 매핑 테스트")
    void getUserTest() {
        User actual = users.save(UserTest.SANJIGI);
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

    @Test
    @DisplayName("answer 연관관계 테스트")
    void answer_relation_Test() {
        entityManager.clear();

        Question expected = questions.findByTitle("title1")
                .get();

        assertThat(expected.getAnswers().size()).isEqualTo(1);
    }
}