package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {

    private Question question1;
    private Question question2;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        question1 = new Question("title1", "contents1")
            .writeBy(UserTest.JAVAJIGI);
        question2 = new Question("title2", "contents2")
            .writeBy(UserTest.SANJIGI);
        questionRepository.save(question1);
        questionRepository.save(question2);
    }

    @AfterEach()
    void tearDown() {
        questionRepository.deleteAll();
    }

    @Test
    void test_질문_저장() {
        questionRepository.deleteAll();
        entityManager.clear();

        Question actual = questionRepository.save(question1);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(question1.getId()),
            () -> assertThat(actual.getTitle()).isEqualTo(question1.getTitle()),
            () -> assertThat(actual.getContents()).isEqualTo(question1.getContents()),
            () -> assertThat(actual.getWriterId()).isEqualTo(question1.getWriterId())
        );
    }

    @Test
    void test_목록_조회() {
        List<Question> questions = questionRepository.findAll();

        assertAll(
            () -> assertThat(questions).isNotNull(),
            () -> assertThat(questions).hasSize(2)
        );
    }

    @Test
    void test_질문_id로_조회() {
        Question actual = questionRepository.findById(question1.getId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(question1.getId())
        );
    }

    @Test
    void test_질문_제목으로_조회() {
        Question actual = questionRepository.findByTitle(question1.getTitle())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getTitle()).isEqualTo(question1.getTitle())
        );
    }

    @Test
    void test_질문_내용으로_조회() {
        Question actual = questionRepository.findByContents(question1.getContents())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(question1.getContents())
        );
    }

    @Test
    void test_질문_제목_수정() {
        question1.setTitle("제목수정");

        Question actual = questionRepository.findById(question1.getId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getTitle()).isEqualTo("제목수정")
        );
    }

    @Test
    void test_질문_내용_수정() {
        question1.setContents("질문내용수정");

        Question actual = questionRepository.findById(question1.getId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo("질문내용수정")
        );
    }

    @Test
    void test_질문_삭제() {
        question1.setDeleted(true);

        List<Question> questions = questionRepository.findAll();

        assertAll(
            () -> assertThat(questions).isNotNull(),
            () -> assertThat(questions).hasSize(2),
            () -> assertThat(questions.get(0).isDeleted()).isTrue(),
            () -> assertThat(questions.get(1).isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("삭제되지 않은 질문을 조회한다.")
    void test_findByDeletedFalse() {
        List<Question> questions = questionRepository.findByDeletedFalse();

        assertAll(
            () -> assertThat(questions).isNotNull(),
            () -> assertThat(questions).hasSize(2),
            () -> assertThat(questions.get(0).isDeleted()).isFalse(),
            () -> assertThat(questions.get(1).isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("질문 ID로 조회 후 삭제되지 않았음을 조회한다.")
    void test_findByIdAndDeletedFalse() {
        Question actual = questionRepository.findById(question1.getId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(question1.getId()),
            () -> assertThat(actual.isDeleted()).isEqualTo(question1.isDeleted()),
            () -> assertThat(actual.isDeleted()).isFalse()
        );
    }
}
