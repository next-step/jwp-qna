package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void 답변_아이디로_조회() {
        Answer answer = answerRepository.save(A1);

        List<Answer> retrievedAnswer = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestionId());

        assertThat(retrievedAnswer).hasSize(1);
    }

    @Test
    void 질문_아이디로_조회() {
        Answer answer = answerRepository.save(A1);

        Answer retrievedAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();

        assertThat(retrievedAnswer).isEqualTo(answer);
    }
}
