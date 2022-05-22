package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {

    @Autowired
    AnswerRepository answerRepository;

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 저장_테스트() {
        Answer expected = AnswerTest.A1;
        Answer result = answerRepository.save(expected);

        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 아이디로_조회() {
        Answer save = answerRepository.save(A1);
        Answer expected = answerRepository.findByIdAndDeletedFalse(save.getId()).get();
        assertThat(expected.getId()).isEqualTo(save.getId());
    }

    @Test
    void 수정하기() {
        Answer answer = answerRepository.save(A1);
        answer.setContents("Answer Contents3");
        Answer expected = answerRepository.findById(answer.getId()).get();
        assertThat(expected.getContents()).isEqualTo("Answer Contents3");
    }
}
