package qna.domain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.entity.Answer;
import qna.domain.entity.Question;
import qna.domain.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
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
        User user = users.save(new User("diqksrk", "diqksrk", "강민준", "diqksrk123@naver.com"));
        Question question = questions.save(new Question("타이틀", "콘텐츠"));

        answers.save(new Answer(user, question, "콘텐츠"));
    }

    @Test
    @DisplayName("answer테이블 save 테스트")
    void save() {
        User user = new User("diqksrk123", "diqksrk123", "강민준", "diqksrk123@naver.com");
        Question question = new Question("타이틀", "콘텐츠2");

        Answer expected = new Answer(user, question, "콘텐츠2");
        Answer actual = answers.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("answer테이블 select 테스트")
    void findById() {
        Answer expected = answers.findByContents("콘텐츠").get();

        assertThat(expected.getContents()).isEqualTo("콘텐츠");
    }

    @Test
    @DisplayName("answer테이블 update 테스트")
    void updateDeletedById() {
        Answer expected = answers.findByContents("콘텐츠")
                .get();
        expected.setContents("콘텐츠2");
        Answer actual = answers.findByContents("콘텐츠2")
                .get();

        assertThat(actual.getContents()).isEqualTo(expected.getContents());
    }

    @Test
    @DisplayName("answer테이블 delete 테스트")
    void delete() {
        Answer expected = answers.findByContents("콘텐츠")
                .get();
        answers.delete(expected);

        assertThat(answers.findByContents("콘텐츠").isPresent()).isFalse();
    }

    @Test
    @DisplayName("answer연관관계 매핑 테스트( user )")
    void getUserTest() {
        Answer answer = answers.findByContents("콘텐츠").get();
        User actual = users.save(new User("diqksrk123", "diqksrk123", "강민준", "diqksrk123@naver.com"));


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
        Answer answer = answers.findByContents("콘텐츠").get();
        Question actual = questions.save(new Question("타이틀", "콘텐츠"));

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
}