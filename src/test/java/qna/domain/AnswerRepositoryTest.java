package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void 답변_저장() {
        Answer actual = new Answer(
            new User("userId", "password", "name", "email"),
            new Question("title", "contents"),
            "contents");
        Answer expected = answerRepository.save(actual);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void id로_답변_조회() {
        Answer actual = new Answer(
            new User("userId", "password", "name", "email"),
            new Question("title", "contents"),
            "contents");
        Answer saved = answerRepository.save(actual);
        Optional<Answer> expected = answerRepository.findByIdAndDeletedFalse(saved.getId());
        assertThat(expected).hasValue(actual);
    }

    @Test
    void 질문id로_답변_조회() {
        Answer actual = new Answer(
            new User("userId", "password", "name", "email"),
            new Question("title", "contents"),
            "contents");
        Answer saved = answerRepository.save(actual);
        Optional<Answer> expected = answerRepository.findByIdAndDeletedFalse(saved.getId());
        assertThat(expected).hasValue(actual);
    }
}
