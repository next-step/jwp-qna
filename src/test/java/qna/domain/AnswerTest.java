package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {

    @Autowired
    private AnswerRepository answerRepository;

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 답변_저장_후_조회_테스트() {
        Answer answer = answerRepository.save(A1);

        Optional<Answer> opAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId());

        assertThat(opAnswer).isNotEmpty();
        assertThat(opAnswer.get()).isEqualTo(answer);
    }
}
