package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private Question question;

    @BeforeEach
    void setUp() {
        user = new User("mwkwon", "password", "권민욱", "mwkwon0110@gmail.com");
        question = new Question("title", "content");
    }

    @Test
    @DisplayName("답변 테이블 저장")
    void save() {
        Answer expected = new Answer(user, question, "Answers Contents1");
        Answer actual = answers.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter())
        );
    }

    @Test
    @DisplayName("아이디 기준 데이터 정상 조회")
    void findById() {
        Answer expected = setUpTestAnswer();

        Optional<Answer> actual = answers.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getId()).isEqualTo(expected.getId());
    }

    @Test
    @DisplayName("연관 관계로 맵핑되어 있는 user 정보 데이터가 저장시 사용한 user 정보와 같은지 확인")
    void findById_equal_user() {
        Answer expected = setUpTestAnswer();

        Optional<Answer> actual = answers.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getWriter().id()).isEqualTo(user.id());
    }

    @Test
    @DisplayName("연관 관계로 맵핑되어 있는 question 정보 데이터가 저장시 사용한 question 정보와 같은지 확인")
    void findById_equal_question() {
        Answer expected = setUpTestAnswer();

        Optional<Answer> actual = answers.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getQuestion().getId()).isEqualTo(question.getId());
    }

    @Test
    @DisplayName("답변 테이블 수정")
    void update() {
        Answer expected = answers.save(new Answer(user, question, "Answers Contents1"));
        expected.delete();
        entityManager.flush();
        entityManager.clear();
        Optional<Answer> actual = answers.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
    }

    @Test
    @DisplayName("답변 테이블 삭제")
    void delete() {
        Answer expected = answers.save(new Answer(user, question, "Answers Contents1"));
        answers.delete(expected);
        entityManager.flush();
        entityManager.clear();
        Optional<Answer> actual = answers.findById(expected.getId());
        assertThat(actual.isPresent()).isFalse();
    }

    private Answer setUpTestAnswer() {
        Answer answer = new Answer(user, question, "Answers Contents1");
        Answer expected = answers.save(answer);
        entityManager.flush();
        entityManager.clear();
        return expected;
    }
}
