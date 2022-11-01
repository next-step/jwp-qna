package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@EnableJpaAuditing
class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @Test
    void answer_도메인_테스트() {
        answerRepository.save(A1);
        answerRepository.save(A2);

        Answer answer1 = answerRepository.findById(A1.getId()).get();
        Answer answer2 = answerRepository.findById(A2.getId()).get();

        assertThat(answer1).isEqualTo(A1);
        assertThat(answer2).isEqualTo(A2);

    }
}
