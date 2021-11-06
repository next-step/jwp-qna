package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void 질문을_저장한다() {
        Question expected = questionRepository.save(QuestionTest.Q1);
        assertAll(
                () -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getWriterId()).isEqualTo(1L),
                () -> assertThat(expected.getTitle()).isEqualTo("title1"),
                () -> assertThat(expected.getContents()).isEqualTo("contents1"),
                () -> assertThat(expected.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(expected.getUpdatedAt()).isBefore(LocalDateTime.now())
        );
    }
}