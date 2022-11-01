package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void init() {
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("입력된 questionId를 가지고, 삭제상태가 아닌 Answer를 가져올 수 있어야 한다")
    void findByQuestionIdAndDeletedFalse() {
        // given
        answerRepository.saveAll(Arrays.asList(A1, A2));

        // when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(A1.getQuestionId());

        // then
        assertThat(answers).usingFieldByFieldElementComparator()
                .containsExactly(A1, A2);
    }

    @Test
    @DisplayName("입력된 AnswerId에 해당하면서 삭제상태가 아닌 Answer를 가져올 수 있어야 한다")
    void findByIdAndDeletedFalse() {
        // given
        answerRepository.save(A2);

        // when
        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(A2.getId());

        // then
        assertThat(answer).contains(A2);
    }
}
