package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("findByIdAndDeletedFalse 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);

        Optional<Answer> answerOptional = answerRepository.findByIdAndDeletedFalse(1L);
        assertThat(answerOptional).map(Answer::getId).hasValue(savedAnswer.getId());
    }

    @DisplayName("findByIdAndDeletedFalse 테스트")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        Answer savedAnswer1 = answerRepository.save(AnswerTest.A1);
        Answer savedAnswer2 = answerRepository.save(AnswerTest.A2);

        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(1L);

        assertThat(answerList).hasSize(2);
        Answer answer1 = answerList.get(0);
        Answer answer2 = answerList.get(1);

        assertThat(answer1.getId()).isEqualTo(savedAnswer1.getId());
        assertThat(answer2.getId()).isEqualTo(savedAnswer2.getId());
    }

}
