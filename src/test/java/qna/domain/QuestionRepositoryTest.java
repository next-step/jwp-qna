package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static qna.domain.QuestionTestFixture.Q1;
import static qna.domain.QuestionTestFixture.Q2;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestMethodOrder(OrderAnnotation.class)
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @Test
    @Order(1)
    @DisplayName("질문 저장")
    void save_question() {
        Question question = questionRepository.save(Q1);
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getId()).isEqualTo(Q1.getId()),
                () -> assertThat(question.getTitle()).isEqualTo(Q1.getTitle()),
                () -> assertThat(question.getContents()).isEqualTo(Q1.getContents()),
                () -> assertThat(question.getWriterId()).isEqualTo(Q1.getWriterId()),
                () -> assertThat(question.getCreatedAt()).isEqualTo(Q1.getCreatedAt()),
                () -> assertThat(question.getUpdatedAt()).isEqualTo(Q1.getUpdatedAt())
        );
    }
    
    @Test
    @Order(2)
    @DisplayName("ID로 질문 조회 (삭제되지 않음)")
    void find_question_by_id_and_deleted_false() {
        Question saveQuestion = questionRepository.save(Q1);
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId());

        assertTrue(findQuestion.isPresent());
        findQuestion.ifPresent(question ->
                assertAll(
                        () -> assertThat(question.getId()).isNotNull(),
                        () -> assertThat(question.getId()).isEqualTo(saveQuestion.getId()),
                        () -> assertThat(question.getTitle()).isEqualTo(saveQuestion.getTitle()),
                        () -> assertThat(question.getContents()).isEqualTo(saveQuestion.getContents()),
                        () -> assertThat(question.getWriterId()).isEqualTo(saveQuestion.getWriterId()),
                        () -> assertFalse(question.isDeleted()),
                        () -> assertThat(question.getCreatedAt()).isEqualTo(saveQuestion.getCreatedAt()),
                        () -> assertThat(question.getUpdatedAt()).isEqualTo(saveQuestion.getUpdatedAt())
                )
        );
    }

    @Test
    @Order(3)
    @DisplayName("ID로 질문 조회 (삭제)")
    void find_question_by_id_and_deleted_true() {
        Question saveQuestion = questionRepository.save(Q1);
        saveQuestion.setDeleted(true);
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedTrue(saveQuestion.getId());
        assertTrue(findQuestion.isPresent());
        findQuestion.ifPresent(question ->
                assertAll(
                        () -> assertThat(question.getId()).isNotNull(),
                        () -> assertThat(question.getId()).isEqualTo(saveQuestion.getId()),
                        () -> assertThat(question.getTitle()).isEqualTo(saveQuestion.getTitle()),
                        () -> assertThat(question.getContents()).isEqualTo(saveQuestion.getContents()),
                        () -> assertThat(question.getWriterId()).isEqualTo(saveQuestion.getWriterId()),
                        () -> assertTrue(question.isDeleted()),
                        () -> assertThat(question.getCreatedAt()).isEqualTo(saveQuestion.getCreatedAt()),
                        () -> assertThat(question.getUpdatedAt()).isEqualTo(saveQuestion.getUpdatedAt())
                )
        );
    }

    @Test
    @Order(4)
    @DisplayName("질문 목록 조회 (삭제되지 않음)")
    void find_question_by_deleted_false() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).hasSize(2);
    }

    @Test
    @Order(5)
    @DisplayName("질문 삭제여부 변경")
    void question_deleted_true() {
        Question question = questionRepository.save(Q1);
        question.setDeleted(true);
        questionRepository.flush();
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(question.getId());
        assertAll(
                () -> assertNull(findQuestion.orElse(null)),
                () -> assertFalse(findQuestion.isPresent()),
                () -> assertTrue(question.isDeleted())
        );
    }
}
