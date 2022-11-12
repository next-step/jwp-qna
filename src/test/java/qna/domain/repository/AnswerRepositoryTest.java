package qna.domain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.entity.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private AnswerRepository answers;

    @BeforeEach
    void setUp() {
        User savedUser = users.save(UserTest.JAVAJIGI);
        Question savedQuestion = questions.save(QuestionTest.Q1.writeBy(savedUser));

        Answer savedAnswer = AnswerTest.A1;
        savedAnswer.setUser(savedUser);
        savedAnswer.setQuestion(savedQuestion);
        answers.save(savedAnswer);
    }

    @Test
    @DisplayName("answer테이블 save 테스트")
    void save() {
        User savedUser = users.save(UserTest.SANJIGI);
        Question savedQuestion = questions.save(QuestionTest.Q2.writeBy(savedUser));

        Answer expected = AnswerTest.A2;
        expected.setUser(savedUser);
        expected.setQuestion(savedQuestion);
        Answer actual = answers.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("answer테이블 select 테스트")
    void findById() {
        Answer expected = answers.findByContents("Answers Contents1").get();

        assertThat(expected.getContents()).isEqualTo("Answers Contents1");
    }

    @Test
    @DisplayName("answer테이블 update 테스트")
    void updateDeletedById() {
        Answer expected = answers.findByContents("Answers Contents1")
                .get();
        expected.setContents("Answers Contents2");
        Answer actual = answers.findByContents("Answers Contents2")
                .get();

        assertThat(actual.getContents()).isEqualTo(expected.getContents());
    }

//    @Test
//    @DisplayName("answer테이블 delete 테스트")
//    void delete() {
//        Answer expected = answers.findByContents("Answers Contents1")
//                .get();
//        answers.delete(expected);
//
//        assertThat(answers.findByContents("Answers Contents1").isPresent()).isFalse();
//    }

    @Test
    @DisplayName("answer연관관계 매핑 테스트( user )")
    void getUserTest() {
        Answer answer = answers.findByContents("Answers Contents1").get();
        User actual = users.save(UserTest.SANJIGI);


        Answer savedAnswer = saveUserInfo(answer, actual);
        User expected = savedAnswer.getUser();

        assertAll(
                () -> assertThat(expected.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    private Answer saveUserInfo(Answer answer, User actual) {
        answer.setUser(actual);
        Answer savedDeleteHistory = answers.save(answer);
        answers.flush();
        return savedDeleteHistory;
    }

    @Test
    @DisplayName("answer연관관계 매핑 테스트( question )")
    void getQuestionTest() {
        Answer answer = answers.findByContents("Answers Contents1").get();
        User savedUser = users.save(UserTest.SANJIGI);
        Question question = QuestionTest.Q2;
        question.setUser(savedUser);

        Question actual = questions.save(QuestionTest.Q2);

        Answer savedAnswer = saveQuestionInfo(answer, actual);
        Question expected = savedAnswer.getQuestion();

        assertAll(
                () -> assertThat(expected.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle())
        );
    }

    private Answer saveQuestionInfo(Answer answer, Question actual) {
        answer.setQuestion(actual);
        Answer savedDeleteHistory = answers.save(answer);
        answers.flush();
        return savedDeleteHistory;
    }

    @Test
    @DisplayName("toString 테스트")
    void toStringTest() {
        Answer answer = answers.findByContents("Answers Contents1")
                .get();

        assertThatNoException().isThrownBy(() -> answer.toString());
    }
}