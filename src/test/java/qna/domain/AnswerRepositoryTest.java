package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void 답변_저장_후_조회_테스트() {
        userRepository.save(AnswerTest.A1.getWriter());
        questionRepository.save(AnswerTest.A1.getQuestion());
        Answer answer = answerRepository.save(AnswerTest.A1);

        Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId());

        assertThat(optionalAnswer).isNotEmpty();
        assertThat(optionalAnswer.get()).isEqualTo(answer);
    }
}
