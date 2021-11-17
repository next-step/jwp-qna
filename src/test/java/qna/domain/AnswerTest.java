package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answers;

    @DisplayName("A1 Answer 정보 저장 및 데이터 확인")
    @Test
    void saveAnswer() {
        final Answer actual = answers.save(A1);

        User writer = actual.getWriter();
        Question question = actual.getQuestion();

        assertThat(writer).isEqualTo(A1.getWriter());
        assertThat(question).isEqualTo(A1.getQuestion());
    }

    @DisplayName("writer_id로 데이터 조회")
    @Test
    void findByWriterId() {
        final Answer standard = answers.save(A1);
        final Answer target = answers.findByWriterId(UserTest.JAVAJIGI.getId());

        User standardWriter = standard.getWriter();
        User targetWriter = target.getWriter();

        assertThat(standardWriter).isEqualTo(targetWriter);
    }

    @DisplayName("QuestionId로 데이터 조회")
    @Test
    void findByQuestionId() {
        final Answer standard = answers.save(A1);
        final List<Answer> target = answers.findByQuestionId(QuestionTest.Q1.getId());

        assertThat(target).contains(standard);
    }

    @DisplayName("Id 와 Deleted 값이 fasle 인 값 찾기")
    @Test
    void findByIdAndDeletedFalse() {
        answers.save(A2);
        final Answer target = answers.findByIdAndDeletedFalse(A2.getId()).get();

        boolean targetDeleted = target.isDeleted();

        assertThat(targetDeleted).isFalse();
    }

    @DisplayName("Contents 값 'ents1' Like 찾기")
    @Test
    void findByContentsLike() {
        answers.save(A1);
        final Answer target = answers.findByContentsLike("%ents1%");

        String targetContents = target.getContents();

        assertThat(targetContents).contains("ents1");
    }
}
