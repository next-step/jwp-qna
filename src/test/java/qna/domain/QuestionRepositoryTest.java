package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("QuestionRepository 클래스")
@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("저장")
    @Test
    void save() {
        final Question saved = questionRepository.save(QuestionTest.Q1);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getCreatedAt()).isNotNull(),
                () -> assertThat(saved.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("Question Id 조회")
    @Test
    void findByIdAndDeletedFalse() {
        final Question saved = questionRepository.save(QuestionTest.Q1);
        final Question finded = questionRepository.findByIdAndDeletedFalse(saved.getId()).get();
        assertThat(finded).isNotNull();
    }

    @DisplayName("Writer Id 조회")
    @Test
    void findByWriterIdAndDeletedFalse() {
        questionRepository.save(QuestionTest.Q1);
        final Question finded = questionRepository.findByDeletedFalse().get(0);
        assertThat(finded).isNotNull();
    }

    @DisplayName("Writer 변경")
    @Test
    void updateWriter() {
        final Question saved = questionRepository.save(QuestionTest.Q1);
        saved.writeBy(UserTest.SANJIGI);
        final Question finded = questionRepository.findByIdAndDeletedFalse(saved.getId()).get();
        assertThat(finded.getWriterId()).isEqualTo(UserTest.SANJIGI.getId());
    }
}
