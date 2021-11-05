package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void 정답을_저장한다() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        assertAll(
                () -> assertThat(answer.getContents()).isEqualTo("Answers Contents1"),
                () -> assertThat(answer.getId()).isEqualTo(1L),
                () -> assertThat(answer.getQuestionId()).isEqualTo(null),
                () -> assertThat(answer.getWriterId()).isEqualTo(1L),
                () -> assertThat(answer.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(answer.getUpdatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(answer.isDeleted()).isEqualTo(false)
        );
    }
}