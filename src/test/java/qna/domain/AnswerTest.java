package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("답변 엔티티")
public class AnswerTest extends JpaSliceTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("저장하면 DB가 생성한 아이디가 있다.")
    @Test
    void save() {
        final Answer newAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "답변입니다.");
        final Answer savedAnswer = answerRepository.save(newAnswer);

        assertThat(savedAnswer.getId()).isNotNull();
    }

    @DisplayName("저장하면 저장한 일시가 생성된다.")
    @Test
    void createdAt() {
        final Answer newAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "답변입니다.");
        final Answer savedAnswer = answerRepository.save(newAnswer);

        assertAll(
                () -> assertThat(savedAnswer.getCreatedAt()).isNotNull(),
                () -> assertThat(savedAnswer.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("답변이 수정되면, 수정일시가 변경된다.")
    @Test
    void updatedDateTime() {
        final Answer newAnswer = answerRepository.save(
                new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "성의X")
        );
        final LocalDateTime firstUpdatedAt = newAnswer.getUpdatedAt();

        newAnswer.setContents("제대로 된 답변입니다.");
        final Answer updatedAnswer = answerRepository.saveAndFlush(newAnswer);

        assertThat(updatedAnswer.getUpdatedAt()).isNotEqualTo(firstUpdatedAt);
    }
}
