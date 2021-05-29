package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("save 확인")
    void save() {
        Answer answer1 = answerRepository.save(A1);
        assertAll(
                () -> assertThat(answer1.getId()).isNotNull(),
                () -> assertThat(answer1.getWriterId()).isEqualTo(A1.getWriterId()),
                () -> assertThat(answer1.getQuestionId()).isEqualTo(A1.getQuestionId()),
                () -> assertThat(answer1.getContents()).isEqualTo(A1.getContents())
        );
    }

    @Test
    @DisplayName("question id 값으로 deleted false 리스트 확인")
    void findByQuestionIdAndDeletedFalse() {
        Answer answer1 = answerRepository.save(A1);
        Answer answer2 = answerRepository.save(A2);
        List<Answer> actualList = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertThat(actualList).contains(answer1, answer2);
    }

    @Test
    @DisplayName("Deleted값이 false인 answer 찾기")
    void findByIdAndDeletedFalse() {
        Answer answer1 = answerRepository.save(A1);
        Answer result = answerRepository.findByIdAndDeletedFalse(answer1.getId()).get();
        assertThat(result.getWriterId()).isEqualTo(answer1.getId());
        assertAll(
                () -> assertThat(result.getWriterId()).isEqualTo(answer1.getWriterId()),
                () -> assertThat(result.getQuestionId()).isEqualTo(answer1.getQuestionId()),
                () -> assertThat(result.getContents()).isEqualTo(answer1.getContents())
        );
    }
}
