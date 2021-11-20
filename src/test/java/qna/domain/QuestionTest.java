package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

@DataJpaTest
public class QuestionTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EntityManager entityManager;

    private User user1;
    private User user2;

    private Question question1;
    private Question question2;

    @BeforeEach
    void setUp() {
        user1 = new User(UserId.from("user1"), Password.from("password"), Name.from("alice"),
            Email.from("alice@gmail.com"));
        user2 = new User(UserId.from("user2"), Password.from("password"), Name.from("bob"),
            Email.from("bob@gmail.com"));

        userRepository.save(user1);
        userRepository.save(user2);

        question1 = new Question("title1", "contents1")
            .writeBy(user1);
        question2 = new Question("title2", "contents2")
            .writeBy(user2);

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
        userRepository.deleteAll();
        entityManager.clear();

        Question actual = questionRepository.save(question1);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(question1.getId()),
            () -> assertThat(actual.getTitle()).isEqualTo(question1.getTitle()),
            () -> assertThat(actual.getContents()).isEqualTo(question1.getContents()),
            () -> assertThat(actual.getWriter().getId()).isEqualTo(question1.getWriter().getId())
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
        Contents contents = Contents.from("질문내용수정");
        question1.setContents(contents);

        Question actual = questionRepository.findById(question1.getId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getContents().contents()).isEqualTo("질문내용수정")
        );
    }

    @Test
    void test_동일_작성자_질문_삭제() throws CannotDeleteException {
        DeleteHistories deleteHistories = question1.delete(question1.getWriter());

        assertAll(
            () -> assertThat(question1.isDeleted()).isTrue(),
            () -> assertThat(deleteHistories.size()).isGreaterThanOrEqualTo(1),
            () -> assertThat(deleteHistories.deleteHistories().get(0).getContentId()).isEqualTo(
                question1.getId())
        );
    }

    @Test
    void test_다른_작성자_질문_삭제시도시_예외() {
        User anotherWriter = question2.getWriter();

        assertAll(
            () -> assertThat(question1.getWriter()).isNotEqualTo(anotherWriter),
            () -> assertThatThrownBy(() -> question1.delete(anotherWriter))
                .isInstanceOf(CannotDeleteException.class),
            () -> assertThat(question1.isDeleted()).isFalse()
        );
    }

    @Test
    void test_답변이_없으면_삭제_가능() {
        Question question = new Question("title1", "contents1")
            .writeBy(user1);

        assertAll(
            () -> assertThat(question.getAnswers().size()).isEqualTo(0),
            () -> assertThat(question.delete(user1)).isNotNull()
        );
    }

    @Test
    void test_질문_작성자_모든_답변_작성자_동일_할_때_삭제_가능() {
        Question question = new Question("title1", "contents1")
            .writeBy(user1);

        Answer answer1 = new Answer(user1, question, "답변1");
        Answer answer2 = new Answer(user1, question, "답변2");

        question.addAnswer(answer1);
        question.addAnswer(answer2);

        assertAll(
            () -> assertThat(question.getAnswers().size()).isEqualTo(2),
            () -> assertThat(question.delete(user1)).isNotNull(),
            () -> assertThat(question.isDeleted()).isTrue(),
            () -> assertThat(question.getAnswers().answers().stream()
                .filter(Answer::isDeleted).count()).isEqualTo(2)
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
