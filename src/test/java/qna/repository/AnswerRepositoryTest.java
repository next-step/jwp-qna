package qna.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.AnswerTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Test
    void findAll() {
        Answer expected = answerRepository.save(AnswerTest.A1);
        List<Answer> actual = answerRepository.findAll();
        assertAll(
                () -> assertThat(actual.get(0).getWriterId()).isEqualTo(expected.getWriterId()),
                () -> assertThat(actual.get(0).getCreatedAt()).isNotNull()
        );
    }

    @Test
    void findByIdAndDeletedFalse() {
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);
        Answer answer = answerRepository.findByIdAndDeletedFalse(AnswerTest.A2.getId()).get();
        assertAll(
                () -> assertThat(answer).isNotNull(),
                () -> assertThat(answer.getId()).isNotEqualTo(AnswerTest.A1.getId()),
                () -> assertThat(answer.getContents()).isEqualTo(AnswerTest.A2.getContents())
        );
    }
}
