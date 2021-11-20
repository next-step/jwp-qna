package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

@DataJpaTest
public class AnswerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private User user1;
    private User user2;

    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        user1 = new User(UserId.from("user1"), Password.from("password"), Name.from("alice"),
            Email.from("alice@gmail.com"));
        user2 = new User(UserId.from("user2"), Password.from("password"), Name.from("bob"),
            Email.from("bob@gmail.com"));

        userRepository.save(user1);
        userRepository.save(user2);

        Question question = new Question("title1", "contents1")
            .writeBy(user1);

        questionRepository.save(question);

        answer1 = new Answer(user1, question, "Answers Contents1");
        answer2 = new Answer(user2, question, "Answers Contents2");

        answerRepository.save(answer1);
        answerRepository.save(answer2);
    }

    @AfterEach
    void tearDown() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void test_답변_저장() {
        Answer actual = answerRepository.save(answer1);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getWriter().getId()).isEqualTo(answer1.getWriter().getId()),
            () -> assertThat(actual.getQuestion().getId()).isEqualTo(answer1.getQuestion().getId())
        );
    }

    @Test
    void test_목록_조회() {
        List<Answer> answers = answerRepository.findAll();

        assertAll(
            () -> assertThat(answers).isNotNull(),
            () -> assertThat(answers).hasSize(2)
        );
    }

    @Test
    void test_답변_id로_조회() {
        Answer actual = answerRepository.findById(answer1.getId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(answer1.getId())
        );
    }

    @Test
    void test_질문_id로_조회() {
        List<Answer> answers = answerRepository.findByQuestionId(answer1.getQuestion().getId());

        assertAll(
            () -> assertThat(answers).isNotNull(),
            () -> assertThat(answers).hasSize(2),
            () -> assertThat(answers.get(0).getQuestion().getId()).isEqualTo(
                answer1.getQuestion().getId())
        );
    }

    @Test
    void test_작성자_id로_조회() {
        Answer actual = answerRepository.findByWriterId(answer1.getWriter().getId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getWriter().getId()).isEqualTo(answer1.getWriter().getId())
        );
    }

    @Test
    void test_답변_업데이트() {
        Contents contents = Contents.from("답변수정");
        answer1.setContents(contents);

        Answer actual = answerRepository.findById(answer1.getId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getContents().contents()).isEqualTo("답변수정")
        );
    }

    @Test
    void test_동일_작성자_답변_삭제() throws CannotDeleteException {
        DeleteHistory deleteHistory = answer1.delete(answer1.getWriter());

        assertAll(
            () -> assertThat(answer1.isDeleted()).isTrue(),
            () -> assertThat(deleteHistory.getContentId()).isEqualTo(answer1.getId())
        );
    }

    @Test
    void test_다른_작성자_답변_삭제시도시_예외() {
        User anotherWriter = answer2.getWriter();

        assertAll(
            () -> assertThat(answer1.getWriter()).isNotEqualTo(anotherWriter),
            () -> assertThatThrownBy(() -> answer1.delete(anotherWriter))
                .isInstanceOf(CannotDeleteException.class),
            () -> assertThat(answer1.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("ID로 조회 후 삭제 되지 않았음을 확인한다.")
    void test_findByIdAndDeletedFalse() {
        Answer actual = answerRepository.findByIdAndDeletedFalse(answer1.getId())
            .orElse(null);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(answer1.getId()),
            () -> assertThat(actual.isDeleted()).isEqualTo(answer1.isDeleted()),
            () -> assertThat(actual.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("질문 ID로 조회 후 삭제 되지 않았음을 확인한다.")
    void test_findByQuestionIdAndDeletedFalse() {
        Long questionId = answer1.getQuestion().getId();

        List<Answer> answers =
            answerRepository.findByQuestionIdAndDeletedFalse(questionId);

        assertAll(
            () -> assertThat(answers).isNotNull(),
            () -> assertThat(answers.get(0).getQuestion().getId()).isEqualTo(
                answer1.getQuestion().getId()),
            () -> assertThat(answers.get(0).isDeleted()).isEqualTo(answer1.isDeleted()),
            () -> assertThat(answers.get(0).isDeleted()).isFalse()
        );
    }
}
