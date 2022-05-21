package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfig;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void id로_조회() {
        // given
        Question question = new Question("title", "contents");
        Question saved = questionRepository.save(question);
        // when
        Optional<Question> result = questionRepository.findById(saved.getId());
        // then
        assertAll(
                () -> assertThat(result)
                        .map(Question::getTitle)
                        .hasValue("title"),
                () -> assertThat(result)
                        .map(Question::getContents)
                        .hasValue("contents")
        );
    }

    @Test
    void 저장() {
        // given
        Question question = new Question("title", "contents");
        // when
        Question result = questionRepository.save(question);
        // then
        assertThat(result).isNotNull();
    }
}