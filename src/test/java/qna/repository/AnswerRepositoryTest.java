package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.repository.entity.Answer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static qna.repository.entity.AnswerTest.*;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired()
    private AnswerRepository answerRepository;
    private Answer answer;

    @BeforeEach
    void setup() {
        answer = answerRepository.save(A1);
        answerRepository.save(A2);
    }

    @Test()
    @DisplayName("정상 상태의 답변을 ID로 찾는다")
    void findByIdAndDeletedFalse() {
        Optional<Answer> find = answerRepository.findByIdAndDeletedFalse(A1.getId());
        Answer answer = find.orElse(null);

        assertAll(
                () -> assertThat(find).isNotEmpty(),
                () -> assertEquals(A1.getContents(), answer.getContents()),
                () -> assertEquals(A1.getWriterId(), answer.getWriterId()),
                () -> assertEquals(A1.getQuestionId(), answer.getQuestionId()),
                () -> assertThat(answer.isDeleted()).isFalse()
        );
    }

    @Test()
    @DisplayName("정상 상태의 답변을 Question ID로 찾는다")
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestionId());

        for (Answer answer : answers) {
            assertThat(answer.getId()).isNotNull();
        }

        assertEquals(2, answers.size());
    }
}
