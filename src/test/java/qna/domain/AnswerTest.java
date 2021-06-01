package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1,
        "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1,
        "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("Answer 저장 테스트")
    public void saveTest() {
        Answer answerA1 = answerRepository.save(A1);

        assertThat(answerA1.getId()).isNotNull();
        assertThat(answerA1.getWriterId()).isEqualTo(answerA1.getWriterId());
        assertThat(answerA1.getQuestionId()).isEqualTo(answerA1.getQuestionId());
        assertThat(answerA1.getContents()).isEqualTo(answerA1.getContents());
    }

    @Test
    @DisplayName("Question id로 deleted false 찾기 테스트")
    void findByQuestionIdAndDeletedFalseTest() {
        Answer answer1 = answerRepository.save(A1);
        Answer answer2 = answerRepository.save(A2);

        List<Answer> actualList = answerRepository
            .findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertThat(actualList).contains(answer1, answer2);
    }

    @Test
    @DisplayName("answer id로 deleted false 찾기 테스트")
    void findByIdAndDeletedFalseTest() {
        Answer answer1 = answerRepository.save(A1);
        Answer result = answerRepository.findByIdAndDeletedFalse(answer1.getId()).get();
        assertThat(result.getId()).isEqualTo(answer1.getId());

        assertThat(result.getWriterId()).isEqualTo(answer1.getWriterId());
        assertThat(result.getQuestionId()).isEqualTo(answer1.getQuestionId());
        assertThat(result.getContents()).isEqualTo(answer1.getContents());
    }
}
