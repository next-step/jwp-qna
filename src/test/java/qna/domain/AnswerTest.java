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
    void save() {
        final Answer actual = answers.save(A1);
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getWriterId()).isEqualTo(A1.getWriterId());
        assertThat(actual.getQuestionId()).isEqualTo(A1.getQuestionId());
        assertThat(actual.getContents()).isEqualTo(A1.getContents());
        assertThat(actual.isDeleted()).isEqualTo(A1.isDeleted());
        assertThat(actual.getUpdatedAt()).isEqualTo(A1.getUpdatedAt());
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
        final Answer answer = answers.save(A1);
        String content = "Updated Content";

        answer.setContents(content);

        final Answer actual = answers.findById(answer.getId()).get();
        assertThat(actual.getContents()).isEqualTo(content);
    }

    @Test
    void delete() {
        final Answer answer = answers.save(A1);
        assertThat(answers.findById(answer.getId())).isNotNull();
        answers.deleteById(answer.getId());
        assertThat(answers.findById(answer.getId())).isEmpty();
    }

}
