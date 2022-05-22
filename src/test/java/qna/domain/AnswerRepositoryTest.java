package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("AnswerRepository 클래스")
@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("저장")
    @Test
    void save() {
        final Answer saved = answerRepository.save(AnswerTest.A1);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getCreatedAt()).isNotNull(),
                () -> assertThat(saved.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("Answer Id 조회")
    @Test
    void findByIdAndDeletedFalse() {
        final Answer saved = answerRepository.save(AnswerTest.A1);
        final Answer finded = answerRepository.findByIdAndDeletedFalse(saved.getId()).get();
        assertThat(finded).isNotNull();
    }

    @DisplayName("Writer Id 조회")
    @Test
    void findByWriterIdAndDeletedFalse() {
        final Answer saved = answerRepository.save(AnswerTest.A1);
        final Answer finded = answerRepository.findByWriterIdAndDeletedFalse(saved.getWriterId()).get(0);
        assertThat(finded).isNotNull();
    }

    @DisplayName("Question Id 조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        final Answer saved = answerRepository.save(AnswerTest.A1);
        final Answer finded = answerRepository.findByQuestionIdAndDeletedFalse(saved.getQuestionId()).get(0);
        assertThat(finded).isNotNull();
    }

    @DisplayName("Contents 변경")
    @Test
    void updateContents() {
        final Answer saved = answerRepository.save(AnswerTest.A1);
        saved.setContents("updated");
        final Answer finded = answerRepository.findByIdAndDeletedFalse(saved.getId()).get();
        assertThat(finded.getContents()).isEqualTo("updated");
    }
}
