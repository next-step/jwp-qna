package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answers;

    @Test
    void instantiate() {
        assertThat(A1.getId()).isNull();
        assertThat(A1.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId());
        assertThat(A1.getQuestionId()).isEqualTo(QuestionTest.Q1.getId());
        assertThat(A1.getContents()).isEqualTo("Answers Contents1");
        assertThat(A1.isDeleted()).isFalse();
        assertThat(A1.getCreatedAt()).isNull();
        assertThat(A1.getUpdatedAt()).isNull();
    }

    @Test
    void save() {
        LocalDateTime now = LocalDateTime.now();
        final Answer actual = answers.save(A1);
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getWriterId()).isEqualTo(A1.getWriterId());
        assertThat(actual.getQuestionId()).isEqualTo(A1.getQuestionId());
        assertThat(actual.getContents()).isEqualTo(A1.getContents());
        assertThat(actual.isDeleted()).isEqualTo(A1.isDeleted());
        assertThat(actual.getCreatedAt()).isAfterOrEqualTo(now);
        assertThat(actual.getUpdatedAt()).isAfterOrEqualTo(now);
    }

    @Test
    void findByName() {
        final Answer expected = answers.save(A1);
        final Answer actual = answers.findById(expected.getId())
                .orElseThrow(RuntimeException::new);
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId());
    }

    @Test
    void update() {
        final LocalDateTime now = LocalDateTime.now();
        final Answer answer = answers.save(A1);
        String content = "Updated Content";

        answer.setContents(content);

        final Answer actual = answers.findById(answer.getId()).get();
        assertThat(actual.getContents()).isEqualTo(content);
        assertThat(actual.getUpdatedAt()).isAfterOrEqualTo(now);
    }

    @Test
    void delete() {
        final Answer answer = answers.save(A1);
        assertThat(answers.findById(answer.getId())).isNotNull();
        answers.deleteById(answer.getId());
        assertThat(answers.findById(answer.getId())).isEmpty();
    }

}
