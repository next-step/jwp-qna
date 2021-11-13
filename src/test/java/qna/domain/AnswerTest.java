package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answers;

    @Test
    void save_답변생성() {
        Answer actual = answers.save(A1);
        Answer expected = answers.findById(actual.getId()).orElse(null);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByIdAndDeletedFalse_조회() {
        Answer actual = answers.save(A1);
        Answer expected = answers.findByIdAndDeletedFalse(actual.getId()).orElse(null);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByIdAndDeletedFalse_삭제_조회() {
        Answer actual = answers.save(A1);
        actual.setDeleted(true);
        Answer expected = answers.findByIdAndDeletedFalse(actual.getId()).orElse(null);
        assertThat(expected).isNull();
    }
}
