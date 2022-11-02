package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import qna.NoneDdlDataJpaTest;

@NoneDdlDataJpaTest
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @Test
    void 질문_저장_및_찾기() {
        Question actual = questionRepository.save(Q1);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(Q1.getContents()),
            () -> assertThat(actual.getTitle()).isEqualTo(Q1.getTitle()),
            () -> assertThat(actual.isDeleted()).isFalse(),
            () -> assertThat(actual.getWriterId()).isEqualTo(Q1.getWriterId()),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isEqualTo(actual.getCreatedAt())
        );
        assertThat(questionRepository.findByIdAndDeletedFalse(actual.getId())).contains(actual);
    }

    @Test
    void 질문_복수개_저장_후_찾기() {
        Question q1 = questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);
        List<Question> findQuestions = questionRepository.findByDeletedFalse();
        assertThat(findQuestions).hasSize(2);
        assertThat(findQuestions).containsExactly(q1, q2);
    }

    @Test
    void 질문_삭제여부_변경() {
        Question savedQuestion = questionRepository.save(Q1);
        assertThat(questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())).isNotEmpty();
        savedQuestion.setDeleted(true);
        assertThat(questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())).isEmpty();
        savedQuestion.setDeleted(false);
    }
}