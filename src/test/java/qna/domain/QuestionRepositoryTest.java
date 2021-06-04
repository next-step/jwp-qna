package qna.domain;

import org.junit.jupiter.api.AfterEach;
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
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(expected.getId())
        );
    }

    @Test
    @DisplayName("질문 테이블 아이디 정상 조회")
    void findById() {
        Question expected = questions.save(new Question("title1", "contents").writeBy(user));
        Optional<Question> actual = questions.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getId()).isEqualTo(expected.getId());
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
        Optional<Question> actual = questions.findById(QuestionExpected.getId());

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().isContainAnswer(answersContents1)).isTrue();
        assertThat(actual.get().isContainAnswer(answersContents2)).isTrue();
        assertThat(actual.get().isSameByAnswersSize(2)).isTrue();

    }

    @Test
    @DisplayName("질문 테이블 정상 수정")
    void update() {
        Question expected = questions.save(new Question("title1", "contents").writeBy(user));
        expected.delete(true);
        entityManager.flush();
        entityManager.clear();
        Optional<Question> actual = questions.findById(expected.getId());
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
        Optional<Question> actual = questions.findById(expected.getId());
        assertThat(actual.isPresent()).isFalse();
    }
}
