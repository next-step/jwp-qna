package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.QUESTION_1;
import static qna.domain.QuestionTest.QUESTION_2;

@DisplayName("질문 Repository")
@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    private Question question2;

    @BeforeEach
    void setUp() {
        question2 = questionRepository.save(QUESTION_2);
    }

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

    @DisplayName("findByDeletedFalse_조회_성공")
    @Test
    void findByDeletedFalse_success() {

        Question question = questionRepository.findByIdAndDeletedFalse(question2.getId()).orElse(null);

        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getContents()).isEqualTo(QUESTION_2.getContents()),
                () -> assertThat(question.getTitle()).isEqualTo(QUESTION_2.getTitle()),
                () -> assertThat(question.isDeleted()).isFalse(),
                () -> assertThat(question.getWriterId()).isNotNull(),
                () -> assertThat(question.getCreatedAt()).isNotNull(),
                () -> assertThat(question.getUpdatedAt()).isNotNull()
        );
    }
}
