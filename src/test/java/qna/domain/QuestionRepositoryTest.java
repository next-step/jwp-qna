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
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private EntityManager entityManager;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("mwkwon", "password", "권민욱", "mwkwon0110@gmail.com");
    }

    @Test
    @DisplayName("질문 테이블 정상 저장")
    void save() {
        Question expected = new Question("title1", "contents").writeBy(user);
        Question actual = questions.save(expected);
        assertAll(
                () -> assertThat(actual.id()).isNotNull(),
                () -> assertThat(actual.writer()).isEqualTo(expected.writer()),
                () -> assertThat(user.id()).isNotNull(),
                () -> assertThat(actual.id()).isEqualTo(expected.id())
        );
    }

    @Test
    @DisplayName("질문 테이블 아이디 정상 조회")
    void findById() {
        Question expected = questions.save(new Question("title1", "contents").writeBy(user));
        Optional<Question> actual = questions.findById(expected.id());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().id()).isEqualTo(expected.id());
        assertThat(actual.get() == expected).isTrue();
    }

    @Test
    @DisplayName("question entity를 이용하여 답변 리스트 정상 조회")
    void findAnswersByQuestion() {
        Question expected = new Question("title1", "contents").writeBy(user);

        Answer answersContents1 = new Answer(user, expected, "Answers Contents1");
        Answer answersContents2 = new Answer(user, expected, "Answers Contents2");
        expected.addAnswer(answersContents1);
        expected.addAnswer(answersContents2);
        Question QuestionExpected = questions.save(expected);
        Optional<Question> actual = questions.findById(QuestionExpected.id());

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().isContainAnswer(answersContents1)).isTrue();
        assertThat(actual.get().isContainAnswer(answersContents2)).isTrue();
    }

    @Test
    @DisplayName("질문 테이블 정상 수정")
    void update() {
        Question expected = questions.save(new Question("title1", "contents").writeBy(user));
        expected.delete();
        entityManager.flush();
        entityManager.clear();
        Optional<Question> actual = questions.findById(expected.id());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문 테이블 정상 삭제")
    void delete() {
        Question expected = questions.save(new Question("title1", "contents").writeBy(user));
        questions.delete(expected);
        entityManager.flush();
        entityManager.clear();
        Optional<Question> actual = questions.findById(expected.id());
        assertThat(actual.isPresent()).isFalse();
    }
}
