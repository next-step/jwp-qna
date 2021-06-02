package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private Question question;

    @BeforeEach
    void setUp() {
        user = new User("mwkwon", "password", "권민욱", "mwkwon0110@gmail.com");
        question = new Question("title", "content");
        users.save(user);
        questions.save(question);
    }
    @Test
    @DisplayName("답변 테이블 정상 저장 테스트")
    void save() {
        Answer expected = new Answer(user, question, "Answers Contents1");
        Answer actual = answers.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter())
        );
    }

    @Test
    @DisplayName("아이디 기준 데이터 정상 조회 테스트")
    void findById() {
        Answer answer = new Answer(user, question, "Answers Contents1");
        Answer expected = answers.save(answer);
        entityManager.flush();
        entityManager.clear();

        Optional<Answer> actual = answers.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getId()).isEqualTo(expected.getId());
    }

    @Test
    @DisplayName("연관 관계로 맵핑되어 있는 user 정보 데이터가 저장시 사용한 user 정보와 같은지 확인")
    void findById_equal_user() {
        Answer answer = new Answer(user, question, "Answers Contents1");
        Answer expected = answers.save(answer);
        entityManager.flush();
        entityManager.clear();

        Optional<Answer> actual = answers.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getWriter().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("연관 관계로 맵핑되어 있는 question 정보 데이터가 저장시 사용한 question 정보와 같은지 확인")
    void findById_equal_question() {
        Answer answer = new Answer(user, question, "Answers Contents1");
        Answer expected = answers.save(answer);
        entityManager.flush();
        entityManager.clear();

        Optional<Answer> actual = answers.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getQuestion().getId()).isEqualTo(question.getId());
    }

    @Test
    void update() {
        Answer expected = answers.save(new Answer(user, question, "Answers Contents1"));
        expected.delete(true);
        entityManager.flush();
        entityManager.clear();
        Optional<Answer> actual = answers.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().isDeleted()).isTrue();
    }

    @Test
    void delete() {
        Answer expected = answers.save(new Answer(user, question, "Answers Contents1"));
        answers.delete(expected);
        entityManager.flush();
        entityManager.clear();
        Optional<Answer> actual = answers.findById(expected.getId());
        assertThat(actual.isPresent()).isFalse();
    }
}
