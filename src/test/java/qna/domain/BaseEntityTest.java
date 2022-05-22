package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfig;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class BaseEntityTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void 엔티티가_생성되면_생성일이_저장된다() {
        // given
        Answer answer = new Answer();
        // when
        Answer result = answerRepository.save(answer);
        // then
        assertThat(result.createdAt()).isBefore(LocalDateTime.now());
    }

    @Test
    void 엔티티가_수정되면_수정일이_갱신된다() {
        // given
        Answer answer = new Answer();
        Answer result = answerRepository.save(answer);
        result.writeContents("contents");
        // when
        answerRepository.flush();
        // then
        assertThat(result.createdAt()).isBefore(result.updatedAt());
    }
}