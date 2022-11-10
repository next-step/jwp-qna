package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.QUESTION_1;

@DisplayName("질문 Repository")
@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("저장_성공")
    @Test
    void save_success() {

        Question question = questionRepository.save(QUESTION_1);

        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getContents()).isEqualTo(QUESTION_1.getContents()),
                () -> assertThat(question.getTitle()).isEqualTo(QUESTION_1.getTitle()),
                () -> assertThat(question.isDeleted()).isFalse(),
                () -> assertThat(question.getWriterId()).isNotNull(),
                () -> assertThat(question.getCreatedAt()).isNotNull(),
                () -> assertThat(question.getUpdatedAt()).isNotNull()
        );
    }
}
