package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("답변 엔티티")
public class AnswerTest extends JpaSliceTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    private User writer;
    private Question question;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        writer = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        question = questionRepository.save(new Question("질문", "질문내용"));
    }

    @DisplayName("저장하면 DB가 생성한 아이디가 있다.")
    @Test
    void save() {
        final Answer newAnswer = new Answer(writer, question, "답변입니다.");
        final Answer savedAnswer = answerRepository.save(newAnswer);

        assertThat(savedAnswer.getId()).isNotNull();
    }

    @DisplayName("저장하면 저장한 일시가 생성된다.")
    @Test
    void createdAt() {
        final Answer newAnswer = new Answer(writer, question, "답변입니다.");
        final Answer savedAnswer = answerRepository.save(newAnswer);

        assertAll(
                () -> assertThat(savedAnswer.getCreatedAt()).isNotNull(),
                () -> assertThat(savedAnswer.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("답변이 수정되면, 수정일시가 변경된다.")
    @Test
    void updatedDateTime() {
        final Answer newAnswer = answerRepository.save(new Answer(writer, question, "성의X"));
        final LocalDateTime firstUpdatedAt = newAnswer.getUpdatedAt();

        newAnswer.setContents("제대로 된 답변입니다.");
        final Answer updatedAnswer = answerRepository.saveAndFlush(newAnswer);

        assertThat(updatedAnswer.getUpdatedAt()).isNotEqualTo(firstUpdatedAt);
    }

    @DisplayName("답변의 작성자는 생성할 때의 유저이다.")
    @Test
    void writer() {
        final Answer answer = answerRepository.save(new Answer(writer, question, "내용"));

        assertThat(answer.getWriter()).isEqualTo(writer);
    }

    @DisplayName("답변의 작성자를 알 수 있다.")
    @Test
    void isOwner() {
        final User other = userRepository.save(new User("sanjigi", "password", "name", "sanjigi@slipp.net"));
        final Answer answer = answerRepository.save(new Answer(writer, QuestionTest.Q1, "내용"));

        assertAll(
                () -> assertThat(answer.isOwner(writer)).isTrue(),
                () -> assertThat(answer.isOwner(other)).isFalse()
        );
    }
}
