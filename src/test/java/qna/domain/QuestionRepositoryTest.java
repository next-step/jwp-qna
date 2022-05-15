package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    private Question question;

    @BeforeEach
    void setup() {
        question = questionRepository.save(Q1);
        questionRepository.save(Q2);
    }

    @DisplayName("해당 ID로 삭제상태가 아닌 질문을 찾는다.")
    @Test
    void findByIdAndDeletedFalse() {
        Long questionId = question.getId();
        Optional<Question> byId = questionRepository.findByIdAndDeletedFalse(questionId);
        Question question = byId.orElse(null);
        assertAll(
            () -> assertEquals(questionId, question.getId()),
            () -> assertEquals("contents1", question.getContents()),
            () -> assertEquals("title1", question.getTitle()),
            () -> assertEquals(1L, question.getWriterId()),
            () -> assertThat(question.isDeleted()).isFalse()
        );
    }

    @DisplayName("삭제 상태가 아닌 질문을 모두 찾는다.")
    @Test
    void findByDeletedFalse() {
        List<Question> questionList = questionRepository.findByDeletedFalse();
        assertEquals(2, questionList.size());
    }
}